from decimal import Decimal

__author__ = 'Irtaza Safi'

from BaseHTTPServer import HTTPServer, BaseHTTPRequestHandler
from SocketServer import ThreadingMixIn
import threading
from databaseConnectionManager import databaseConnectionManager

HOST_NAME = '10.130.2.78'
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

            id = dbConnection.createUser(pathArray[2],pathArray[3],Decimal(pathArray[4]),Decimal(pathArray[5]))
            if id is not 0:
                self.wfile.write("Created-"+str(id))
                print("Account Created")
                dbConnection.viewAllUsers()
            else:
                self.wfile.write("createError")
                print ("Error Creating User")



class ThreadedHTTPServer(ThreadingMixIn, HTTPServer):
    """Handle requests in a separate thread."""


if __name__ == '__main__':
    server = ThreadedHTTPServer((HOST_NAME, PORT), Handler)
    print 'Starting server, use <Ctrl-C> to stop'
    server.serve_forever()
