import json
import traceback
from VocabularyWord import VocabularyWord

__author__ = 'Irtaza Safi'

import MySQLdb


class databaseConnectionManager:
    def __init__(self):
        self.db = MySQLdb.connect(host="localhost",  # your host, usually localhost
                                  user="root",  # your username
                                  passwd="xCode113",  # your password
                                  db="mnemorizer")  # name of the data base
        self.queryExecutor = self.db.cursor()

    def viewAllUsers(self):
        self.queryExecutor.execute("SELECT id,email,password FROM USERS")
        # for row in self.queryExecutor.fetchall():
        #     print str(row[0]) + " " + row[1] + " " + row[2]
        return self.queryExecutor.fetchall();

    def validateUser(self, email, password):
        self.queryExecutor.execute("SELECT id FROM USERS WHERE email=%s AND password = %s", (email, password));
        returnValue = 0;
        for row in self.queryExecutor.fetchall():
            returnValue += row[0]

        return returnValue

    def checkUserExists(self, email):
        self.queryExecutor.execute("SELECT id FROM USERS WHERE email =%s", (email))
        if self.queryExecutor.fetchone() != None:

            return True
        else:

            return False

    def createUser(self, email, password, latitude, longitude):
        try:

            if (self.checkUserExists(email)):
                print "User Already exists"
                return -1
            self.queryExecutor.execute("SELECT MAX(id) FROM USERS")
            returnValue = self.queryExecutor.fetchone()[0]

            newID = returnValue + 1;
            self.queryExecutor.execute(
                "INSERT INTO USERS (id,email,password,latitude,longitude) VALUES (%s,%s,%s,%s,%s)",
                (newID, email, password, latitude, longitude))

            self.db.commit()
            print "created successfully"
            return newID

        except:
            print "something went wrong with database query"
            return 0

    def test(self):

        self.queryExecutor.execute("SELECT email FROM USERS")
        for row in self.cur.fetchall():
            print row[0]

    def fetchWords(self):
        self.queryExecutor.execute(
            "SELECT WORDS.id,WORDS.word,WORDS.deckid,MEANINGS.meaning FROM WORDS,MEANINGS WHERE WORDS.id = MEANINGS.wordid")
        return self.queryExecutor.fetchall()

    def fetchMnemonicsForWord(self, _id):
        self.queryExecutor.execute(
            "SELECT DISTINCT MNEMONICS.id,MNEMONICS.mnemonic,MNEMONICS.wordid,MNEMONICS.creatorid,MNEMONICS.latitude,MNEMONICS.longitude,MNEMONICS.score  FROM WORDS,MNEMONICS WHERE MNEMONICS.wordid = %s",
            (_id))
        return self.queryExecutor.fetchall()

    def createMnemonic(self, mnemonic, wordid, creatorid, latitude, longitude):
        self.queryExecutor.execute("SELECT MAX(id) FROM MNEMONICS")
        returnValue = self.queryExecutor.fetchone()[0]
        newID = returnValue + 1
        score = 0

        self.queryExecutor.execute("INSERT INTO MNEMONICS(id,mnemonic,wordid,creatorid,latitude,longitude,score) VALUES (%s,%s,%s,%s,%s,%s,%s)",
                                   (newID,mnemonic,wordid,creatorid,latitude,longitude,score))

        self.db.commit()
        print "mnemonic inserted successfuly"
        return newID


        print "error submitting to database"
        return 0

    def giveThumbsUp(self,id):

        self.queryExecutor.execute("UPDATE MNEMONICS SET score = score+1 WHERE id=%s",(id))
        self.db.commit()
        return 1


        traceback.print_exception()
        print("error updating database")
        return 0

    def giveThumbsDown(self,id):

        self.queryExecutor.execute("UPDATE MNEMONICS SET score = score-1 WHERE id=%s",(id))
        self.db.commit()
        return 1


        traceback.print_exception()
        print("error updating database")
        return 0



#dbConnection = databaseConnectionManager()

#dbConnection.createMnemonic("cool cool safidi",5,1,34.5534,74.34434)

#dbConnection.viewAllUsers()
