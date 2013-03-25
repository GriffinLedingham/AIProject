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

    sudoku5 = SudokuClass()
    sudoku6 = SudokuClass()
    sudoku7 = SudokuClass()
    sudoku8 = SudokuClass()
    sudoku9 = SudokuClass()
    
    def __init__(self):
        pass
        
    def RunAllTests(self):        
        self.runDPLLTests()
        self.runSudokuTests()
        
    def runDPLLTests(self):
        self.testPartialAssignment()
        self.testBackTracking()
        self.testUP()
        self.testPureL()
        self.testDPLL()
        
    def runSudokuTests(self):
        self.runTestOne()
        self.runTestTwo()
        self.runTestThree()
        self.runTestFour()
        self.runTestFive()
        self.runTestSix()
        self.runTestSeven()
        self.runTestEight()
        self.runTestNine()
        
    def testPartialAssignment(self):
        testAssignment = {1:'true', 3:'false'}
        testData = [[1, 2], [-1, -2, 3]]
        result = self.dpllTester.partialInterp(testData, testAssignment)
        if result == True:
            print "ERROR"
        else:
            print "Passed Test 1"
        
        testData = [[-1, 2], [-1, 3]]
        result = self.dpllTester.partialInterp(testData, testAssignment)
        if result == True:
            print "ERROR"
        else:
            print "Passed Test 2"
        
        testData = [[1, 2, 3], [-2, -3]]
        result = self.dpllTester.partialInterp(testData, testAssignment)
        if result == False:
            print "Error"
        else:
            print "Passed Test 3"
            
    def testBackTracking(self):
        testData = [[-1, -2], [1, -2], [-1, -3]]
        testLiterals = [1, 2, 3]
        result = self.dpllTester.runBacktracking(testData, {}, testLiterals)
        if result == False:
            print "Error"
        else:
            print "Passed Test 4"
            
        testData = [[-1, -2], [-1, 2], [1, -2], [2, -3], [1, 3]]
        result = self.dpllTester.runBacktracking(testData, {}, testLiterals)
        if result == True:
            print "Error"
        else:
            print "Passed Test 5"
            
        testData = [[2, 1], [-1], [-2, -3], [3, 1]]
        result = self.dpllTester.runBacktracking(testData, {}, testLiterals)
        if result == True:
            print "Error"
        else:
            print "Passed Test 6"
            
    def testUP(self):
        testData = [[2, 1], [-1], [-2, -3], [3, 1]]
        testLiterals = [1, 2, 3]
        partial = {}
        self.dpllTester.unitPropagateList(testData, partial, testLiterals)
        if testData != [[0]]:
            print "Error"
        else:
            print "Passed Test 7"
            
        testData = [[-1], [-1, 2], [1, -2], [2, -3], [1, 3]]
        testLiterals = [1, 2, 3]
        partial = {}
        self.dpllTester.unitPropagateList(testData, partial, testLiterals)
        if testData != [[0]]:
            print "Error"
        else:
            print "Passed Test 8"
        
    def testPureL(self):
        testData = [[1, -2, -3], [1, 3], [2, -3]]
        dataLit = [1, 2, 3]
        partial = {}
        self.dpllTester.pureLiteralAssignList(testData, partial, dataLit)
        if partial[1] != 'true':
            print "error"
        if partial[2] != 'true':
            print "error"
        else:
            print "Passed Test 9"
            
    def testDPLL(self):
        testData = [[-1, 3, 4], [-2, 6, 4], [-2, -6, -3], [-4, -2], [2, -3, -1], [2, 6, 3], [2, -6, -4], [1, 5], [1, 6], [-6, 3, -5], [1, -3, -5]]
        dataLit = [1, 2, 3, 4, 5, 6]
        partial = {}
        result = self.dpllTester.runDPLL(testData, partial, dataLit)
        if result == True:
            print "Error"
        else:
            print "Passed Test 10"        
        
    def runTestOne(self):
        self.sudoku1.run("sudoku1.txt", "sat1.txt")
        
    def runTestTwo(self):
        self.sudoku2.run("sudoku2.txt", "sat2.txt")
        
    def runTestThree(self):
        self.sudoku3.run("sudoku3.txt", "sat3.txt")
        
    def runTestFour(self):
        self.sudoku4.run("sudoku4.txt", "sat4.txt")

    def runTestFive(self):
        self.sudoku5.run("sudoku5.txt", "sat5.txt")

    def runTestSix(self):
        self.sudoku6.run("sudoku6.txt", "sat6.txt")

    def runTestSeven(self):
        self.sudoku7.run("sudoku7.txt", "sat7.txt")

    def runTestEight(self):
        self.sudoku8.run("sudoku8.txt", "sat8.txt")

    def runTestNine(self):
        self.sudoku9.run("sudoku9.txt", "sat9.txt")
