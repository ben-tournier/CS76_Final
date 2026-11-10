package ui;

import java.awt.GridLayout;

import javax.swing.JPanel;

/**
 * The 8x8 grid of squares. Receives clicks and updates the display.
 * <p>
 * This class does NOT decide whether moves are legal; that belongs to the
 * game logic.
 */
public class BoardPanel extends JPanel {

    /** Number of rows and columns on the board. */
    public static final int SIZE = 8;

    private final SquareButton[][] squares = new SquareButton[SIZE][SIZE];
    private final StatusPanel statusPanel;
    private SquareButton selected; // null when nothing is selected

    /**
     * Builds the grid of squares and places the starting pieces.
     *
     * @param statusPanel the status bar to send messages to when squares are clicked
     */
    public BoardPanel(StatusPanel statusPanel) {
        this.statusPanel = statusPanel;
        setLayout(new GridLayout(SIZE, SIZE));

        // Positions are 1-based to match the game classes: row 1 is White's
        // back rank. GridLayout fills top-to-bottom, so add row 8 first.
        for (int row = SIZE; row >= 1; row--) {
            for (int col = 1; col <= SIZE; col++) {
                SquareButton square = new SquareButton(row, col);
                square.addActionListener(e -> handleSquareClick(square));
                squares[row - 1][col - 1] = square; // convert position to index
                add(square);
            }
        }

        setUpStartingPieces();
    }

    /**
     * Places the standard starting position on the board.
     * <p>
     * Placeholder: draws pieces as Unicode text until the Piece classes exist.
     */
    private void setUpStartingPieces() {
        String[] blackBack = {"♜", "♞", "♝", "♛", "♚", "♝", "♞", "♜"};
        String[] whiteBack = {"♖", "♘", "♗", "♕", "♔", "♗", "♘", "♖"};

        for (int col = 1; col <= SIZE; col++) {
            getSquare(8, col).setPieceSymbol(blackBack[col - 1]); // Black back rank
            getSquare(7, col).setPieceSymbol("♟");                // Black pawns
            getSquare(2, col).setPieceSymbol("♙");                // White pawns
            getSquare(1, col).setPieceSymbol(whiteBack[col - 1]); // White back rank
        }
    }

    /**
     * Handles a click on a square.
     * <p>
     * For now: click a square to select it, click it again to deselect, or
     * click a different square to move the selection there.
     * Later: a second click on a different square should attempt a move.
     *
     * @param square the square that was clicked
     */
    private void handleSquareClick(SquareButton square) {
        // Clicking the already-selected square clears the selection.
        if (selected == square) {
            selected.setSelected(false);
            selected = null;
            statusPanel.showMessage("Selection cleared");
            return;
        }

        // Otherwise, un-highlight the old square (if any) and select the new one.
        if (selected != null) {
            selected.setSelected(false);
        }
        selected = square;
        selected.setSelected(true);
        statusPanel.showMessage("Selected " + square.getSquareName());
    }

    /**
     * Returns the square at the given position.
     * Uses the same 1-based positions as the game classes.
     *
     * @param row the row, 1 to 8, where 1 is White's back rank
     * @param col the column, 1 to 8, where 1 is the left side (the a-file)
     * @return the square at that row and column
     */
    public SquareButton getSquare(int row, int col) {
        return squares[row - 1][col - 1];
    }

    /**
     * Overloads the function above to accept an array, e.g. from piece.getPosition().
     *
     * @param position the position as {row, col}
     * @return the square at that position
     */
    public SquareButton getSquare(int[] position) {
        return getSquare(position[0], position[1]);
    }
}
