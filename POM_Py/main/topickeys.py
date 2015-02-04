'''
Created on 3 Feb 2015

@author: Bin.Lu
'''

class topickeys:
    
    def __init__(self):
        self.terms = []
    
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
        pass
        
    def dumpKeys(self):
        for v in self.terms:
            print v
    
if __name__ == '__main__':
    tk_ = topickeys()
    tk_.loadKeys('all_keys100.txt')
    tk_.trimkeys(10)
    tk_.dumpKeys()