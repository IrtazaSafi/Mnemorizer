__author__ = 'Irtaza Safi'
import json
from json import JSONEncoder


class VocabularyWord(JSONEncoder):

    def __init__(self,_id,_word,_deckID,_meaning):
        self.id = _id
        self.word = _word
        self.deckid = _deckID
        self.meaning = _meaning
        self.mnemonics = []


    def _returnAsSerializable(self):
        word = VocabularyWord(self.id,self.word,self.deckid,self.meaning)
        word.mnemonics = list(self.mnemonics)
        return word


