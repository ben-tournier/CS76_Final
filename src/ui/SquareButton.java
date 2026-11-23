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
    private static final Color FOG_LIGHT = new Color(95, 95, 100);  // hidden light squares (Fog of War)
    private static final Color FOG_DARK = new Color(70, 70, 75);    // hidden dark squares (Fog of War)

    private boolean hidden = false; // true when the square is covered by fog

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
        this(row, col, DEFAULT_PIXEL_SIZE);
    }

    /** Width and height of a square in pixels when no size is given. */
    public static final int DEFAULT_PIXEL_SIZE = 70;

    /**
     * Creates an empty square at the given position with the given size in pixels.
     * Larger boards use smaller squares so the window still fits on screen.
     */
    public SquareButton(int row, int col, int pixelSize) {
        this.row = row;
        this.col = col;

        // Squares alternate colors: a1 (row 1, col 1) is dark, so dark when row + col is even.
        baseColor = (row + col) % 2 == 0 ? DARK : LIGHT;

        setPreferredSize(new Dimension(pixelSize, pixelSize));
        setFont(new Font("Segoe UI Symbol", Font.PLAIN, 40)); // font with chess glyphs on Windows
        setFocusPainted(false);  // no focus outline around the piece
        setBorderPainted(false); // no button border, so squares sit flush
        setOpaque(true);         // needed for the background color to show
        setBackground(baseColor);
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
        updateBackground();
    }

    /**
     * Covers this square with fog or uncovers it (Fog of War mode).
     * The board panel is in charge of also hiding any piece on the square.
     *
     * @param hidden true to show the square as fog, false to show it normally
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
        updateBackground();
    }

    /**
     * @return true if this square is currently covered by fog
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Picks the background color: fog if hidden, yellow if selected, otherwise the normal square color.
     */
    private void updateBackground() {
        if (hidden) {
            setBackground(baseColor == DARK ? FOG_DARK : FOG_LIGHT);
        }
        else if (isSelected()) {
            setBackground(SELECTED);
        }
        else {
            setBackground(baseColor);
        }
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
