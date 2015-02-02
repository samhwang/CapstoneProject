'''
Created on 25 Nov 2014

@author: Bin.Lu
'''
class Story:
    def __init__(self, ID):
        self.ID = ID
    
    def SetStoryBody(self, storyBody):
        self.storyBody = storyBody
    
    def SetStoryTitle(self, storyTitle):
        self.storyTitle = storyTitle



storyCollection = []

def LoadStory():
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
            #Bad
            line = f.readline()
            storyCollection.append(story)
            
    f.closed


def DumpStories():
    print 'Dumping Stories...'
    print 'Story Count: %d' % (len(storyCollection))
    for story in storyCollection:
        print '%s: %s' % (story.ID, story.storyTitle)


if __name__== '__main__':
    LoadStory()
    DumpStories()
