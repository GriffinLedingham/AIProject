'''
Created on Mar 12, 2013

@author: stryder
'''
from DPLL import DPLL
from time import time, clock

class SudokuClass:
    '''
    classdocs
    '''
    puzzleMat = []
    numInitialNonBlank = 0


    def __init__(self):
        '''
        Constructor
        '''
        self.puzzleMat = []
        self.numInitialNonBlank = 0
    
    def run(self, puzzleFilename, outputFilename):        
        dpll = DPLL()
        print ""
        print "/"*10 + "NEW SUDOKU PUZZLE - USING DPLL" + "/"*10
        print "Unsolved Puzzle:"
        self.loadPuzzleMatrix(puzzleFilename)
        self.printPuzzle()
        
        literals = self.loadLiteralList()
        clauseList = self.encodeMinimal(outputFilename)
        print ""
        print "-"*40
        print "Solved Puzzle Using MINIMAL Encoding:"
        t0 = clock()        
        result = dpll.runDPLL(clauseList, {}, list(literals))
        print "Elapsed Time:" +  str(clock() - t0)
        if result == False:
            print "Didn't find encoding using minimal"
        else:
            print "Found encoding using minimal"
        
        
        clauseList = self.encodeExtended(outputFilename)
        print ""
        print "-"*40
        print "Solved Puzzle Using EXTENDED Encoding:"
        t0 = clock()
        result = dpll.runDPLL(clauseList, {}, list(literals))
        print "Elapsed Time:" +  str(clock() - t0)
        if result == False:
            print "Didn't find encoding using extended"
            print "No Solution found...."
        else:
            print "Found encoding using extended"
        
    def loadPuzzleMatrix(self, puzzleFilename):
        myFile = open(puzzleFilename, 'r')
        
        for line in myFile:
            string = line.replace('.', '0')
            string = string.replace('*', '0')
            string = string.replace('?', '0')
            string = string.replace('\n', '')
            list = []
            length = len(string)
            if length == 9:
                self.numInitialNonBlank += 9 - string.count('0')
                for letter in string:
                    list.append(int(letter))
                self.puzzleMat.append(list)
            else:
                self.numInitialNonBlank = 81 - string.count('0')
                for x in range(0,9):
                    list = []
                    for y in range(0,9):
                        list.append(int(string[9*x + y]))
                    self.puzzleMat.append(list)                  

    def printPuzzle(self):
        for i, row in enumerate(self.puzzleMat):
            string = ""
            for k, element in enumerate(row):
                if element == 0:
                    string += "  "
                else:
                    string += (str(element) + " ")
                if (k+1)%3 == 0 and (k+1) != 9:
                    string += "|"
            print string
            if (i+1)%3 == 0 and (i+1) != 9:
                print '-'*20
    
    def loadLiteralList(self):
        literals = []
        # there is at least one number in each entry
        for x in range(1,10):
            for y in range(1,10):
                for z in range(1,10):
                    literals.append(x*100 + y*10 + z)
        return literals
    def encodeMinimal(self, outputFileName):
        clauseList = []
        myFile = open('minimal' + outputFileName, 'w')
        clauses = 8829 + self.numInitialNonBlank
        myFile.write('p cnf 729 ' + str(clauses) + '\n')
        
        # there is at least one number in each entry
        for x in range(1,10):
            for y in range(1,10):
                clause = []
                for z in range(1,10):
                    myFile.write(str(x*100 + y*10 + z) + ' ')
                    clause.append(x*100 + y*10 + z)
                myFile.write('\n')
                clauseList.append(clause)
        
        # each number appears at most once in each row
        for y in range(1,10):
            for z in range(1,10):
                for x in range(1,9):
                    for i in range(x+1,10):
                        myFile.write(str((-1)*(x*100 + y*10 + z)) + " " + str((-1)*(i*100 + y*10 + z)) + '\n')
                        clause = []
                        clause.append((-1)*(x*100 + y*10 + z))
                        clause.append((-1)*(i*100 + y*10 + z))
                        clauseList.append(clause)
                            
        # each number appears at most once in each column
        for x in range(1,10):
            for z in range(1,10):
                for y in range(1,9):
                    for i in range(y+1,10):
                        myFile.write(str((-1)*(x*100 + y*10 + z)) + " " + str((-1)*(x*100 + i*10 + z)) + '\n')
                        clause = []
                        clause.append((-1)*(x*100 + y*10 + z))
                        clause.append((-1)*(x*100 + i*10 + z))
                        clauseList.append(clause)
        
        # each number appears at most once in each 3x3 sub-grid
        for z in range(1,10):
            for i in range(0,3):
                for j in range(0,3):
                    for x in range(1,4):
                        for y in range(1,4):
                            for k in range(y+1,4):
                                myFile.write(str((-1)*((3*i+x)*100 + (3*j+y)*10 + z)) + " " + str((-1)*((3*i+x)*100 + (3*j+k)*10 + z)) + '\n')
                                clause = []
                                clause.append((-1)*((3*i+x)*100 + (3*j+y)*10 + z))
                                clause.append((-1)*((3*i+x)*100 + (3*j+k)*10 + z))
                                clauseList.append(clause)
                            for k in range(x+1,4):
                                for l in range(1,4):
                                    myFile.write(str((-1)*((3*i+x)*100 + (3*j+y)*10 + z)) + " " + str((-1)*((3*i+k)*100 + (3*j+l)*10 + z)) + '\n')
                                    clause = []
                                    clause.append((-1)*((3*i+x)*100 + (3*j+y)*10 + z))
                                    clause.append((-1)*((3*i+k)*100 + (3*j+l)*10 + z))
                                    clauseList.append(clause)
                                    
        # now our unit clauses from the initial sudoku puzzle
        for x in range(0,9):
            for y in range(0,9):
                if self.puzzleMat[x][y] != 0:
                    myFile.write(str((x+1)*100 + (y+1)*10 + self.puzzleMat[x][y]) + '\n')
                    clause = []
                    clause.append((x+1)*100 + (y+1)*10 + self.puzzleMat[x][y])
                    clauseList.append(clause)
        return clauseList
        
    def encodeExtended(self, outputFileName):
        myFile = open('extended' + outputFileName, 'w')
        clauses = 11988 + self.numInitialNonBlank
        myFile.write('p cnf 729 ' + str(clauses) + '\n')
        clauseList = []
        
        # there is at least one number in each entry
        for x in range(1,10):
            for y in range(1,10):
                clause = []
                for z in range(1,10):
                    myFile.write(str(x*100 + y*10 + z) + ' ')
                    clause.append(x*100 + y*10 + z)
                myFile.write('\n')
                clauseList.append(clause)
        
        # each number appears at most once in each row
        for y in range(1,10):
            for z in range(1,10):
                for x in range(1,9):
                    for i in range(x+1,10):
                        myFile.write(str((-1)*(x*100 + y*10 + z)) + " " + str((-1)*(i*100 + y*10 + z)) + '\n')
                        clause = []
                        clause.append((-1)*(x*100 + y*10 + z))
                        clause.append((-1)*(i*100 + y*10 + z))
                        clauseList.append(clause)
                            
        # each number appears at most once in each column
        for x in range(1,10):
            for z in range(1,10):
                for y in range(1,9):
                    for i in range(y+1,10):
                        myFile.write(str((-1)*(x*100 + y*10 + z)) + " " + str((-1)*(x*100 + i*10 + z)) + '\n')
                        clause = []
                        clause.append((-1)*(x*100 + y*10 + z))
                        clause.append((-1)*(x*100 + i*10 + z))
                        clauseList.append(clause)
        
        # each number appears at most once in each 3x3 sub-grid
        for z in range(1,10):
            for i in range(0,3):
                for j in range(0,3):
                    for x in range(1,4):
                        for y in range(1,4):
                            for k in range(y+1,4):
                                myFile.write(str((-1)*((3*i+x)*100 + (3*j+y)*10 + z)) + " " + str((-1)*((3*i+x)*100 + (3*j+k)*10 + z)) + '\n')
                                clause = []
                                clause.append((-1)*((3*i+x)*100 + (3*j+y)*10 + z))
                                clause.append((-1)*((3*i+x)*100 + (3*j+k)*10 + z))
                                clauseList.append(clause)
                            for k in range(x+1,4):
                                for l in range(1,4):
                                    myFile.write(str((-1)*((3*i+x)*100 + (3*j+y)*10 + z)) + " " + str((-1)*((3*i+k)*100 + (3*j+l)*10 + z)) + '\n')
                                    clause = []
                                    clause.append((-1)*((3*i+x)*100 + (3*j+y)*10 + z))
                                    clause.append((-1)*((3*i+k)*100 + (3*j+l)*10 + z))
                                    clauseList.append(clause)
                                    
        # now our unit clauses from the initial sudoku puzzle
        for x in range(0,9):
            for y in range(0,9):
                if self.puzzleMat[x][y] != 0:
                    myFile.write(str((x+1)*100 + (y+1)*10 + self.puzzleMat[x][y]) + '\n')
                    clause = []
                    clause.append((x+1)*100 + (y+1)*10 + self.puzzleMat[x][y])
                    clauseList.append(clause)
        
        
        # there is at most one number in each entry
        for x in range(1,10):
            for y in range(1,10):
                for z in range(1,9):
                    for i in range(z+1,10):
                        myFile.write(str((-1)*(x*100 + y*10 + z)) + ' ' + str((-1)*(x*100 + y*10 + i)) + '\n')
                        clause = []
                        clause.append((-1)*(x*100 + y*10 + z))
                        clause.append((-1)*(x*100 + y*10 + i))
                        clauseList.append(clause)
                        
        # each number appears at least once in each row
        for y in range (1,10):
            for z in range(1,10):
                clause = []
                for x in range(1,10):
                    myFile.write(str(x*100 + y*10 + z) + ' ')
                    clause.append(x*100 + y*10 + z)
                myFile.write('\n')
                clauseList.append(clause)
                
        # each number appears at least once in each column
        for x in range(1,10):
            for z in range(1,10):
                clause = []
                for y in range(1,10):
                    myFile.write(str(x*100 + y*10 + z) + ' ')
                    clause.append(x*100 + y*10 + z)
                myFile.write('\n')
                clauseList.append(clause)
                
        # each number appears at least once in each 3x3 sub-grid
        for z in range(1,10):
            clause = []
            for i in range(0,3):
                for j in range(0,3):
                    for x in range(1,4):
                        for y in range(1,4):
                                myFile.write(str((3*i+x)*100 + (3*j+y)*10 + z) + ' ')
                                clause.append((3*i+x)*100 + (3*j+y)*10 + z)
            myFile.write('\n')
            clauseList.append(clause)
                    
        return clauseList