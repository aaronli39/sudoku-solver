#! /usr/bin/python3

import sys

# Sudoku-boards.txt
inp = open(sys.argv[1], 'r')
# out.txt or something
out = open(sys.argv[2], 'w')
# should be full name (without space/newline at end)
# of an unsolved board
name_of_board = sys.argv[3]
backtracks = 0

# size stuff
SIZE = 9
board = []
masterBoard = []

# parse input board by argv[3] (name of board)
# fills in golbal board with the specified cells
# or 0 for empty cells to solve
def getBoard():
    board = []
    lines = [x.strip() for x in inp.readlines()]
    for i in range(1, 10):
        row = lines[lines.index(name_of_board) + i].strip().split(",")
        # change "_" to 0s
        for i in range(9):
            if row[i] == '_':
                row[i] = 0
            else:
                row[i] = int(row[i])
        board.append(row)
    return board

# call func to populate board
board = getBoard()

# check if board is solved
# if not, return an unsolved cell
def is_solved():
    isSolved = True
    for i in range(0, SIZE):
        for j in range(0, SIZE):
            # if cell isnt assigned
            if board[i][j] == 0:
                row = i
                col = j
                isSolved = False
                a = [row, col, isSolved]
                return a
    # everything solved, return
    a = [-1, -1, isSolved]
    return a

# check if n can be placed
# at row r, col c, and box bounded
# by r and c, based on rules of sudoku
def is_safe(n, r, c):
    # check if n is valid in row
    for i in range(0, SIZE):
        # there is a cell with same value
        if board[r][i] == n:
            return False

    # check if n is valid in column
    for i in range(0, SIZE):
        # there is a cell with same value
        if board[i][c] == n:
            return False

    # check if n is valid in box

    # row_start and col_start are the start
    # of n's row, col
    row_start, col_start = (r // 3) * 3, (c // 3) * 3
    for i in range(row_start, row_start+3):
        for j in range(col_start, col_start+3):
            if board[i][j] == n:
                return False

    return True

# get a list of safe
# values of specified cell
def valid_list(r, c):
    ret = []
    for i in range(1, 10):
        if is_safe(i, r, c):
            ret.append(i)
    return ret

# get the cell with least
# number of possible guesses
def get_smallest():
    # get next unsolved cell
    temp = is_solved()

    # initial unsolved cell with list of possible values
    ret = [temp[0], temp[1], valid_list(temp[0], temp[1])]

    # loop through entire board and get 
    # smallest valid list so we can do that first
    for row in range(temp[0], SIZE):
        for col in range(0, SIZE):
            curr = valid_list(row, col)
            if board[row][col] == 0 and len(curr) <= len(ret[2]):
                ret = [row, col, curr]

    return ret

# -----------------------------------------------------------------------
# Naive algorithm
def solve():
    row, col = 0, 0
    global backtracks

    # if all cells are assigned then the sudoku is already solved
    a = is_solved()
    if a[2]: return True

    # not solved, get an unsolved cell
    row = a[0]
    col = a[1]

    # get possible values of cell
    lis = valid_list(row, col)


    # for each value of cell,
    # attempt to solve it until
    # true, or backtrack to next value
    for i in lis:
        # set board to i
        board[row][col] = i

        # try solving board
        # with modified i
        if solve(): return True

        # didn't solve, so we
        # try next i and increment
        # backtrack
        backtracks += 1

    # we want to reset the current cell
    board[row][col] = 0

    return False
# ----------------------------------------------------------------------


# ----------------------------------------------------------------------
# smarter algo 1: fill in cells we are CERTAIN of
# this only helps for easy/medium boards 
def solve1(state):
    # on the very first solve1 call,
    # we want to fill in cells we are certain of
    if state == 0:
        for row in range(0, 9):
            for col in range(0, 9):
                if board[row][col] == 0:
                    temp = valid_list(row, col)
                    if len(temp) == 1: board[row][col] = temp[0]

    row, col = 0, 0
    global backtracks
    # if all cells are assigned then the sudoku is already solved
    a = is_solved()
    if a[2]: return True
    row = a[0]
    col = a[1]

    lis = valid_list(row, col)
    for i in lis:
        # check if i is a valid cell
        board[row][col] = i

        # backtracking
        if solve1(1): return True

        # reassign the cell
        backtracks += 1
        board[row][col] = 0

    return False
# ------------------------------------------------------------------------


# ------------------------------------------------------------------------
# smarter algo 2: use the smallest guess
def solve2(state):
    # fill in cells we are certain of
    if state == 0:
        for row in range(0, 9):
            for col in range(0, 9):
                if board[row][col] == 0:
                    temp = valid_list(row, col)
                    if len(temp) == 1: board[row][col] = temp[0]

    # if all cells are assigned then the sudoku is already solved
    temp = get_smallest()
    a = is_solved()

    # if board solved
    if a[2]: return True

    row = temp[0]
    col = temp[1]
    global backtracks

    for i in temp[2]:
        # try i
        board[row][col] = i

        # recurse
        if solve2(1): return True

        # backtracking...
        # reassign the cell
        backtracks += 1
        board[row][col] = 0

    return False
# -------------------------------------------------------------------------

# reverses filled in cells to 0
def rev(lis):
    for i in lis: 
        if (i != True and i != False):
            board[i[0]][i[1]] = 0

def fill():
    ret = []
    for row in range(0, 9):
        for col in range(0, 9):
            if board[row][col] == 0:
                temp = valid_list(row, col)
                if len(temp) == 1:
                    ret.append([row, col, temp[0]])
                    board[row][col] = temp[0]
                elif len(temp) == 0: 
                    ret.append(False)
                    return ret
    # print(ret)
    ret.append(True)
    return ret

# ------------------------------------------------------------------------
# smarter algo 3: use the smallest guesses and fill 
# cells that we are certain of
def solve3(state, back):
    if state == 0:
        for row in range(0, 9):
            for col in range(0, 9):
                if board[row][col] == 0:
                    temp = valid_list(row, col)
                    if len(temp) == 1:
                        board[row][col] = temp[0]

    # if all cells are assigned then the sudoku is already solved
    temp = get_smallest()
    a = is_solved()

    if a[2]: return True

    row = temp[0]
    col = temp[1]
    global backtracks

    for i in temp[2]:
        # check if i is a valid cell
        board[row][col] = i

        # baccc is list of filled in
        # certan cells
        baccc = fill()

        # backtracking
        if solve3(1, baccc): 
            return True

        backtracks += 1
        
        # revert board to state
        # before modification
        board[row][col] = 0
        rev(baccc)

    return False
# -------------------------------------------------------------------------

def write():
    # print(solve3(0, []))
    # solve()
    # solve1(0)
    # solve2(0)
    # solve3(0, [])

    print(backtracks)
    # name_of_solved_board = name_of_board.replace('unsolved', 'solved') + '\n'
    # out.write(name_of_solved_board)
    for i in range(9):
        out.write(','.join([str(x) for x in board[i]]) + '\n')

write()