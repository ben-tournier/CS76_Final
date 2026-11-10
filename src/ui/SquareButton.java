package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;

/**
 * One square on the board. Knows its position and how to draw itself.
 */
public class SquareButton extends JButton {

    private static final Color LIGHT = new Color(240, 217, 181);    // light squares
    private static final Color DARK = new Color(181, 136, 99);      // dark squares
    private static final Color SELECTED = new Color(246, 246, 105); // highlight for the selected square

    private final int row;
    private final int col;
    private final Color baseColor; // LIGHT or DARK; restored when deselected

    /**
     * Creates an empty square at the given position.
     *
     * Uses the same 1-based positions as the game classes.
     *
     * @param row the row, 1 to 8, where 1 is White's back rank (bottom of the screen)
     * @param col the column, 1 to 8, where 1 is the left side (the a-file)
     */
    public SquareButton(int row, int col) {
        this.row = row;
        this.col = col;

        // Squares alternate colors: a1 (row 1, col 1) is dark, so dark when row + col is even.
        baseColor = (row + col) % 2 == 0 ? DARK : LIGHT;

        setPreferredSize(new Dimension(70, 70));
        setFont(new Font("Segoe UI Symbol", Font.PLAIN, 40)); // font with chess glyphs on Windows
        setFocusPainted(false);  // no focus outline around the piece
        setBorderPainted(false); // no button border, so squares sit flush
        setOpaque(true);         // needed for the background color to show
        setBackground(baseColor);
    }

    /**
     * Shows a piece on this square.
     *
     * @param symbol the Unicode chess symbol to display, e.g. "♔"
     */
    public void setPieceSymbol(String symbol) {
        setText(symbol);
    }

    /**
     * Removes any piece shown on this square.
     */
    public void clearPiece() {
        setText("");
    }

    /**
     * Marks this square as selected or not, and highlights it to match.
     *
     * @param selected true to highlight the square, false to restore its normal color
     */
    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        setBackground(selected ? SELECTED : baseColor);
    }

    /**
     * Converts this square's row/col to chess notation,
     * e.g. row 1, col 1 -> "a1".
     *
     * @return the square's name in chess notation
     */
    public String getSquareName() {
        char file = (char) ('a' + col - 1); // column 1 is the a-file
        int rank = row;                     // row number is the rank
        return "" + file + rank;
    }

    /**
     * @return this square's row, 1 to 8, where 1 is White's back rank
     */
    public int getRow() {
        return row;
    }

    /**
     * @return this square's position as {row, col}, matching piece.getPosition()
     */
    public int[] getPosition() {
        return new int[] {row, col};
    }

    /**
     * @return this square's column, 1 to 8, where 1 is the left side
     */
    public int getCol() {
        return col;
    }
}
