'''
Created on 25 Nov 2014

@author: Bin.Lu
'''
import csv
import operator

class Story:
    def __init__(self, ID):
        self.ID = ID
    
    def SetStoryBody(self, storyBody):
        self.storyBody = storyBody
    
    def SetStoryTitle(self, storyTitle):
        self.storyTitle = storyTitle

    def SetGood(self, good):
        self.good = good.replace('nothing', '')
        self.good = self.good.replace(';;', ';')
        self.hasGood = len(self.good) > 0
        
    def SetBad(self, bad):
        self.bad = bad.replace('nothing', '')
        self.bad = self.bad.replace(';;', ';')
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
    
class storyManager:

    storyCollection = []
    
    totalGoodLen = 0
    totalBadLen = 0
    maxGoodLen = 0
    maxBadLen = 0
    averageGoodLen = 0
    averageBadLen = 0
    
    goodStoryCount = 0
    badStoryCount = 0
    bothStoryCount = 0
    noneStoryCount = 0
    
    bagGood = {}
    bagBad = {}
    bagAll = {}
    
    def LoadStory(self):
        print 'Loading Stories...'
        f = open('auStory.txt', 'r')
        while True:
            line = f.readline()
            if(len(line) <= 0):
                break;
            if line.find('ID:') == 0: #Start of Story
                story = Story(line.lstrip('ID: ').strip()) 
                #Title
                line = f.readline()
                story.SetStoryTitle(line.lstrip('Title: ').strip())
                #Time
                line = f.readline()
                #Location
                line = f.readline()
                #Author
                line = f.readline()
                #Story
                line = f.readline()
                story.SetStoryBody(line.lstrip('Story: ').strip())
                #Relate
                line = f.readline()
                #Good
                line = f.readline()
                story.SetGood(line.lstrip('Good: ').strip().rstrip(';'))
                #Bad
                line = f.readline()
                story.SetBad(line.lstrip('Bad: ').strip().rstrip(';'))
                self.storyCollection.append(story)
                self.CalculateStoryCount(story)
                self.FillInWordBags(story)
                
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
    
    def CalculateStoryCount(self, story):

        if(story.hasGood and story.hasBad):
            self.bothStoryCount = self.bothStoryCount + 1
                    
            self.totalGoodLen = self.totalGoodLen + story.GetGoodLen()
            if(story.GetGoodLen() > self.maxGoodLen):
                self.maxGoodLen = story.GetGoodLen()
                    
            self.totalBadLen = self.totalBadLen + story.GetBadLen()
            if(story.GetBadLen() > self.maxBadLen):
                self.maxBadLen = story.GetBadLen()
                        
        elif(story.hasGood):
            self.goodStoryCount = self.goodStoryCount + 1
                    
            self.totalGoodLen = self.totalGoodLen + story.GetGoodLen()
            if(story.GetGoodLen() > self.maxGoodLen):
                self.maxGoodLen = story.GetGoodLen()
                        
        elif(story.hasBad):
            self.badStoryCount = self.badStoryCount + 1
                    
            self.totalBadLen = self.totalBadLen + story.GetBadLen()
            if(story.GetBadLen() > self.maxBadLen):
                self.maxBadLen = story.GetBadLen()
        else:
            self.noneStoryCount = self.noneStoryCount + 1
    
    def DumpBagsToCSV(self):
        fieldnames = ['keywords', 'count']
        
        sortedBag = sorted(self.bagAll.items(), key = operator.itemgetter(1))
        with open('user_comment.csv', 'wb') as csvfile:
            writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
            writer.writeheader()
            for v in reversed(sortedBag):
                writer.writerow({'keywords': v[0], 'count':v[1]})
    
        sortedGood = sorted(self.bagGood.items(), key = operator.itemgetter(1))
        with open('good.csv', 'wb') as csvGood:
            writer = csv.DictWriter(csvGood, fieldnames=fieldnames)
            writer.writeheader()
            for v in reversed(sortedGood):
                writer.writerow({'keywords': v[0], 'count':v[1]})
                
        sortedBad = sorted(self.bagBad.items(), key = operator.itemgetter(1))
        with open('bad.csv', 'wb') as csvBad:
            writer = csv.DictWriter(csvBad, fieldnames=fieldnames)
            writer.writeheader()
            for v in reversed(sortedBad):
                writer.writerow({'keywords': v[0], 'count':v[1]})
                
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
        
#         print '\n\n********************************\n'
#         for k, v in self.bagGood.iteritems():
#             print k, v

