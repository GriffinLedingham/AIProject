'''
Created on Mar 12, 2013

@author: stryder
'''

from Sudoku import SudokuClass


class TesterClass:
    
    sudoku = SudokuClass()
    
    def __init__(self):
        pass
        
    def RunAllTests(self):
        self.runTestOne()
        
    def runTestOne(self):
        self.sudoku.run("sudoku.txt", "cnf.txt")