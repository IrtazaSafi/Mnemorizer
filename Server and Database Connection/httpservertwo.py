__author__ = 'Irtaza Safi'

from BaseHTTPServer import HTTPServer, BaseHTTPRequestHandler
from SocketServer import ThreadingMixIn
import threading
from databaseConnectionManager import databaseConnectionManager

HOST_NAME = '192.168.10.7'
PORT = 80

dbConnection = databaseConnectionManager()


class Handler(BaseHTTPRequestHandler):
    def do_GET(self):
        self.send_response(200)
        self.send_header("Content-type", "text/html")
        self.end_headers()
        message = threading.currentThread().getName()
        print message
        # handle Request
        pathArray = self.path.split('-')

        if pathArray[1] == "loginRequest":
            id = dbConnection.validateUser(pathArray[2], pathArray[3])
            # print "id"+ id
            if id is not 0:
                self.wfile.write("Validated-" + str(id))
                print "Validated"
            else:
                self.wfile.write("Invalid")
        if pathArray[1] == "signUpRequest":
            print "signUpRequest recieved"
            self.wfile.write(self.path)


class ThreadedHTTPServer(ThreadingMixIn, HTTPServer):
    """Handle requests in a separate thread."""


if __name__ == '__main__':
    server = ThreadedHTTPServer((HOST_NAME, PORT), Handler)
    print 'Starting server, use <Ctrl-C> to stop'
    server.serve_forever()
