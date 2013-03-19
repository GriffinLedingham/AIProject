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
    sudoku5 = SudokuClass()
    sudoku6 = SudokuClass()
    sudoku7 = SudokuClass()
    sudoku8 = SudokuClass()
    sudoku9 = SudokuClass()
    
    def __init__(self):
        pass
        
    def RunAllTests(self):
        self.runTestOne()
        self.runTestTwo()
        self.runTestThree()
        self.runTestFour()
        self.runTestFive()
        self.runTestSix()
        self.runTestSeven()
        self.runTestEight()
        self.runTestNine()
        
    def runTestOne(self):
        self.sudoku1.run("sudoku1.txt", "cnf.txt", "sat1.txt")
        
    def runTestTwo(self):
        self.sudoku2.run("sudoku2.txt", "cnf.txt", "sat2.txt")
        
    def runTestThree(self):
        self.sudoku3.run("sudoku3.txt", "cnf.txt", "sat3.txt")
        
    def runTestFour(self):
        self.sudoku4.run("sudoku4.txt", "cnf.txt", "sat4.txt")

    def runTestFive(self):
        self.sudoku5.run("sudoku5.txt", "cnf.txt", "sat5.txt")

    def runTestSix(self):
        self.sudoku6.run("sudoku6.txt", "cnf.txt", "sat6.txt")

    def runTestSeven(self):
        self.sudoku7.run("sudoku7.txt", "cnf.txt", "sat7.txt")

    def runTestEight(self):
        self.sudoku8.run("sudoku8.txt", "cnf.txt", "sat8.txt")

    def runTestNine(self):
        self.sudoku9.run("sudoku9.txt", "cnf.txt", "sat9.txt")