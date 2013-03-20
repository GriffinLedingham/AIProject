'''
Created on Mar 12, 2013

@author: stryder
'''
from DPLL import DPLL

class SudokuClass:
    '''
    classdocs
    '''
    puzzleMat = []
    numInitialNonBlank = 0
    cnf = []


    def __init__(self):
        '''
        Constructor
        '''
        pass
    
    def run(self, puzzleFilename, cnfFormula, outputFilename):        
        self.loadPuzzleMatrix(puzzleFilename)

        self.loadCNFFormula(cnfFormula)
        
        self.encodeMinimal(outputFilename)
        self.encodeExtended(outputFilename)
        
        dpll = DPLL()
        dpll.runDPLL(clauseList, {}, literalList)
        
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
    
                
    def loadCNFFormula(self, cnfFormula):
        myFile = open(cnfFormula, 'r')
        
        for line in myFile:
            list = line.split(' ')
            list = map(int, list)
            self.cnf.append(list[:-1])                    

    
    def encodeMinimal(self, outputFileName):
        myFile = open('minimal' + outputFileName, 'w')
        clauses = 8829 + self.numInitialNonBlank
        myFile.write('p cnf 729 ' + str(clauses) + '\n')
        
        # there is at least one number in each entry
        for x in range(1,10):
            for y in range(1,10):
                for z in range(1,10):
                    myFile.write(str(x*100 + y*10 + z) + ' ')
                myFile.write('\n')
        
        # each number appears at most once in each row
        for y in range(1,10):
            for z in range(1,10):
                for x in range(1,9):
                    for i in range(x+1,10):
                        myFile.write(str((-1)*(x*100 + y*10 + z)) + " " + str((-1)*(i*100 + y*10 + z)) + '\n')
                            
        # each number appears at most once in each column
        for x in range(1,10):
            for z in range(1,10):
                for y in range(1,9):
                    for i in range(y+1,10):
                        myFile.write(str((-1)*(x*100 + y*10 + z)) + " " + str((-1)*(x*100 + i*10 + z)) + '\n')
        
        # each number appears at most once in each 3x3 sub-grid
        for z in range(1,10):
            for i in range(0,3):
                for j in range(0,3):
                    for x in range(1,4):
                        for y in range(1,4):
                            for k in range(y+1,4):
                                myFile.write(str((-1)*((3*i+x)*100 + (3*j+y)*10 + z)) + " " + str((-1)*((3*i+x)*100 + (3*j+k)*10 + z)) + '\n')
                            for k in range(x+1,4):
                                for l in range(1,4):
                                    myFile.write(str((-1)*((3*i+x)*100 + (3*j+y)*10 + z)) + " " + str((-1)*((3*i+k)*100 + (3*j+l)*10 + z)) + '\n')
                                    
        # now our unit clauses from the initial sudoku puzzle
        for x in range(0,9):
            for y in range(0,9):
                if self.puzzleMat[x][y] != 0:
                    myFile.write(str((x+1)*100 + (y+1)*10 + self.puzzleMat[x][y]) + '\n')
        
        
    def encodeExtended(self, outputFileName):
        myFile = open('extended' + outputFileName, 'w')
        clauses = 11988 + self.numInitialNonBlank
        myFile.write('p cnf 729 ' + str(clauses) + '\n')
        
        # there is at least one number in each entry
        for x in range(1,10):
            for y in range(1,10):
                for z in range(1,10):
                    myFile.write(str(x*100 + y*10 + z) + ' ')
                myFile.write('\n')
        
        # each number appears at most once in each row
        for y in range(1,10):
            for z in range(1,10):
                for x in range(1,9):
                    for i in range(x+1,10):
                        myFile.write(str((-1)*(x*100 + y*10 + z)) + " " + str((-1)*(i*100 + y*10 + z)) + '\n')
                            
        # each number appears at most once in each column
        for x in range(1,10):
            for z in range(1,10):
                for y in range(1,9):
                    for i in range(y+1,10):
                        myFile.write(str((-1)*(x*100 + y*10 + z)) + " " + str((-1)*(x*100 + i*10 + z)) + '\n')
        
        # each number appears at most once in each 3x3 sub-grid
        for z in range(1,10):
            for i in range(0,3):
                for j in range(0,3):
                    for x in range(1,4):
                        for y in range(1,4):
                            for k in range(y+1,4):
                                myFile.write(str((-1)*((3*i+x)*100 + (3*j+y)*10 + z)) + " " + str((-1)*((3*i+x)*100 + (3*j+k)*10 + z)) + '\n')
                            for k in range(x+1,4):
                                for l in range(1,4):
                                    myFile.write(str((-1)*((3*i+x)*100 + (3*j+y)*10 + z)) + " " + str((-1)*((3*i+k)*100 + (3*j+l)*10 + z)) + '\n')
                                    
        # now our unit clauses from the initial sudoku puzzle
        for x in range(0,9):
            for y in range(0,9):
                if self.puzzleMat[x][y] != 0:
                    myFile.write(str((x+1)*100 + (y+1)*10 + self.puzzleMat[x][y]) + '\n')
        
        
        # there is at most one number in each entry
        for x in range(1,10):
            for y in range(1,10):
                for z in range(1,9):
                    for i in range(z+1,10):
                        myFile.write(str((-1)*(x*100 + y*10 + z)) + ' ' + str((-1)*(x*100 + y*10 + i)) + '\n')
                        
        # each number appears at least once in each row
        for y in range (1,10):
            for z in range(1,10):
                for x in range(1,10):
                    myFile.write(str(x*100 + y*10 + z) + ' ')
                myFile.write('\n')
                
        # each number appears at least once in each column
        for x in range(1,10):
            for z in range(1,10):
                for y in range(1,10):
                    myFile.write(str(x*100 + y*10 + z) + ' ')
                myFile.write('\n')
                
        # each number appears at least once in each 3x3 sub-grid
        for z in range(1,10):
            for i in range(0,3):
                for j in range(0,3):
                    for x in range(1,4):
                        for y in range(1,4):
                                myFile.write(str((3*i+x)*100 + (3*j+y)*10 + z) + ' ')
            myFile.write('\n')
                    
        