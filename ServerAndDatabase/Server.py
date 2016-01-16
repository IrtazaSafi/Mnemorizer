__author__ = 'Irtaza Safi'
from BaseHTTPServer import HTTPServer, BaseHTTPRequestHandler
from SocketServer import ThreadingMixIn
import threading
from databaseConnectionManager import databaseConnectionManager
from VocabularyWord import VocabularyWord
import json
from Mnemonic import Mnemonic
import jsonpickle
from ObjectBuilder import ObjectBuilder

HOST_NAME = '192.168.10.6'
PORT = 80

dbConnection = databaseConnectionManager()
objectBuilder = ObjectBuilder()


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

        if pathArray[1] == "test":
            # word = VocabularyWord(1, 'apple', 1, "cool")
            # # def __init__(self,_ID,_mnemonic,_word,_score,_creatorID,_latitude,_longitude):
            # helper = Mnemonic(10, "cool cool afridi", "apple", 20, 1, 3.12112, 33.1414)
            # word.mnemonics.append(helper)
            # # data = json.dumps(word._returnAsSerializable().__dict__)
            data = jsonpickle.encode(list(objectBuilder.generateWordObjects()))#objectBuilder.generateWordObjects() #
            print data
            self.wfile.write("json-" + data)


class ThreadedHTTPServer(ThreadingMixIn, HTTPServer):
    """Handle requests in a separate thread."""


if __name__ == '__main__':
    server = ThreadedHTTPServer((HOST_NAME, PORT), Handler)
    print 'Starting server, use <Ctrl-C> to stop'
    server.serve_forever()
