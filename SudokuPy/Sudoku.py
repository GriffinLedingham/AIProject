'''
Created on Mar 12, 2013

@author: stryder
'''

class SudokuClass:
    '''
    classdocs
    '''
    puzzleMat = []
    cnf = []


    def __init__(self):
        '''
        Constructor
        '''
        pass
        
    
    def run(self, puzzleFilename, cnfFormula):
        self.loadPuzzleMatrix(puzzleFilename)

        self.loadCNFFormula(cnfFormula)
        
        
    def loadPuzzleMatrix(self, puzzleFilename):
        myFile = open(puzzleFilename, 'r')
        
        for line in myFile:
            string = line.replace('.', '0')
            string = string.replace('\n', '')
            list = []
            for letter in string:
                list.append(int(letter))
            self.puzzleMat.append(list)
                
    def loadCNFFormula(self, cnfFormula):
        myFile = open(cnfFormula, 'r')
        
        for line in myFile:
            list = line.split(' ')
            list = map(int, list)
            self.cnf.append(list[:-1])
        
        
        