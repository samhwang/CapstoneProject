'''
Created on 5 Feb 2015

@author: Bin.Lu
'''
import csvheader
import csv
import operator

class WordBag:
    
    def __init__(self):
        self.bagGood = {}
        self.bagBad = {}
        self.bagAll = {}
        self.bagKey = {}
        
        
    def fillInBags(self, story_):
        if(story_.hasGood):
            for good in story_.GetGoodAsVec():
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
                        
        if(story_.hasBad):
            for bad in story_.GetBadAsVec():
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
    
    def SaveToFile(self, keypath = 'userkeys.csv', commentpath = 'user_comment.csv', goodpath = 'good.csv', badpath = 'bad.csv'):
        print 'Save bag csv....'
        with open(keypath, 'wb') as csvfile:
            writer = csv.DictWriter(csvfile, fieldnames = csvheader.wordbag_field)
            writer.writeheader()
            for k, v in self.bagKey.items():
                writer.writerow({'keywords':k, 'count':v})
                
        sortedBag = sorted(self.bagAll.items(), key = operator.itemgetter(1), reverse = True)
        with open(commentpath, 'wb') as csvfile:
            writer = csv.DictWriter(csvfile, fieldnames = csvheader.wordbag_field)
            writer.writeheader()
            for v in sortedBag:
                writer.writerow({'keywords': v[0], 'count':v[1]})
    
        sortedGood = sorted(self.bagGood.items(), key = operator.itemgetter(1), reverse = True)
        with open(goodpath, 'wb') as csvGood:
            writer = csv.DictWriter(csvGood, fieldnames = csvheader.wordbag_field)
            writer.writeheader()
            for v in  sortedGood:
                writer.writerow({'keywords': v[0], 'count':v[1]})
                
        sortedBad = sorted(self.bagBad.items(), key = operator.itemgetter(1), reverse = True)
        with open(badpath, 'wb') as csvBad:
            writer = csv.DictWriter(csvBad, fieldnames = csvheader.wordbag_field)
            writer.writeheader()
            for v in sortedBad:
                writer.writerow({'keywords': v[0], 'count':v[1]})
    
    def LoadFromFile(self, path):
        pass
    
if __name__ == '__main__':
    pass