import socket
import sys

args = sys.argv
if len(args) != 2:
    raise Exception("Wrong amount of arguments, should be 1 - the port")

ip = ("127.0.0.1", args[1])
conn = socket.create_connection(ip)


def send(type_, string):
    encoded_string = (type_ + ":" + string).encode("utf8")
    conn.send(len(encoded_string) + encoded_string)


try:
    import betterLogger
except ImportError:
    send("INFO", "betterLogger not installed")
    send("INFO", "Installing betterLogger")

    import subprocess
    subprocess.call([sys.executable, '-m', 'pip', 'install', "betterLogger"])

    try:
        import betterLogger
    except ImportError:
        send("ERROR", "Install of betterLogger failed")
        sys.exit(1)
    send("INFO", "Installed betterLogger")
