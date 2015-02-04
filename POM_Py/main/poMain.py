'''
Created on 3 Feb 2015

@author: Bin.Lu
'''

import storyManager

print 'Start....'
sManager = storyManager.storyManager()
sManager.LoadStory()
sManager.DumpBagsToCSV()
sManager.SaveDfData()
#sManager.TestDumpStory()
print 'End....'
if __name__ == '__main__':
    pass