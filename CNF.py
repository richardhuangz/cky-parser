#!/usr/bin/python3

import sys
import os
import math

def normalize(file):

    result = []
    terms = {}
    toosie = {}

    with open(file, "r") as fp:
        
        for line in fp:
            line = line.strip()
            parts = line.split()
            lenparts = len(parts)            

            # non non or non terminal
            if lenparts == 2:
                # non non bad
                if parts[1][0].isupper():
                    toosie[parts[1]] = parts[0]
                # non terminal good
                else:
                    result.append(parts)
                    if parts[0] not in terms:
                        terms[parts[0]] = [parts[1]]
                    else:
                        terms[parts[0]].append(parts[1])
            # non non non good
            elif lenparts == 3:
                result.append(parts)
            # non non non non bad
            elif lenparts == 4:
                combined = [parts[1] + parts[2], parts[1], parts[2]]
                if combined not in result:
                    result.append(combined)
                update = [parts[0], parts[1] + parts[2], parts[3]]
                if update not in result:
                    result.append(update)
    
    with open(file, "r") as fp:
        for line in fp:
            line = line.strip()
            parts = line.split()
            lenparts = len(parts)

            if parts[0] in toosie:
                update = [toosie[parts[0]]]
                for i in range(1, lenparts):
                    update.append(parts[i])
                if len(update) == 3:
                    result.append(update)
                elif lenparts == 2 and parts[1][0].islower():
                    result.append([toosie[parts[0]], parts[1]])
                elif lenparts == 2:
                    toosie[update[0]] = update[1]            

    for toos in toosie:
        trans = toosie[toos]
        for term in terms:
            if trans == term:
                for word in terms[term]:
                    result.append([toos, word])

    return result

# no need to use this
def sortish(grammar):
    ntnt = []
    ntt = []
    result = []
    for line in grammar:
        trueline = ""
        for word in line:
            trueline += word + " "
        trueline = trueline.strip()
        if len(line) == 2:
            ntt.append(trueline)
        else:
            ntnt.append(trueline)
    ntnt.sort()
    ntt.sort()

    for line in ntnt:
        result.append(line)
    for line in ntt:
        result.append(line)

    return result


# (B)
l = normalize("3-1b.txt")
for line in l:
    printthis = ""
    for word in line:
        printthis += word + " "
    print(printthis.strip())
