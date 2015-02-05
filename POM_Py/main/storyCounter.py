'''
Created on 5 Feb 2015

@author: Bin.Lu
'''

class storyCounter:
    
    def __init__(self):
        self.totalGoodLen = 0
        self.totalBadLen = 0
        self.maxGoodLen = 0
        self.maxBadLen = 0
        self.averageGoodLen = 0
        self.averageBadLen = 0
        
        self.goodStoryCount = 0
        self.badStoryCount = 0
        self.bothStoryCount = 0
        self.noneStoryCount = 0
        
    
    def countStory(self, story_):
        if(story_.hasGood and story_.hasBad):
            self.bothStoryCount = self.bothStoryCount + 1
                    
            self.totalGoodLen = self.totalGoodLen + story_.GetGoodLen()
            if(story_.GetGoodLen() > self.maxGoodLen):
                self.maxGoodLen = story_.GetGoodLen()
                    
            self.totalBadLen = self.totalBadLen + story_.GetBadLen()
            if(story_.GetBadLen() > self.maxBadLen):
                self.maxBadLen = story_.GetBadLen()
                        
        elif(story_.hasGood):
            self.goodStoryCount = self.goodStoryCount + 1
                    
            self.totalGoodLen = self.totalGoodLen + story_.GetGoodLen()
            if(story_.GetGoodLen() > self.maxGoodLen):
                self.maxGoodLen = story_.GetGoodLen()
                        
        elif(story_.hasBad):
            self.badStoryCount = self.badStoryCount + 1
                    
            self.totalBadLen = self.totalBadLen + story_.GetBadLen()
            if(story_.GetBadLen() > self.maxBadLen):
                self.maxBadLen = story_.GetBadLen()
        else:
            self.noneStoryCount = self.noneStoryCount + 1
            
    
    def CalculateAverage(self):
        self.averageGoodLen = self.totalBadLen / (self.goodStoryCount + self.bothStoryCount)
        self.averageBadLen = self.totalBadLen / (self.badStoryCount + self.bothStoryCount)