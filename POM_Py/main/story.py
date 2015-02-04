'''
Created on 4 Feb 2015

@author: Bin.Lu
'''
class Story:
    
    def __init__(self, ID):
        self.ID = ID
        self.hasGood = False
        self.hasBad = False
        
    def SetStoryBody(self, storyBody):
        self.storyBody = storyBody.lower()
    
    def SetStoryTitle(self, storyTitle):
        self.storyTitle = storyTitle.lower()

    def SetGood(self, good):
        self.good = good.replace('nothing', '')
        self.good = self.good.replace(';;', ';').lower()
        self.hasGood = len(self.good) > 0
        
    def SetBad(self, bad):
        self.bad = bad.replace('nothing', '')
        self.bad = self.bad.replace(';;', ';').lower()
        self.hasBad = len(self.bad) > 0
        
    def GetGoodAsVec(self):
        return self.good.split(';')

    def GetBadAsVec(self):
        return self.bad.split(';')

    def GetGoodLen(self):
        tempGood = self.good.replace(';', ' ')
        return len(tempGood.split())
    
    def GetBadLen(self):
        tempBad = self.bad.replace(';', ' ')
        return len(tempBad.split())