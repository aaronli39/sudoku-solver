# Sudoku Solver

## Algorithms
### Naive Solver
1. Checks if the current board is solved.
2. If it is not, get the first unsolved cell.
3. Get the list of possible guesses for that cell.
4. Try each guess until the board can be solved with some guess. If it can't, backtrack and try the next possible guess.

### Slightly Smarter Solver
1. Initially fills in cells in the board with 1 guess (in order words, cells we are certain of).
2. Checks if the current board is solved.
3. If it is not, get the first unsolved cell.
4. Get the list of possible guesses for that cell.
5. Try each guess until the board can be solved with some guess. If it can't, backtrack and try the next possible guess.

### Smart Solver
1. Initially fills in cells in the board with 1 guess (in order words, cells we are certain of).
2. Checks if the current board is solved.
3. If it is not, get the unsolved cell with the least amount of possible guesses.
4. Get the list of possible guesses for that cell.
5. Try each guess until the board can be solved with some guess. If it can't backtrack and try the next possible guess.

### Smartest Solver
1. Initially fills in cells in the board with 1 guess (in order words, cells we are certain of).
2. Checks if the current board is solved.
3. If it is not, get the unsolved cell with the least amount of possible guesses.
4. Get the list of possible guesses for that cell.
5. Try each guess and fill in cells we are certain of until the board can be solved with some guess. If it can't backtrack and try the next possible guess.