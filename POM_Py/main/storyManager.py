'''
Created on 25 Nov 2014

@author: Bin.Lu
'''
import story
import docFreqCounter
import storyCounter
import wordBag

class storyManager:

    def __init__(self):
        self.dfCounter = docFreqCounter.docFreqCounter()
        self.storyCollection = []
        self.storycounter = storyCounter.storyCounter()
        self.wordbag = wordBag.WordBag()
        
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
                self.storycounter.countStory(story_)
                self.wordbag.fillInBags(story_)
                self.dfCounter.addDocFreq(story_)
        self.storycounter.CalculateAverage()
        f.closed
    
    def DumpBagsToCSV(self):
        self.wordbag.SaveToFile()

    
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
        print 'Both: %d, Good: %d, Bad: %d, None: %d' % (self.storycounter.bothStoryCount, self.storycounter.goodStoryCount, self.storycounter.badStoryCount, self.storycounter.noneStoryCount)
        print 'Max Good: %d, Max Bad: %d' % (self.storycounter.maxGoodLen, self.storycounter.maxBadLen)
        print 'Average Good: %d, Average Bad: %d' % (self.storycounter.averageGoodLen, self.storycounter.averageBadLen)
        print 'All: %d, Good: %d, Bad: %d' % (len(self.wordbag.bagAll), len(self.wordbag.bagGood), len(self.wordbag.bagBad))
        
        
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