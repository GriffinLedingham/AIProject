'''
Created on Mar 12, 2013

@author: stryder
'''

from Sudoku import SudokuClass


class TesterClass:
    
    sudoku1 = SudokuClass()
    sudoku2 = SudokuClass()
    sudoku3 = SudokuClass()
    sudoku4 = SudokuClass()
    
    def __init__(self):
        pass
        
    def RunAllTests(self):
        self.runTestOne()
        self.runTestTwo()
        self.runTestThree()
        self.runTestFour()
        
    def runTestOne(self):
        self.sudoku1.run("sudoku1.txt", "cnf.txt", "sat1.txt")
        
    def runTestTwo(self):
        self.sudoku2.run("sudoku2.txt", "cnf.txt", "sat2.txt")
        
    def runTestThree(self):
        self.sudoku3.run("sudoku3.txt", "cnf.txt", "sat3.txt")
        
    def runTestFour(self):
        self.sudoku4.run("sudoku4.txt", "cnf.txt", "sat4.txt")