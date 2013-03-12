'''
Created on Mar 12, 2013

@author: stryder
'''

from Sudoku import SudokuClass


class TesterClass:
    
    sudoku = SudokuClass()
    sudoku.run("sudoku.txt", "cnf.txt")
    
    def __init__(self):
        