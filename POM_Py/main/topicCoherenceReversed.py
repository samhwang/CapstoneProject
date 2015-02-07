'''
Created on 7 Feb 2015

@author: BLu
'''

import docFreqCounter
import topickeys
import math

dfCounter = docFreqCounter.docFreqCounter()
dfCounter.loadDocFreqData('sf.csv')

tk = topickeys.topickeys()
tk.loadKeys("all_keys100_new.txt")
tk.sortkeys()
tk.trimkeys(10)


scoreList = []

for key in tk.terms:
    pass
    score = float(0)
    
    for t1 in key[:-1]:
        for t2 in key[1:]:
            score = score + math.log10(float((dfCounter.getDocFreq(t1) + 2))/(dfCounter.getCoDocFreq(t1, t2)+1))
    
    scoreList.append(score)
    

for s in scoreList:
    print s