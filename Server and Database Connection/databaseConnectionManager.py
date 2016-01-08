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
        self.queryExecutor.execute("SELECT * FROM USERS")
        for row in self.queryExecutor.fetchall():
            print str(row[0]) + " " + row[1] + " " + row[2] + " " + str(row[3])+ " " +str(row[4])


    def validateUser(self, email, password):
        self.queryExecutor.execute("SELECT ID FROM USERS WHERE Email=%s AND Password = %s", (email, password));
        returnValue = 0;
        for row in self.queryExecutor.fetchall():
            returnValue += row[0]

        return returnValue

    def checkUserExists(self, email):
        self.queryExecutor.execute("SELECT ID FROM USERS WHERE Email =%s", (email))
        if self.queryExecutor.fetchone() != None:

            return True
        else:

            return False

    def createUser(self, email, password, latitude, longitude):
        if (self.checkUserExists(email)):
            print "User Already exists"

            return 0

        try:
            self.queryExecutor.execute("SELECT MAX(ID) FROM USERS")
            returnValue = self.queryExecutor.fetchone()[0]


            newID = returnValue + 1;
            self.queryExecutor.execute(
                "INSERT INTO USERS (ID,Email,Password,Latitude,Longitude) VALUES (%s,%s,%s,%s,%s)",
                (newID, email, password, latitude, longitude))

            self.db.commit()
            print "created successfully"
            return newID

        except:
            print "something went wrong with database query"
            return 0

    def test(self):

        self.queryExecutor.execute("SELECT * FROM USERS")
        for row in self.cur.fetchall():

            print row[0] << " " << row[1] << " " << row[2] << " " << row[3];




# dbConnection = databaseConnectionManager();
# #
# # dbConnection.createUser("alisafi99@gmail.com", "2k16", 30.115, 20.112)
# # dbConnection.viewAllUsers()
#
# #dbConnection.deleteUser("irtaza.safi@gmail.com")
#dbConnection.viewAllUsers()
