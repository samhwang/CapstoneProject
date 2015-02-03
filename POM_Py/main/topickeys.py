'''
Created on 3 Feb 2015

@author: Bin.Lu
'''

class topickeys:
    
    terms = []
    def loadKeys(self, path):
        f = open(path, 'r')
        while True:
            line = f.readline()
            if(len(line) <= 0):
                break;
            
            self.terms.append(line.split('\t')[2].strip())
            


    def dumpKeys(self):
        for v in self.terms:
            print v
    
if __name__ == '__main__':
    topickeys = topickeys()
    topickeys.loadKeys('all_keys100.txt')
    topickeys.dumpKeys()