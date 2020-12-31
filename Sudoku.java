import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Sudoku {
    // this will contain the board to solve
    private int[][] board;
    // algo to use
    private String algo;

    // @puzzle: name of puzzle to solve
    // If puzzle name is valid, this will initiate the
    // board and parse the given puzzle name
    public Sudoku(String puzzleName, String algo) {
        this.algo = algo;

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

    private int[] isSolved() {
        int[] ret = new int[3];

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (this.board[row][col] == 0) {
                    ret[0] = row;
                    ret[1] = col;
                    ret[2] = false;
                }
            }
        }

        return;
    }

    public static void main(String[] args) {
        Sudoku test;
        if (args.length == 3)
            test = new Sudoku(args[0], args[1], args[2]);
        else if (args.length == 2)
            test = new Sudoku(args[0], args[1]);
        else {
            System.out.println("You have entered an invalid number of arguments!");
            System.exit(0);
        }

        // for (int[] arr : test.board) {
        // System.out.println(Arrays.toString(arr));
        // }
    }
}