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
        
    
    def run(self, puzzleFilename, cnfFormula):
        
        self.loadPuzzleMatrix(puzzleFilename)

        
        self.loadCNFFormula(cnfFormula)
        
    def loadPuzzleMatrix(self, puzzleFilename):
        myFile = open(puzzleFilename, 'r')
        
        for line in myFile:
            print line
        
    def loadCNFFormula(self, cnfFormula):
        myFile = open(cnfFormula, 'r')
        
        for line in myFile:
            print line
        
        
        