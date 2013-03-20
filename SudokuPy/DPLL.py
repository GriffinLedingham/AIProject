'''
Created on Mar 19, 2013

@author: stryder
'''
import copy

class DPLL(object):
    '''
    classdocs
    '''


    def __init__(self):
        '''
        Constructor
        '''
        pass
        
    def tests(self):        
        testData = [[-1, 3, 4], [-2, 6, 4], [-2, -6, -3], [-4, -2], [2, -3, -1], [2, 6, 3], [2, -6, -4], [1, 5], [1, 6], [-6, 3, -5], [1, -3, -5]]
        literals = [1, 2, 3, 4, 5, 6]
        
        self.DPLL(testData, {}, literals)
        
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
    
    def eliminateLiteral(self, clauseList, literal, parity):
        #must itterate over a copy to avoid weirdness
        clauseListCopy = copy.deepcopy(clauseList)
        clauseKillList = []
        
        #positive unit clause
        for i, clause in enumerate(clauseListCopy):
            for element in clause:
                #Same polarity as unit clause
                if element == literal:
                    #if the unit clause was negative
                    if parity == False:
                        #remove element
                        clause = self.removeLiteralFromClauseNoFalse(literal, clauseList[i])
                        if clause == []:
                            clauseKillList.append(i)
                    #if the unit clause was positive    
                    elif parity == True:
                        #remove clause
                        clauseKillList.append(i)
                        break
                #Negation of unit clause
                if element == literal*-1:
                    if parity == False:
                        #remove clause
                        clauseKillList.append(i)
                        break
                    elif parity == True:
                        #remove element
                        clause = self.removeLiteralFromClauseNoFalse(literal*-1, clauseList[i])
                        if clause == []:
                            clauseKillList.append(i)
                            
        clauseKillList.reverse()
        for i in clauseKillList:
            del clauseList[i]
        return clauseList
    
    def partialInterp(self, clauseList, partialAssignment):
        clauseListCopy = copy.deepcopy(clauseList)
        for key, item in partialAssignment.iteritems():
            if item == 'true':
                clauseListCopy = self.eliminateLiteral(clauseListCopy, key, True)
            if item == 'false':
                clauseListCopy = self.eliminateLiteral(clauseListCopy, key, False)
        #if its empty then the formula is equal to true with these partial assignments
        if clauseListCopy == []:
            return True
        #return false if not all the clauses have not been satisfied or there are still false's
        return False
    
    def removeLiteralFromClause(self, literal, clause):
        for element in clause:
            if element == literal:
                clause.remove(element)
        return clause
    
    def removeLiteralFromClauseNoFalse(self, literal, clause):
        for element in clause:
            if element == literal:
                if len(clause) == 1:
                    clause.remove(element)
                    clause.append(0)
                    return clause
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
        clauseListCopy = copy.deepcopy(clauseList)
        
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
    
    def negateList(self, clauseList):
        negatedList = []
        for clause in clauseList:
            literalList = []
            for literal in clause:
                literalList.append(literal*-1)
            negatedList.append(literalList)
        return negatedList
    
    def runBacktracking(self, clauseList, partialAssignment,  literalList):
        if self.partialInterp(clauseList, partialAssignment):
            return True
        if self.partialInterp(self.negateList(clauseList), partialAssignment):
            return False
        
        chosenLiteral = self.chooseLiteral(partialAssignment, literalList)
        
        posDict = dict(partialAssignment)
        posDict[chosenLiteral] = 'true'
        negDict = dict(partialAssignment)
        negDict[chosenLiteral] = 'false'
        if(self.runBacktracking(clauseList, posDict, literalList)):
            return True
        if(self.runBacktracking(clauseList, negDict, literalList)):
            return True
        return False 
    
    def runDPLL(self, clauseList, partialAssignment,  literalList):
        if self.partialInterp(clauseList, partialAssignment):
            return True
        if self.partialInterp(self.negateList(clauseList), partialAssignment):
            return False
        
        clauseList, partialAssignment, literalList = self.unitPropagateList(clauseList, partialAssignment, literalList)
        
        clauseList, partialAssignment, literalList = self.pureLiteralAssignList(clauseList, partialAssignment, literalList)
        
        chosenLiteral = self.chooseLiteral(partialAssignment, literalList)
        
        posList = list(clauseList)
        posList.append([chosenLiteral])
        negList = list(clauseList)
        negList.append([chosenLiteral*-1])
        if(self.runDPLL(posList, partialAssignment, literalList)):
            return True
        if(self.runDPLL(negList, partialAssignment, literalList)):
            return True
        return False 