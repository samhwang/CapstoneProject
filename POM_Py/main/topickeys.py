'''
Created on 3 Feb 2015

@author: Bin.Lu
'''
import csv
import csvheader

class topickeys:
    
    def __init__(self):
        self.terms = []
        self.userkeys = {}
        
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

    def sortkeys(self):
        with open('userkeys.csv', 'rb') as usercomment:
            reader = csv.DictReader(usercomment)
            for row in reader:
                self.userkeys[row[csvheader.df_field[0]]] = row[csvheader.wordbag_field[1]]
         
            
    def dumpKeys(self):
        for v in self.terms:
            print v
        
        print '*****************************************'
        
        for k, v in self.userkeys.items():
            print k, v
    
if __name__ == '__main__':
    tk_ = topickeys()
    tk_.loadKeys('all_keys100.txt')
    tk_.trimkeys(10)
    tk_.sortkeys()
    tk_.dumpKeys()
    