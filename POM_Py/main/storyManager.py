'''
Created on 25 Nov 2014

@author: Bin.Lu
'''
import csv
import operator
import story
import docFreqCounter
import csvheader

class storyManager:


    
    def __init__(self):
        self.dfCounter = docFreqCounter.docFreqCounter()
        self.storyCollection = []
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
        
        self.bagGood = {}
        self.bagBad = {}
        self.bagAll = {}
        self.bagKey = {}
        
    def LoadStory(self):
        print 'Loading Stories...'
        f = open('auStory.txt', 'r')
        while True:
            line = f.readline()
            if(len(line) <= 0):
                break;
            if line.find('ID:') == 0: #Start of Story
                story_ = story.Story(line.lstrip('ID: ').strip()) 
                #Title
                line = f.readline()
                story_.SetStoryTitle(line.lstrip('Title: ').strip())
                #Time
                line = f.readline()
                #Location
                line = f.readline()
                #Author
                line = f.readline()
                #Story
                line = f.readline()
                story_.SetStoryBody(line.lstrip('Story: ').strip())
                #Relate
                line = f.readline()
                #Good
                line = f.readline()
                story_.SetGood(line.lstrip('Good: ').strip().rstrip(';'))
                #Bad
                line = f.readline()
                story_.SetBad(line.lstrip('Bad: ').strip().rstrip(';'))
                self.storyCollection.append(story_)
                self.CalculateStoryCount(story_)
                self.FillInWordBags(story_)
                self.dfCounter.addDocFreq(story_)
        self.averageGoodLen = self.totalBadLen / (self.goodStoryCount + self.bothStoryCount)
        self.averageBadLen = self.totalBadLen / (self.badStoryCount + self.bothStoryCount)
        f.closed
    
    def FillInWordBags(self, story):
        if(story.hasGood):
            for good in story.GetGoodAsVec():
                if good in self.bagGood:
                    self.bagGood[good] = self.bagGood[good] + 1
                else:
                    self.bagGood[good] = 1
                
                if good in self.bagAll:
                    self.bagAll[good] = self.bagAll[good] + 1
                else:
                    self.bagAll[good] = 1
        
                for key in good.strip().split(' '):
                    if key in self.bagKey:
                        self.bagKey[key] = self.bagKey[key] + 1
                    else:
                        self.bagKey[key] = 1
                        
        if(story.hasBad):
            for bad in story.GetBadAsVec():
                if bad in self.bagBad:
                    self.bagBad[bad] = self.bagBad[bad]  + 1
                else:
                    self.bagBad[bad] = 1
                
                if bad in self.bagAll:
                    self.bagAll[bad] = self.bagAll[bad] + 1
                else:
                    self.bagAll[bad]  = 1
    
                for key in bad.strip().split(' '):
                    if key in self.bagKey:
                        self.bagKey[key] = self.bagKey[key] + 1
                    else:
                        self.bagKey[key] = 1
                        
    def CalculateStoryCount(self, story_):

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
    
    def DumpBagsToCSV(self):
        
        print 'Save bag csv....'
        with open('userkeys.csv', 'wb') as csvfile:
            writer = csv.DictWriter(csvfile, fieldnames = csvheader.wordbag_field)
            writer.writeheader()
            for k, v in self.bagKey.items():
                writer.writerow({'keywords':k, 'count':v})
                
        sortedBag = sorted(self.bagAll.items(), key = operator.itemgetter(1))
        with open('user_comment.csv', 'wb') as csvfile:
            writer = csv.DictWriter(csvfile, fieldnames = csvheader.wordbag_field)
            writer.writeheader()
            for v in reversed(sortedBag):
                writer.writerow({'keywords': v[0], 'count':v[1]})
    
        sortedGood = sorted(self.bagGood.items(), key = operator.itemgetter(1))
        with open('good.csv', 'wb') as csvGood:
            writer = csv.DictWriter(csvGood, fieldnames = csvheader.wordbag_field)
            writer.writeheader()
            for v in reversed(sortedGood):
                writer.writerow({'keywords': v[0], 'count':v[1]})
                
        sortedBad = sorted(self.bagBad.items(), key = operator.itemgetter(1))
        with open('bad.csv', 'wb') as csvBad:
            writer = csv.DictWriter(csvBad, fieldnames = csvheader.wordbag_field)
            writer.writeheader()
            for v in reversed(sortedBad):
                writer.writerow({'keywords': v[0], 'count':v[1]})
    
    def SaveDfData(self):
        self.dfCounter.SaveDocFreqData('df.csv')
        
    def DumpStories(self):
        print 'Dumping Stories...'
        print 'Story Count: %d' % (len(self.storyCollection))
        for story in self.storyCollection:
            print '%s: %s' % (story.ID, story.storyTitle)


    def TestDumpStory(self):
        testIdx = 0
        print 'Test Dumping Stories...'
        print 'Story Count: %d' % (len(self.storyCollection))
        print 'Both: %d, Good: %d, Bad: %d, None: %d' % (self.bothStoryCount, self.goodStoryCount, self.badStoryCount, self.noneStoryCount)
        print 'Max Good: %d, Max Bad: %d' % (self.maxGoodLen, self.maxBadLen)
        print 'Average Good: %d, Average Bad: %d' % (self.averageGoodLen, self.averageBadLen)
        print 'All: %d, Good: %d, Bad: %d' % (len(self.bagAll), len(self.bagGood), len(self.bagBad))
        
        
        print '\n\n********************************\n'
        print '%s: %s' % (self.storyCollection[testIdx].ID, self.storyCollection[0].storyTitle)
        print self.storyCollection[testIdx].storyBody
        print self.storyCollection[testIdx].good
        print self.storyCollection[testIdx].bad
        print self.storyCollection[testIdx].GetGoodAsVec()
        print self.storyCollection[testIdx].GetBadAsVec()
        print self.storyCollection[testIdx].GetGoodLen()
        print self.storyCollection[testIdx].GetBadLen()
        print self.storyCollection[testIdx].hasGood
        print self.storyCollection[testIdx].hasBad
        
        print self.dfCounter.getDocFreq('feedback')
        print self.dfCounter.getDocList('feedback')
#         print '\n\n********************************\n'
#         for k, v in self.bagGood.iteritems():
#             print k, v

if __name__ == '__main__':
    sm = storyManager()
    sm.LoadStory()
    sm.TestDumpStory()