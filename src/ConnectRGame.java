import java.util.ArrayList;
import java.util.Scanner;

/**
 * Used to store properties of the connect r game being played
 */
public class ConnectRGame {

    private int n, m, r, depth, numOfMoves;

    private ConnectRGrid grid;

    private Scanner input = new Scanner(System.in);

    /**
     * Creates ConnectRGame instance
     * @param n - number of columns
     * @param m - number of rows
     * @param r - number of markers to be connected
     * @param depth - how far to go down tree
     */
    public ConnectRGame(int n, int m, int r, int depth) {
        this.n = n;
        this.m = m;
        this.r = r;
        this.depth = depth * 2;
        grid = new ConnectRGrid(m, n, r);
        numOfMoves = 0;
    }

    public void playHumanVsHuman() throws Exception {

        grid.setArePlayersSame(true);

        while (!isGameOver()) {
            grid.displayGrid();

            makeHumanMove();

            ++numOfMoves;
        }

        grid.displayGrid();
    }

    public void playHumanVsAI() throws Exception {

        System.out.println("Are you black or red (black goes first)?");
        System.out.println("Enter 1 for black or 2 for red");
        int color = input.nextInt();

        if (color == 2) {
            grid.setTurn(false);
        } else {
            grid.setTurn(true);
        }

        while (!isGameOver()) {
            grid.displayGrid();

            if (grid.getTurn()) {
                makeHumanMove(); // Double check this
            } else {
                makeAIMove(); // Double check this too
            }

            ++numOfMoves; // Count the total number of moves made
        }

        grid.displayGrid();

    }

    public void playAIVsAI() throws Exception {

        grid.setArePlayersSame(true);

        while (!isGameOver()) {
            grid.displayGrid();

            if (grid.getTurn()) {
                makeAIMove();
            } else {
                makeAIMoveAgainstAI();
            }

            ++numOfMoves;
        }

        grid.displayGrid();
    }

    private boolean isGameOver() {
        return numOfMoves != 0 && (numOfMoves == n * m || grid.isRConnected());
    }

    /**
     * Implementation of minMax algorithm with alpha beta pruning
     * @param grid - state of game
     * @param depth - how far down the tree we will go
     * @param maximizingPlayer - maximizing or minimizing player
     */
    private int minMaxWithAlphaBeta(ConnectRGrid grid, int depth, boolean maximizingPlayer,
                                    int alpha, int beta) throws Exception {

        int bestValue;
        int v;

        // Create copy of grid
        ConnectRGrid futureGrid;

        // Base case:
        if (depth == 0 || grid.isRConnected()) {
            return grid.getValue();
        }

        ArrayList<Integer> validMoves = grid.getValidMoves();

        // Recursive case
        if (maximizingPlayer) {
            bestValue = Integer.MIN_VALUE;
            for (int i = 0; i < n; ++i) {
                if (validMoves.contains(i)) {
                    futureGrid = grid.clone(); // Assign copy of grid to futureGrid before making move
                    futureGrid.placeMarker(i);
                    v = minMaxWithAlphaBeta(futureGrid, depth - 1, false, alpha, beta);
                    if (v > bestValue) {
                        bestValue = v;
                    }
                    if (v > alpha) {
                        alpha = v;
                    }
                    if (beta <= alpha) {
                        //System.out.println("DEBUG beta <= alpha");
                        return bestValue; // Alpha-Beta pruning
                    }
                }

            }
        } else {
            bestValue = Integer.MAX_VALUE;
            for (int i = 0; i < n; ++i) {
                if (validMoves.contains(i)) {
                    futureGrid = grid.clone(); // Assign copy of grid to futureGrid before making move
                    futureGrid.placeMarker(i);
                    v = minMaxWithAlphaBeta(futureGrid, depth - 1, true, alpha, beta);
                    if (v < bestValue) {
                        bestValue = v;
                    }
                    if (v < beta) {
                        beta = v;
                    }
                    if (beta <= alpha) {
                        //System.out.println("DEBUG beta <= alpha");
                        return bestValue; // Alpha-Beta pruning
                    }
                }
            }
        }

        return bestValue;
    }

    /**
     * Implementation of minMax algorithm with alpha beta pruning
     * @param grid - state of game
     * @param depth - how far down the tree we will go
     * @param maximizingPlayer - maximizing or minimizing player
     */
    private int minMax(ConnectRGrid grid, int depth, boolean maximizingPlayer) throws Exception {

        int bestValue;
        int v;

        // Create copy of grid
        ConnectRGrid futureGrid;

        // Base case:
        if (depth == 0 || grid.isRConnected()) {
            return grid.getValue();
        }

        ArrayList<Integer> validMoves = grid.getValidMoves();

        // Recursive case
        if (maximizingPlayer) {
            bestValue = Integer.MIN_VALUE;
            for (int i = 0; i < n; ++i) {
                if (validMoves.contains(i)) {
                    futureGrid = grid.clone(); // Assign copy of grid to futureGrid before making move
                    futureGrid.placeMarker(i);
                    v = minMax(futureGrid, depth - 1, false);
                    if (v > bestValue) {
                        bestValue = v;
                    }
                }

            }
        } else {
            bestValue = Integer.MAX_VALUE;
            for (int i = 0; i < n; ++i) {
                if (validMoves.contains(i)) {
                    futureGrid = grid.clone(); // Assign copy of grid to futureGrid before making move
                    futureGrid.placeMarker(i);
                    v = minMax(futureGrid, depth - 1, true);
                    if (v < bestValue) {
                        bestValue = v;
                    }
                }
            }
        }

        return bestValue;
    }

    private void makeAIMove() throws Exception {
        int bestValue = Integer.MIN_VALUE;
        int bestMove = 0;
        int v;

        Thread timer = new Thread(new Timer());
        ArrayList<Integer> validMoves = grid.getValidMoves();

        timer.start();
        for (int i = 0; i < n && timer.isAlive(); i++) {
            if (validMoves.contains(i)) {
                ConnectRGrid futureGrid = grid.clone();
                futureGrid.placeMarker(i);
                //v = minMax(futureGrid, depth, false);
                v = minMaxWithAlphaBeta(futureGrid, depth, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                System.out.println("DEBUG : " + v + " : " + i);
                if (v > bestValue) {
                    bestValue = v;
                    bestMove = i;
                }
            }

        }

        grid.placeMarker(bestMove);
    }

    private void makeAIMoveAgainstAI() throws Exception {
        int bestValue = Integer.MAX_VALUE;
        int bestMove = 0;
        int v;

        ArrayList<Integer> validMoves = grid.getValidMoves();

        for (int i = 0; i < n; i++) {
            if (validMoves.contains(i)) {
                ConnectRGrid futureGrid = grid.clone();
                futureGrid.placeMarker(i);
                v = minMax(futureGrid, depth, true);
                System.out.println("DEBUG : " + v + " : " + i);
                if (v < bestValue) {
                    bestValue = v;
                    bestMove = i;
                }
            }
        }

        grid.placeMarker(bestMove);
    }

    private void makeHumanMove() throws Exception {

        System.out.println("Make move (0 - " + (n - 1) + ")");
        grid.placeMarker(input.nextInt());

    }

}
