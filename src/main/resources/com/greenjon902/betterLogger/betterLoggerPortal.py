import socket
import sys
import traceback


def debug(string, end="\n"):
    ORIGINAL_STDOUT.write(string + end)
    ORIGINAL_STDOUT.flush()


ORIGINAL_STDOUT = sys.stdout


args = sys.argv
if len(args) != 2:
    raise Exception("Wrong amount of arguments, should be 1 - the port")

ip = ("127.0.0.1", args[1])
conn = socket.create_connection(ip)


def send(type_, string):
    encoded_type = f"{len(type_):04}{type_}".encode("utf8")
    encoded_string = f"{len(string):08}{string}".encode("utf8")

    conn.send(encoded_type + encoded_string)


class ConnSender:
    current_send_type = ""

    def write(self, string: str):
        send(self.current_send_type, string)
        debug(f"<PROGRAM> [{self.current_send_type}]  {string}", end="")

    def flush(self):
        pass


connSender = ConnSender()
connSender.current_send_type = "LOG"
sys.stdout = connSender

errorConnSender = ConnSender()
errorConnSender.current_send_type = "ERROR"
sys.stderr = errorConnSender

try:
    try:
        import betterLogger
    except ImportError:
        send("INFO", "betterLogger not installed")
        send("INFO", "Installing betterLogger")

        connSender.current_send_type = "PIP_INSTALL"
        import subprocess

        subprocess.run([sys.executable, '-m', 'pip', 'install', "betterLogger"])

        connSender.current_send_type = "LOG"
        try:
            import betterLogger
        except ImportError:
            send("ERROR", "Install of betterLogger failed")
            sys.exit(1)
        send("INFO", "Installed betterLogger")

    while True:
        type_ =   conn.recv(int(conn.recv(4).decode("ascii"))).decode("ascii")
        message = conn.recv(int(conn.recv(8).decode("ascii"))).decode("ascii")
        debug(f"Got \"{type_}\" \"{message}\"")

        if type_ == "CTRL":
            if message == "END":
                betterLogger.root_logger.info("Finishing, Cya!")
                #betterLogger.console_handler.flush()

                #time.sleep(1)

                send("CTRL", "END")
                debug("SENT CLOSE SIGNAL, WAITING FOR CONFIRMATION")
                conn.recv(1)  # Wait for closed signal
                debug("RECIEVED")
                conn.close()
                break
except Exception as e:
    errorConnSender.write(traceback.format_exc()) # This puts the entire error into one message

debug("DONE!")
