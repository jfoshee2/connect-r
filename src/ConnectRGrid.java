import java.util.ArrayList;

/**
 * Used to keep track of coordinates in connect r grid
 */
public class ConnectRGrid {

    private int rows; // number of rows in grid
    private int columns; // number of columns in grid
    private int r; // number of markers that should be connected
    private int mostRecentColumn; // the most recent column a marker was placed in
    private boolean turn = true;
    private boolean arePlayersSame = false; // boolean variable for see whether both players are human or AI

    private int[] manyUsed; // Used to keep track of how many markers are in each column
    private Player data[][] = new Player[6][7]; // Default 6 * 7 grid

    private int R_VALUE; // Winning value

    /**
     * Creates ConnectFourGrid instance.
     * @param rows - number of rows in ConnectFourGrid
     * @param columns - number of columns in ConnectFourGrid
     */
    public ConnectRGrid(int rows, int columns, int r) {
        this.rows = rows;
        this.columns = columns;
        this.r = r;
        data = new Player[rows][columns];
        manyUsed = new int[columns];
        R_VALUE = r * (int) Math.pow(rows, 2) * columns;
        initializeGrid();
    }

    /**
     * Will be used to create new instance of ConnectFourGrid
     * using existing Player[][]
     * @param game - existing Player[][]
     */
    public ConnectRGrid(Player[][] game) {
        this.data = game.clone();
        for (int i = 0; i < game.length; i++) {
            this.data[i] = game[i].clone();
        }
    }

    public ConnectRGrid clone() {
        ConnectRGrid result = new ConnectRGrid(this.data);
        result.rows = this.rows;
        result.columns = this.columns;
        result.r = this.r;
        result.mostRecentColumn = this.mostRecentColumn;
        result.turn = this.turn;
        result.manyUsed = this.manyUsed.clone();
        result.R_VALUE = this.R_VALUE;
        return result;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public boolean getTurn() {
        return turn;
    }

    public void setArePlayersSame(boolean arePlayersSame) {
        this.arePlayersSame = arePlayersSame;
    }

    public boolean arePlayersSame() {
        return arePlayersSame;
    }

    public int getMostRecentColumn() {
        return mostRecentColumn;
    }

    public void displayGrid() {
        System.out.println("Most Recent Move: " + mostRecentColumn);
        System.out.println("\nCurrent game status: " + getValue() + "\n(AI = A  and  Human = H):\n");

        for (int i = rows - 1; i >= 0; i--) {
            for (int j = 0; j < columns; j++) {
                switch (data[i][j]) {
                    case AI:
                        System.out.print("A "); break;
                    case HUMAN:
                        System.out.print("H "); break;
                    case PLAYER_ONE:
                        System.out.print("1 "); break;
                    case PLAYER_TWO:
                        System.out.print("2 "); break;
                    case NEUTRAL:
                        System.out.print("* "); break;
                }
            }
            System.out.println();
        }

//        if (isGameOver()) {
//            System.out.println("Game over");
//        } else if (lastMover() == ConnectFour.playerType.AI) {
//            System.out.println("Please type in a column number");
//        } else {
//            System.out.println("AI turn please wait");
//        }
    }

    public boolean isRConnected() {

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (value(i, j, 1, 0) == R_VALUE || value(i, j, 0, 1) == R_VALUE || value(i, j, 1, 1) == R_VALUE ||
                        value(i, j, 1, -1) == R_VALUE) {
                    return true;
                }
                if (value(i, j, 1, 0) == -R_VALUE || value(i, j, 0, 1) == -R_VALUE || value(i, j, 1, 1) == -R_VALUE ||
                        value(i, j, 1, -1) == -R_VALUE) {
                    return true;
                }
            }
        }

        return false;
    }

    public ArrayList<Integer> getValidMoves() {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < columns; i++) {
            if (isLegal(i)) {
                result.add(i);
            }
        }

        return result;
    }

    public int getValue () {
        int result = 0;

//        for (int i = r - 1; i < rows; i++) {
//            for (int j = 0; j < columns; j++) {
//                result += value(i, j, -1, 0);
//            }
//        }
//
//        for (int i = 0; i < rows; i++) {
//            for (int j = r - 1; j < columns; j++) {
//                result += value(i, j, 0, -1);
//            }
//        }
//
//        for (int i = r - 1; i < rows; i++) {
//            for (int j = r - 1; j < columns; j++) {
//                result += value(i, j, -1, -1);
//            }
//        }
//
//        for (int i = r - 1; i < rows; i++) {
//            for (int j = 0; j < columns - r; j++) {
//                result += value(i, j, -1, 1);
//            }
//        }



        // Iterate through grid and calculate value
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                // Check for connections vertically
                result += value(i, j, 1, 0);
                // Check for connections horizontally
                result += value(i, j, 0, 1);
                // Check for connections diagonally to the right
                 result += value(i, j, 1, 1);
                // Check for connections diagonally to the left
                result += value(i, j, 1, -1);
            }
        }

        return result;
    }

    /**
     *
     * @param row - row index of starting cell
     * @param column - column index of starting cell
     * @param deltaR - how much to increment row
     * @param deltaC - how much to increment column
     * @return value for choice
     */
    private int value(int row, int column, int deltaR, int deltaC) {
        int endRow = row + (r - 1) * deltaR;
        int endColumn = column + (r - 1) * deltaC;
        int playerScore = 0;
        int opponentScore = 0;

        // If there is no chance of connecting r markers based on position
        if (row < 0 || column < 0 || endRow < 0 || endColumn < 0
                || row >= rows || endRow >= rows
                || column >= columns || endColumn >= columns) {
            return 0;
        }

        for (int i = 0; i < r; i++) {
            if (data[row][column] == Player.AI) {
                ++playerScore;
            } else if (data[row][column] == Player.HUMAN) {
                ++opponentScore;
            }

            // if both players are human or AI
            if (arePlayersSame) {
                if (data[row][column] == Player.PLAYER_ONE) {
                    ++playerScore;
                } else if (data[row][column] == Player.PLAYER_TWO) {
                    ++opponentScore;
                }
            }
            row += deltaR; // Increment row
            column += deltaC; // Increment column
        }

        if (playerScore > 0 && opponentScore > 0) {
            return 0;
        } else if (playerScore == r) {
            return R_VALUE;
        } else if (opponentScore == r) {
            return -R_VALUE;
        } else {
            return playerScore - opponentScore;
        }
    }

    public void placeMarker(int column) throws Exception {
        if (isLegal(column)) {
            int row = manyUsed[column]++;
            data[row][column] = nextMover();
            mostRecentColumn = column;
            turn = !turn;
        }
    }

    private Player nextMover() {
        if (arePlayersSame) {
            return turn ? Player.PLAYER_ONE : Player.PLAYER_TWO;
        } else {
            return turn ? Player.HUMAN : Player.AI;
        }

    }

    public Player lastMover() {
        if (arePlayersSame) {
            return !turn ? Player.PLAYER_TWO : Player.PLAYER_ONE;
        } else {
            return !turn ? Player.AI : Player.HUMAN;
        }
    }

    /**
     * Used to populate Player[][]
     */
    private void initializeGrid() {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                data[i][j] = Player.NEUTRAL;
            }
        }
    }

    private boolean isLegal(int move) {
        return move < columns && manyUsed[move] < rows;
    }

}
