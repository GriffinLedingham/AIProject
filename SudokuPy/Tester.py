'''
Created on Mar 12, 2013

@author: stryder
'''

from Sudoku import SudokuClass
from DPLL import DPLL

class TesterClass:
    
    sudoku1 = SudokuClass()
    sudoku2 = SudokuClass()
    sudoku3 = SudokuClass()
    sudoku4 = SudokuClass()
    dpllTester = DPLL()
    
    def __init__(self):
        pass
        
    def RunAllTests(self):
        self.testPartialAssignment()
        self.testBackTracking()
        self.testUP()
        self.testPureL()
        
        
        self.runTestOne()
        self.runTestTwo()
        self.runTestThree()
        self.runTestFour()
        
    def testPartialAssignment(self):
        testAssignment = {1:'true', 3:'false'}
        testData = [[1, 2], [-1, -2, 3]]
        result = self.dpllTester.partialInterp(testData, testAssignment)
        if result == True:
            print "ERROR"
        
        testData = [[-1, 2], [-1, 3]]
        result = self.dpllTester.partialInterp(testData, testAssignment)
        if result == True:
            print "ERROR"
        
        testData = [[1, 2, 3], [-2, -3]]
        result = self.dpllTester.partialInterp(testData, testAssignment)
        if result == False:
            print "Error"
            
    def testBackTracking(self):
        testData = [[-1, -2], [1, -2], [-1, -3]]
        testLiterals = [1, 2, 3]
        result = self.dpllTester.runBacktracking(testData, {}, testLiterals)
        if result == False:
            print "Error"
            
        testData = [[-1, -2], [-1, 2], [1, -2], [2, -3], [1, 3]]
        result = self.dpllTester.runBacktracking(testData, {}, testLiterals)
        if result == True:
            print "Error"
            
        testData = [[2, 1], [-1], [-2, -3], [3, 1]]
        result = self.dpllTester.runBacktracking(testData, {}, testLiterals)
        if result == False:
            print "Error"
            
    def testUP(self):
        pass
        
    def testPureL(self):
        testData = [[1, -2, -3, [1, 3], [2, -3]]]
        dataLit = [1, 2, 3]
        clauseList, dic, literals = self.dpllTester.pureLiteralAssignList(testData, {}, dataLit)
        if dic[1] != 'true':
            print "error"
        if dic[2] != 'true':
            print "error"
        if dic[3] != 'false':
            print"error"
        
    def runTestOne(self):
        self.sudoku1.run("sudoku1.txt", "cnf.txt", "sat1.txt")
        
    def runTestTwo(self):
        self.sudoku2.run("sudoku2.txt", "cnf.txt", "sat2.txt")
        
    def runTestThree(self):
        self.sudoku3.run("sudoku3.txt", "cnf.txt", "sat3.txt")
        
    def runTestFour(self):
        self.sudoku4.run("sudoku4.txt", "cnf.txt", "sat4.txt")