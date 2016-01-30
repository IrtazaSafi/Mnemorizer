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
import urllib

HOST_NAME = '192.168.10.4'
PORT = 80

dbConnection = databaseConnectionManager()
objectBuilder = ObjectBuilder()
#sub = Mnemonic(0,0,0,0,0,0,0)


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
            latitude = float(pathArray[4])
            longitude = float(pathArray[5])
            # print "id"+ id
            if id is not 0:
                data = jsonpickle.encode(list(objectBuilder.generateWordObjects(latitude,longitude)))
                dbConnection.updateUserLocation(id,latitude,longitude)
                self.wfile.write("Validated-" + str(id)+"-"+data)
                print "Validated"
            else:
                self.wfile.write("Invalid")

        if pathArray[1] == "loginRequestVerif":
            latitude = float(pathArray[3])
            longitude = float(pathArray[4])
            dbConnection.updateUserLocation(int(pathArray[2]),latitude,longitude)
            data = jsonpickle.encode(list(objectBuilder.generateWordObjects(latitude,longitude)))
            self.wfile.write("Validated-" + str(pathArray[2])+"-"+data)
            print "Validated"


        if pathArray[1] == "signUpRequest":
            print "signUpRequest recieved"


            email = pathArray[2]
            password = pathArray[3]
            latitude = pathArray[4]
            longitude = pathArray[5]

            code = dbConnection.createUser(email,password,latitude,longitude)

            if code is -1:
                self.wfile.write("error-Exists")

            elif code is 0:
                self.wfile.write("error-dbError")
            else:
                data = jsonpickle.encode(list(objectBuilder.generateWordObjects(latitude,longitude)))
                self.wfile.write("success-"+ str(code)+"-"+data)

        if pathArray[1] == "test":
            # word = VocabularyWord(1, 'apple', 1, "cool")
            # # def __init__(self,_ID,_mnemonic,_word,_score,_creatorID,_latitude,_longitude):
            # helper = Mnemonic(10, "cool cool afridi", "apple", 20, 1, 3.12112, 33.1414)
            # word.mnemonics.append(helper)
            # # data = json.dumps(word._returnAsSerializable().__dict__)
            data = jsonpickle.encode(list(objectBuilder.generateWordObjects(31.4712731,74.2531819)))#objectBuilder.generateWordObjects() #
            print data
            self.wfile.write("json-" + data)

        if pathArray[1] == "mnemonicSubmission":

            rawRequestVector = urllib.unquote_plus(self.path).decode('utf8').split('-')

            creatorid = int(rawRequestVector[2])
            wordid = int(rawRequestVector[3])

            mnemonic = rawRequestVector[4]
            latitude = float(rawRequestVector[5])
            longitude = float(rawRequestVector[6])

            code = dbConnection.createMnemonic(mnemonic,wordid,creatorid,latitude,longitude)

            if code is not 0:
                self.wfile.write("success-" + str(code))

            else:
                self.wfile.write("dbQueryError")


        if pathArray[1] == "ratingQuery":
            id = int(pathArray[3])
            if pathArray[2] == "thumbsUp":
                code = dbConnection.giveThumbsUp(id)
                if code is not 0:
                    self.wfile.write("successLIKE")
                else:
                    self.wfile.write("dbQueryError")

            else:
                code = dbConnection.giveThumbsDown(id)
                if code is not 0:
                    self.wfile.write("successUNLIKE")
                else:
                    self.wfile.write("dbQueryError")














class ThreadedHTTPServer(ThreadingMixIn, HTTPServer):
    """Handle requests in a separate thread."""


if __name__ == '__main__':
    server = ThreadedHTTPServer((HOST_NAME, PORT), Handler)
    print 'Starting server, use <Ctrl-C> to stop'
    server.serve_forever()
