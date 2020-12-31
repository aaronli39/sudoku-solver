import java.io.*;
import java.util.*;

public class Sudoku {
    // input file name
    private String inputFile;
    // output file name
    private String outputFile;
    // this will contain the board to solve
    private int[][] board;
    // algo to use
    private String algo;
    // count of backtracks
    private int backtracks;

    // @puzzle: name of puzzle to solve
    // If puzzle name is valid, this will initiate the
    // board and parse the given puzzle name
    public Sudoku(String puzzleName, String algo) {
        this.algo = algo;
        backtracks = 0;
        outputFile = "out.txt";
        // try to open file
        try {
            File file = new File("./Sudoku-boards.txt");
            Scanner scanner = new Scanner(file);
            board = new int[9][9];

            // get to the designated puzzle first
            while (scanner.hasNext()) {
                if (!scanner.next().equals(puzzleName))
                    continue;
                else
                    break;
            }

            // scan and fill board
            for (int row = 0; row < 9; row++) {
                String[] arr = scanner.next().split(",");
                for (int col = 0; col < 9; col++) {
                    if (arr[col].equals("_"))
                        board[row][col] = 0;
                    else
                        board[row][col] = Integer.parseInt(arr[col]);
                }
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("The specified puzzle doesn't exist!");
            System.exit(0);
        }
    }

    // @inputFile: user specified input file with sudoku boards
    // @outputFile: specified output buffer
    // If inputs are valid, this will initialize the board
    // and parse the lines of the board
    public Sudoku(String inputFile, String outputFile, String algo) {
        this.algo = algo;
        this.backtracks = 0;
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        // try to open file
        try {
            File file = new File(inputFile);
            Scanner scanner = new Scanner(file);
            board = new int[9][9];
            int index = 0;

            // scan and fill board
            while (scanner.hasNext()) {
                String[] line = scanner.next().split(",");
                for (int i = 0; i < 9; i++) {
                    if (line[i].equals("_"))
                        this.board[index][i] = 0;
                    else
                        this.board[index][i] = Integer.parseInt(line[i]);
                }

                index++;
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("The specified input file doesn't exist!");
            System.exit(0);
        }

    }

    // returns first cell that is not solved
    // if board is solved, then thats cool bro
    // cell[2] is boolean as int (1 == true, 0 == false)
    private int[] isSolved() {
        int[] cell = new int[3];
        cell[0] = -1;
        cell[1] = -1;
        cell[2] = 1;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (this.board[row][col] == 0) {
                    cell[0] = row;
                    cell[1] = col;
                    cell[2] = 0;
                    return cell;
                }
            }
        }
        return cell;
    } 

    // check if n can be placed at position (r, c)
    // based on rules of sudoku
    // n can't be in r, c, or the box
    private boolean isSafe(int n, int r, int c) {
        // check if n is valid in the row
        for (int i = 0; i < 9; i++) {
            // there is a cell with same value
            if (this.board[r][i] == n) return false;
        }
        // check if n is valid in the column
        for (int i = 0; i < 9; i++) {
            // there is a cell with same value
            if (this.board[i][c] == n) return false;
        }
        // check if n is valid in the box
        // rowStart and colStart are the start
        // of n's row and col
        int rowStart = (r / 3) * 3;
        int colStart = (c / 3) * 3;
        for (int i = rowStart; i < rowStart + 3; i++) {
            for (int j = colStart; j < colStart + 3; j++) {
                if (this.board[i][j] == n) return false;
            }
        }
        return true;
    }

    // get a list of safe guesses at specified cell (r, c)
    private int[] validList(int r, int c) {
        List<Integer> guesses = new ArrayList<>();
        for (int n = 1; n < 10; n++) {
            if (isSafe(n, r, c)) guesses.add(n);
        }
        int[] validGuesses = new int[guesses.size()];
        for (int i = 0; i < guesses.size(); i++) {
            validGuesses[i] = guesses.get(i);
        }
        return validGuesses;
    }

    // get the cell with the least number of possible guesses
    // formated as [ r, c, [list of guesses] ]
    private Object[] getSmallest() {
        int[] temp = isSolved();
        // initial unsolved cell with list of possible values
        Object[] smallest = {temp[0], temp[1], validList(temp[0], temp[1])};
        // loop through entire board and get 
        // the smallest valid list to solve for first
        for (int row = temp[0]; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int[] guess = validList(row, col);
                // type casting...
                if (board[row][col] == 0 && guess.length < ((int[])smallest[2]).length) {
                    smallest[0] = row;
                    smallest[1] = col;
                    smallest[2] = guess;
                }
                    
            }
        }
        return smallest;
    }

    private void write() {
        System.out.println(this.backtracks);
        StringBuilder output = new StringBuilder();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                output.append(board[row][col]);
                output.append(col == 8 ? "\n" : ",");
            }
        }
        try {
            String out = output.toString();
            FileOutputStream ostream = new FileOutputStream(this.outputFile);
            byte[] strToBytes = out.getBytes();
            ostream.write(strToBytes);
            ostream.close();
        } catch (Exception e) {
            System.out.println("Do better...");
        }
    }

    //===============================Solvers===========================================
    public boolean naive() {
        int[] check = isSolved();
        // check if board is solved
        if (check[2] == 1) return true; 
        // if its not solved, get unsolved cell
        int row = check[0];
        int col = check[1];
        // get possible guesses of cell at (row, col)
        int[] guesses = validList(row, col);
        // for each cell attempt to solve it until board is solved
        // otherwise backtrack to next guess
        for (int n: guesses) {
            // set cell to n
            board[row][col] = n;
            // try solving the rest of the board
            if (naive()) return true;
            // otherwise try next n and increment backtracks
            this.backtracks++;
        }
        // reset the cell since the guesses were bad
        board[row][col] = 0;
        return false;
    }

    public static void main(String[] args) {
        Sudoku test;
        if (args.length == 3) {
            test = new Sudoku(args[0], args[1], args[2]);
        }
            
        else if (args.length == 2) {
            test = new Sudoku(args[0], args[1]);
            test.naive();
            test.write();
        }
        else {
            System.out.println("You have entered an invalid number of arguments!");
            System.exit(0);
        }

        
        

        // for (int[] arr : test.board) {
        // System.out.println(Arrays.toString(arr));
        // }
    }
}