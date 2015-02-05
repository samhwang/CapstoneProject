'''
Created on 3 Feb 2015

@author: Bin.Lu
'''
import csv
import csvheader
import operator

class topickeys:
    
    def __init__(self):
        self.terms = []
        self.userkeys = {}
        self.maxFiltered = 0;
        self.minFiltered = 9999;
        self.averageFiltered = 0;
        
    def loadKeys(self, path):
        f = open(path, 'r')
        while True:
            line = f.readline()
            if(len(line) <= 0):
                break
            
            self.terms.append(line.split('\t')[2].strip().split(' '))
            
    def trimkeys(self, count):
        tempList = []
        for v in self.terms:
            tempList.append(v[0:count])
        self.terms = tempList

    def sortkeys(self, filtered = False):
        _totalFiltered = 0
        with open('userkeys.csv', 'rb') as usercomment:
            reader = csv.DictReader(usercomment)
            for row in reader:
                self.userkeys[row[csvheader.df_field[0]]] = row[csvheader.wordbag_field[1]]
        
        sortedKeys = []
        for v in self.terms:
            unsortedK = {}
            for t in v:
                if t in self.userkeys:
                    unsortedK[t] = int(self.userkeys[t])
                else:
                    if not filtered:
                        unsortedK[t] = 0
            sortedK = sorted(unsortedK.items(), key = operator.itemgetter(1), reverse = True)
            tmpList = []
            for k in sortedK:
                tmpList.append(k[0])
            sortedKeys.append(tmpList)
            if filtered:
                if len(tmpList) > self.maxFiltered:
                    self.maxFiltered = len(tmpList)
                if len(tmpList) < self.minFiltered:
                    self.minFiltered = len(tmpList)
                
                _totalFiltered = _totalFiltered + len(tmpList)
        
        self.averageFiltered = _totalFiltered/len(self.terms)
        self.terms = sortedKeys
            
    def dumpKeys(self, index):
        print self.terms[index]
        
    def dumpAllKeys(self):
        for v in self.terms:
            print v
        
        print '*****************************************'
        
    def dumpCount(self):
        print 'Filter Max:' , self.maxFiltered
        print 'Filter Min:' , self.minFiltered
        print 'Filter Average', self.averageFiltered
        
if __name__ == '__main__':
    tk_ = topickeys()
    tk_.loadKeys('all_keys100.txt')
    #tk_.trimkeys(10)
    tk_.dumpKeys(0)
    tk_.sortkeys(True)
    tk_.dumpKeys(0)
    tk_.dumpCount()
    