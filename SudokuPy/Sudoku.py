'''
Created on Mar 12, 2013

@author: stryder
'''

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
        
        
        testData = [[-1, 3, 4], [-2, 6, 4], [-2, -6, -3], [-4, -2], [2, -3, -1], [2, 6, 3], [2, -6, -4], [1, 5], [1, 6], [-6, 3, -5], [1, -3, -5]]
        literals = [1, 2, 3, 4, 5, 6]
        
        self.DPLL(testData, {}, literals)
        
        self.loadPuzzleMatrix(puzzleFilename)

        self.loadCNFFormula(cnfFormula)
        
        self.encodeMinimal(outputFilename)
        self.encodeExtended(outputFilename)
        
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
    
    #Return 0 if none found
    #Else return the value of the first encountered clause
    def findNextUnitClause(self, clauseList):
        #Search the clauseList for a clause with only one element.. Def of a unit clause
        for clause in clauseList:
            if(len(clause) == 1):
                return clause[0]
        #If no value return 0, since 0 cant be a literal <no negation>
        return 0

    #There is a nicer recursive implementation of this but it would make things a bit more obfuscated
    def unitPropagateList(self, clauseList, partialAssignment, literalList):
        #partial assignment will hold all the true, false assignments
        #ie {!y, y or z, y or !z} --> y = false --> {true, false or z, false or !z} --> {z, !z} | literalAssignDic = {y:true}
                    
        #Keep going until all the unit clauses are resolved           
        while(True):
            unitClause = self.findNextUnitClause(clauseList)
            #if 0 then no more unit clauses
            if unitClause == 0 or len(clauseList) == 0:
                return clauseList, partialAssignment, literalList
            #if positive then add as true value
            elif unitClause > 0:
                partialAssignment[unitClause] = 'true'
                literalList.remove(unitClause)
            #if negative assign as false value
            else:
                partialAssignment[unitClause*-1] = 'false'
                literalList.remove(unitClause*-1)
            
            #remove the unit clause from all the other clauses
            clauseList = self.removeUnitClause(unitClause, clauseList)
        return clauseList, partialAssignment, literalList
            
    
    def eliminateLiteral(self, clauseList, element, parity):
        #must iterate over a copy to avoid weirdness
        clauseListCopy = list(clauseList)
        
        #positive unit clause
        for clause in clauseListCopy:
            for element in clause:
                #Same polarity as unit clause
                if element == element:
                    #if the unit clause was negative
                    if parity == False:
                        #remove element
                        clause = self.replaceWithFalse(element, clause)
                        if clause == []:
                            clauseList.remove(clause)
                    #if the unit clause was positive    
                    elif parity == True:
                        #remove clause
                        clauseList.remove(clause)
                        break
                #Negation of unit clause
                if element == element*-1:
                    if parity == False:
                        #remove clause
                        clauseList.remove(clause)
                        break
                    elif parity == True:
                        #remove element
                        clause = self.replaceWithFalse(element*-1, clause)
                        if clause == []:
                            clauseList.remove(clause)
        return clauseList
        
    def partialInterp(self, clauseList, partialAssignment):
        for element in partialAssignment:
            if partialAssignment[element] == 'true':
                clauseList = self.eliminateLiteral(clauseList, element, True)
            if partialAssignment[element] == 'false':
                clauseList = self.eliminateLiteral(clauseList, element, False)
        #if its empty then the formula is equal to true with these partial assignments
        if clauseList == []:
            return True
        #return false if not all the clauses have not been satisfied or there are still false's
        return False
    
    def hasEmptyClause(self):
        return True
    
    def removeLiteralFromClause(self, literal, clause):
        for element in clause:
            if element == literal:
                clause.remove(element)
        return clause
    
    #FINISH THIS
    def replaceWithFalse(literal, clause)
        for element in clause:
                if element == literal:
                    clause.remove(element)
        return clause
    def removeUnitClause(self, unitClause, clauseList):
        #must itterate over a copy to avoid weirdness
        clauseListCopy = list(clauseList)
        
        #positive unit clause
        for clause in clauseListCopy:
            for element in clause:
                #Same polarity as unit clause
                if element == unitClause:
                    #if the unit clause was negative
                    if unitClause < 0:
                        #remove element
                        clause = self.removeLiteralFromClause(unitClause, clause)
                        if clause == []:
                            clauseList.remove(clause)
                    #if the unit clause was positive    
                    elif unitClause > 0:
                        #remove clause
                        clauseList.remove(clause)
                        break
                #Negation of unit clause
                if element == unitClause*-1:
                    if unitClause < 0:
                        #remove clause
                        clauseList.remove(clause)
                        break
                    elif unitClause > 0:
                        #remove element
                        clause = self.removeLiteralFromClause(unitClause*-1, clause)
                        if clause == []:
                            clauseList.remove(clause)
        return clauseList
    
    #Return True if unit clause found in the list
    #Return False if no unit clause
    def checkUnitClause(self, clauseList):
        #Search for a clause of length 1, <Def of unit clause>
        for clause in clauseList:
            if(len(clause) == 1):
                return True
        #Return false if no Unit clause found
        return False
    
    def pureLiteral(self, element, clauseList):
        firstElementSeen = 0
        for clause in clauseList:
            for literal in clause:
                if literal == element or literal*-1 == element:
                    if firstElementSeen == 0:
                        firstElementSeen = literal
                    #if the current literal is the negation of the firstElementSeen
                    if firstElementSeen != literal:
                        return False, firstElementSeen
        #If you dont see that element at all
        if firstElementSeen == 0:
            return False, firstElementSeen
        #they are all the same element
        return True, firstElementSeen
    
    def chooseLiteral(self, partialAssignment, literalList):
        for literal in literalList:
            if literal not in partialAssignment:
                return literal
        return None
    
    def pureLiteralAssignList(self, clauseList, partialAssignmnet, literalList):
        #must iterate over clause list copy to avoid weirdness
        clauseListCopy = list(clauseList)
        
        for literal in literalList:
            #A pure literal is a literal with all the same polarity(negated or not negated)
            #in the whole clause list
            result, pureLiteral = self.pureLiteral(literal, clauseList)
            if result:
                if pureLiteral > 0:
                    partialAssignmnet[literal] = 'true'
                    literalList.remove(literal)
                elif pureLiteral < 0:
                    partialAssignmnet[literal] = 'false'
                    literalList.remove(literal)
                #If we find a pureLiteral remove the whole clause from the clauseList that contains the pure literal
                for clause in clauseListCopy:
                    if literal in clause or literal*-1 in clause:
                        clauseList.remove(clause)
        return clauseList, partialAssignmnet, literalList                       
        

    
    #Create a new dic that will hold all the true, false assignments
    #ie {!y, y or z, y or !z} --> y = false --> {true, false or z, false or !z} --> {z, !z} | literalAssignDic = {y:true}
    def DPLL(self, clauseList, partialAssignment,  literalList):
        if self.partialInterp(clauseList, partialAssignment):
            return True
        if self.partialInterp(self.negate(clauseList), partialAssignment)
            return False
        
        clauseList, partialAssignment, literalList = self.unitPropagateList(clauseList, partialAssignment, literalList)
        
        clauseList, partialAssignment, literalList = self.pureLiteralAssignList(clauseList, partialAssignment, literalList)
        
        chosenLiteral = self.chooseLiteral(clauseList, literalList)
        
        posList = list(clauseList)
        posList.append([chosenLiteral])
        negList = list(clauseList)
        negList.append([chosenLiteral*-1])
        if(self.DPLL(posList, partialAssignment, literalList)):
            return True
        if(self.DPLL(negList, partialAssignment, literalList)):
            return True
        return False    
    
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
        for i in range(0,3):
            for j in range(0,3):
                for x in range(1,4):
                    for y in range(1,4):
                        for z in range(1,10):
                            myFile.write(str((3*i+x)*100 + (3*j+y)*10 + z) + ' ')
                        myFile.write('\n')
                    
        