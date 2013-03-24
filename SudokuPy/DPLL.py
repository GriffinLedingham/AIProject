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

    
    def assignUnassignedLiterals(self, partialAssignment, literalList):
        for literal in literalList:
            partialAssignment[literal] = 'true'
            literalList.remove(literal)
    
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
                        self.removeLiteralFromClause(literal, clauseList[i])
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
                        self.removeLiteralFromClause(literal*-1, clauseList[i])
                        if clause == []:
                            clauseKillList.append(i)
                            
        clauseKillList.reverse()
        for i in clauseKillList:
            del clauseList[i]
    
    def partialInterp(self, clauseList, partialAssignment):
        for key, item in partialAssignment.iteritems():
            if item == 'true':
                self.eliminateLiteral(clauseList, key, True)
            if item == 'false':
                self.eliminateLiteral(clauseList, key, False)
        #if its empty then the formula is equal to true with these partial assignments
        if clauseList == []:
            return 'true'
        if [0] in clauseList:
            return 'false'
        #return false if not all the clauses have not been satisfied
        return 'unassigned'
    
    def removeLiteralFromClause(self, literal, clause):
        for element in clause:
            if element == literal:
                if len(clause) == 1:
                    clause.remove(element)
                    clause.append(0)
                    return
                clause.remove(element)
        return
    
    
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
          
        #Keep going until all the unit clauses are resolved           
        while(True):
            unitClause = self.findNextUnitClause(clauseList)
            #if 0 then no more unit clauses
            if unitClause == 0 or len(clauseList) == 0:
                return
            #if positive then add as true value
            elif unitClause > 0:
                partialAssignment[unitClause] = 'true'
                literalList.remove(unitClause)
            #if negative assign as false value
            else:
                partialAssignment[unitClause*-1] = 'false'
                literalList.remove(unitClause*-1)
            
            #remove the unit clause from all the other clauses
            self.removeUnitClause(unitClause, clauseList)
        return  
    
      
    def removeUnitClause(self, unitClause, clauseList):
        #must itterate over a copy to avoid weirdness
        clauseListCopy = copy.deepcopy(clauseList)
        clauseKillList = []
        #positive unit clause
        for i, clause in enumerate(clauseListCopy):
            for element in clause:
                #Same polarity as unit clause
                if element == unitClause:
                    #remove clause
                    clauseKillList.append(i)
                    break
                #Negation of unit clause
                if element == unitClause*-1:
                    #remove element
                    self.removeLiteralFromClause(unitClause*-1, clauseList[i])
                    if clause == []:
                        clauseKillList.append(i)
                            
        clauseKillList.reverse()
        for i in clauseKillList:
            del clauseList[i]
        return
    
    #Return True if unit clause found in the list
    #Return False if no unit clause
    def checkUnitClause(self, clauseList):
        #Search for a clause of length 1, <Def of unit clause>
        for clause in clauseList:
            if(len(clause) == 1):
                return True
        #Return false if no Unit clause found
        return False
    
    
    def removePureFromList(self, literal, clauseList):
        clauseListCopy = copy.deepcopy(clauseList)
        
        for clause in clauseListCopy:
            if literal in clause:
                clauseList.remove(clause)    
                
                
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
        #must iterate over literal list copy to avoid weirdness
        literalListCopy = copy.deepcopy(literalList)
        
        for literal in literalListCopy:
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
                self.removePureFromList(pureLiteral, clauseList)
        return
    
    def runBacktracking(self, clauseList, partialAssignment,  literalList):
        result = self.partialInterp(clauseList, partialAssignment)
        if result == 'true':
            return True
        if result == 'false':
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
        result = self.partialInterp(clauseList, partialAssignment)
        if result == 'true':
            self.assignUnassignedLiterals(partialAssignment, literalList)
            return True
        if result == 'false':
            return False
        
        self.unitPropagateList(clauseList, partialAssignment, literalList)
        if [0] in clauseList:
            return False
        
        self.pureLiteralAssignList(clauseList, partialAssignment, literalList)
        if clauseList == []:
            self.assignUnassignedLiterals(partialAssignment, literalList)
            return True        
        
        chosenLiteral = self.chooseLiteral(partialAssignment, literalList)
        
        posDict = dict(partialAssignment)
        posDict[chosenLiteral] = 'true'
        
        negDict = dict(partialAssignment)
        negDict[chosenLiteral] = 'false'
        
        literalList.remove(chosenLiteral)
        
        if(self.runDPLL(copy.deepcopy(clauseList), posDict, list(literalList))):
            return True
        if(self.runDPLL(copy.deepcopy(clauseList), negDict, list(literalList))):
            return True
        return False 