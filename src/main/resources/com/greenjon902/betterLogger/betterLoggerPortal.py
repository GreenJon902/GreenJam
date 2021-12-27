import socket
import sys
import time

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

    def flush(self):
        pass


connSender = ConnSender()
connSender.current_send_type = "LOG"
#sys.stdout = connSender

errorConnSender = ConnSender()
errorConnSender.current_send_type = "ERROR"
#sys.stderr = errorConnSender

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
    type_ = conn.recv(int(conn.recv(4).decode("utf8"))).decode("utf8")
    message = conn.recv(int(conn.recv(8).decode("utf8"))).decode("utf8")

    if type_ == "CTRL":
        if message == "END":
            send("CTRL", "END")
            conn.recv(1)  # Wait for closed signal
            conn.close()
            break
