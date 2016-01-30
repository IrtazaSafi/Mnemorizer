__author__ = 'Irtaza Safi'
from databaseConnectionManager import databaseConnectionManager
from VocabularyWord import VocabularyWord
from Mnemonic import Mnemonic
import jsonpickle
import json
from math import cos, asin, sqrt



class ObjectBuilder:
    def __init__(self):

        self.dbConnection = databaseConnectionManager()

    def distance(self,lat1, lon1, lat2, lon2):


        p = 0.017453292519943295
        a = 0.5 - cos((lat2 - lat1) * p)/2 + cos(lat1 * p) * cos(lat2 * p) * (1 - cos((lon2 - lon1) * p)) / 2
        return 12742 * asin(sqrt(a))

    def generateWordObjects(self,latitude,longitude):
        wordList = []
        finaloutput = ""
        for row in self.dbConnection.fetchWords():


            wordList.append(VocabularyWord(row[0], row[1], row[2], row[3]))


        for word in wordList:
            mnemonics = self.dbConnection.fetchMnemonicsForWord(word.id)
            for row in mnemonics:
                mnemonic = Mnemonic(row[0],row[1],row[2],row[3],float(row[4]),float(row[5]),row[6])
                mnemonic.priority = mnemonic.score
                    #self.distance(latitude,longitude,mnemonic.latitude,mnemonic.longitude)
                word.mnemonics.append(mnemonic)


            # jsonWord = "-" +jsonpickle.encode(word._returnAsSerializable())
            # #jsonWord = "-" +json.dumps(word._returnAsSerializable().__dict__)
            # finaloutput = finaloutput + jsonWord

        for word in wordList:
            word.mnemonics.sort(key=lambda x:x.priority,reverse =True)


        return wordList


        # generate JSON objects FOR EACH WORD AND APPEND TO STRING



        return finaloutput


#builder = ObjectBuilder()
#wordList = builder.generateWordObjects()
# print wordList[2].mnemonics[2]
# for word in wordList:
#     print word.word + " " + word.meaning
#
# for mnemonic in builder.dbConnection.fetchMnemonicsForWord(2)
#     print mnemonic[0]