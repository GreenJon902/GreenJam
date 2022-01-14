import os
import socket
import sys
import traceback

from betterLoggerPortalExceptions import UnknownCtrlCommandException, NoLoggerWithThatIdException, \
    IllegalLogLevelException


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
        debug(f"<PROGRAM_OUT> [{self.current_send_type}]  {string}", end="")

    def flush(self):
        pass


connSender = ConnSender()
connSender.current_send_type = "LOG"
sys.stdout = connSender

errorConnSender = ConnSender()
errorConnSender.current_send_type = "ERROR"
sys.stderr = open("./error", "a")
sys.stderr.write("--------------------------------")

try:
    try:
        os.environ["LOG_LEVEL"] = "1"
        os.environ["LOG_NAMES_TO_SHORTEN"] = '{"matchTemplatesAndGetLength": "mtagl"}'
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


    from betterLogger.classWithLogger import ClassWithLogger
    loggers: dict[ClassWithLogger] = dict()

    while True:
        type_ = conn.recv(int(conn.recv(4).decode("ascii"))).decode("ascii")
        message = conn.recv(int(conn.recv(8).decode("ascii"))).decode("ascii")
        debug(f"Got \"{type_}\" \"{message}\"")

        if type_ == "CTRL":
            if message == "END":
                betterLogger.root_logger.info("Finishing, Cya!")

                send("CTRL", "END")
                debug("SENT CLOSE SIGNAL, WAITING FOR CONFIRMATION")
                conn.recv(1)  # Wait for closed signal
                debug("RECIEVED")
                conn.close()
                break

            elif message.startswith("NEW_LOGGER"):
                ctrl_code, loggerId, logger_name = message.split(":", 2)
                loggers[loggerId] = betterLogger.get_logger(logger_name)

            elif message.startswith("PUSH_NAME"):
                ctrl_code, loggerId, name_to_push = message.split(":", 2)
                if loggerId not in loggers:
                    raise NoLoggerWithThatIdException(f"No logger with the id {loggerId}")
                loggers[loggerId].push_logger_name(name_to_push)

            elif message.startswith("POP_NAME"):
                ctrl_code, loggerId = message.split(":", 1)
                if loggerId not in loggers:
                    raise NoLoggerWithThatIdException(f"No logger with the id {loggerId}")
                loggers[loggerId].pop_logger_name()

            else:
                raise UnknownCtrlCommandException(f"Unknown ctrl command \"{message}\"")

        if type_ == "LOG":
            loggerId, level, message = message.split(":", 2)

            if loggerId not in loggers:
                raise NoLoggerWithThatIdException(f"No logger with the id {loggerId}")

            if hasattr(loggers[loggerId], f"log_{level.lower()}"):
                getattr(loggers[loggerId], f"log_{level.lower()}")(message)
            else:
                raise IllegalLogLevelException(f"No log level with the name \"{level}\"")


except Exception as e:
    sys.stderr.write(traceback.format_exc())
    try:
        import betterLogger
        betterLogger.root_logger.error(traceback.format_exc())
    except Exception as e2:
        print(e2)
    errorConnSender.write(traceback.format_exc())  # This puts the entire error into one message

debug("DONE!")
