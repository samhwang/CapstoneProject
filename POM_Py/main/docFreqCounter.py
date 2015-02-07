'''
Created on 4 Feb 2015

@author: Bin.Lu
'''

import storyparser
import csv
import csvheader
from collections import defaultdict

class docFreqCounter:
    
    def __init__(self):
        self.docList = defaultdict(list)
        self.summaryList = defaultdict(list)
        
    def addDocFreq(self, story):
        self.addSummaryFeq(story)
        terms_set = storyparser.getTermsList(story.storyBody)
        for t in terms_set:
            if t in self.docList:
                self.docList[t].append(story.ID)
            else:
                self.docList[t].append(story.ID)
 
    def addSummaryFeq(self, story):
        terms_set = storyparser.getTermsList(story.good)
        for t in terms_set:
            if len(t.strip()) > 0:
                self.summaryList[t].append(story.ID)
        
        terms_set = storyparser.getTermsList(story.bad)
        for t in terms_set:
            if len(t.strip()) > 0:
                self.summaryList[t].append(story.ID)
        
    def SaveDocFreqData(self, path, summaryPath):
        print 'Saving df csv...'
        with open(path, 'wb') as csvdf:
            writer = csv.DictWriter(csvdf, fieldnames = csvheader.df_field)
            writer.writeheader()
            for v in self.docList:
                writer.writerow({csvheader.df_field[0]: v, csvheader.df_field[1]: (" ").join(self.docList[v])})
                
        with open(summaryPath, 'wb') as csvsf:
            writer = csv.DictWriter(csvsf, fieldnames = csvheader.df_field)
            writer.writeheader()
            for v in self.summaryList:
                writer.writerow({csvheader.df_field[0]: v, csvheader.df_field[1]: (" ").join(self.summaryList[v])})
    
    def loadDocFreqData(self, path):
        print 'Loading df csv...'
        with open(path, 'rb') as csvdf:
            reader = csv.DictReader(csvdf)
            for row in reader:
                for t in row[csvheader.df_field[1]].split(' '):
                    self.docList[row[csvheader.df_field[0]]].append(t)
                
    def getDocList(self, term):
        return self.docList[term]
        
    def getDocFreq(self, term):
        if term in self.docList:
            return len(self.docList[term])
        else:
            return 0
    
    def getCoDocFreq(self, term1, term2):
        L1 = self.getDocList(term1)
        L2 = self.getDocList(term2)
        ret = []
        for v in L1:
            if v in L2:
                ret.append(v)
        
        return len(ret)
    
    
if __name__ == '__main__':
    dfCount = docFreqCounter();
    dfCount.loadDocFreqData('df.csv')
    print dfCount.getDocFreq('feedback')
    print dfCount.getDocList('feedback')
    print dfCount.getCoDocFreq('feedback', 'a')
    