__author__ = 'Irtaza Safi'
from databaseConnectionManager import databaseConnectionManager
from VocabularyWord import VocabularyWord
from Mnemonic import Mnemonic
import jsonpickle
import json


class ObjectBuilder:
    def __init__(self):

        self.dbConnection = databaseConnectionManager()

    def generateWordObjects(self):
        wordList = []
        finaloutput = ""
        for row in self.dbConnection.fetchWords():
            wordList.append(VocabularyWord(row[0], row[1], row[2], row[3]))


        for word in wordList:
            mnemonics = self.dbConnection.fetchMnemonicsForWord(word.id)
            for row in mnemonics:
                word.mnemonics.append(Mnemonic(row[0],row[1],row[2],row[3],float(row[4]),float(row[5]),row[6]))
                #sex = json.dumps(word.mnemonics[0].__dict__)
                #print word.word + " " + mnemonic[0] + " " + str(word.deckid)

            jsonWord = "-" +jsonpickle.encode(word._returnAsSerializable())
            #jsonWord = "-" +json.dumps(word._returnAsSerializable().__dict__)
            finaloutput = finaloutput + jsonWord


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