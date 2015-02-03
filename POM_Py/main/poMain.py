'''
Created on 3 Feb 2015

@author: Bin.Lu
'''

import storyManager

sManager = storyManager.storyManager()
sManager.LoadStory()
sManager.DumpBagsToCSV()
sManager.TestDumpStory()

if __name__ == '__main__':
    pass