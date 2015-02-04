'''
Created on 4 Feb 2015

@author: Bin.Lu
'''
import re

def getTermsList(line):
    return set(cleanLine(line).split(' '))

def cleanLine(line):
    return re.sub(r'\W+',' ', line).strip()

if __name__ == '__main__':
    print getTermsList('after years of physiotherapists, osteopaths, acupuncture, massage therapists & nurafun, i asked my doctor to refer me to a muscular skeletal specialist. i thought there must be an answer to the ongoing pain & discomfort & hoped a specialist practitioner could help. the treatment involved more anti inflammatory medication & anesthetic injections into the local area of pain. i thought i would be in for a more specialist/proactive treatment & consequently did not return. i didn\'t feel inclined to pursue this situation with my gp in case he was offended at the feedback aimed at his colleague. so back to massage & physio & stretches & exercises thanks to the excellent help from my local physio who seems to listen well & ask the right questions & can always put his hand on the exact location of the pain or injury just by "listening" to me complain. i guess the moral of my story is i will always return to the listener as the first alternative rather than risk prescription of more drugs & then counter drugs for damage control.')