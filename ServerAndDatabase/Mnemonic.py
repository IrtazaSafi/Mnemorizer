__author__ = 'Irtaza Safi'
from json import JSONEncoder


class Mnemonic(JSONEncoder):
    def __init__(self,_ID,_mnemonic,_wordid,_creatorID,_latitude,_longitude,_score):
        self.id = _ID
        self.mnemonic = _mnemonic
        self.wordid = _wordid
        self.score = _score
        self.creatorid = _creatorID
        self.latitude = _latitude
        self.longitude = _longitude


