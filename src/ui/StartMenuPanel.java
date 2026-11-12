package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * The start menu shown before the chess board.
 * <p>
 * Layout:
 * <pre>
 *   +-----------------------------------+
 *   |          CS 67 Chess (title)      |
 *   +-----------+-----------+-----------+
 *   |   Play    | Fog of War|  Custom   |
 *   |  Classic  |  (soon)   |  Board    |
 *   +-----------+-----------+-----------+
 *   |  Random   |  Options  |   Quit    |
 *   |  Effects  |  (soon)   |           |
 *   +-----------+-----------+-----------+
 * </pre>
 * The tiles are laid out in a grid and colored like board squares, so the
 * menu matches the look of the board.
 * <p>
 * Only "Play Classic" and "Quit" work right now. The other tiles are
 * placeholders for game modes and options from the README's
 * "Potential Changes" list. They can't be clicked and say "Coming soon".
 * <p>
 * This panel does NOT open the board itself. It runs the {@code onPlay}
 * action it is given, so whoever builds the menu decides what "Play" does.
 * That keeps the menu separate from the rest of the program.
 */
public class StartMenuPanel extends JPanel {

    /** Rows and columns in the grid of tiles. */
    private static final int GRID_ROWS = 2;
    private static final int GRID_COLS = 3;

    // Same colors as SquareButton, so the menu matches the board.
    // (Copied rather than shared so SquareButton did not need to change.)
    private static final Color LIGHT = new Color(240, 217, 181);
    private static final Color DARK = new Color(181, 136, 99);
    private static final Color BACKGROUND = new Color(60, 45, 35); // dark wood around the grid
    private static final Color PLACEHOLDER_TEXT = new Color(110, 85, 65); // dimmed text for "Coming soon" tiles

    /** Counts tiles as they are added, so they alternate light/dark like a board. */
    private int tileCount = 0;

    /**
     * Builds the menu: a title on top and a grid of tiles below it.
     *
     * @param onPlay what to do when "Play Classic" is clicked (e.g. open the board)
     * @param onQuit what to do when "Quit" is clicked (e.g. close the program)
     */
    public StartMenuPanel(Runnable onPlay, Runnable onQuit) {
        setLayout(new BorderLayout(0, 20)); // 20px gap between title and grid
        setBackground(BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // padding around the edges

        add(createTitle(), BorderLayout.NORTH);
        add(createTileGrid(onPlay, onQuit), BorderLayout.CENTER);
    }

    /**
     * Creates the title label at the top of the menu.
     *
     * @return the title label
     */
    private JLabel createTitle() {
        JLabel title = new JLabel("CS 67 Chess", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 44));
        title.setForeground(LIGHT);
        return title;
    }

    /**
     * Creates the grid of menu tiles.
     * <p>
     * To add a new game mode later: add a tile here, or enable one of the
     * placeholders by passing a real action instead of {@code null}.
     *
     * @param onPlay action for the "Play Classic" tile
     * @param onQuit action for the "Quit" tile
     * @return the panel holding all the tiles
     */
    private JPanel createTileGrid(Runnable onPlay, Runnable onQuit) {
        // GridLayout(rows, cols, hgap, vgap): the gaps show the background
        // between tiles, like lines between squares.
        JPanel grid = new JPanel(new GridLayout(GRID_ROWS, GRID_COLS, 8, 8));
        grid.setOpaque(false); // let the dark background show through the gaps

        // Tiles fill the grid left-to-right, top-to-bottom.
        // A null action means the tile is a "Coming soon" placeholder.
        grid.add(createTile("Play Classic", "Standard chess", onPlay));
        grid.add(createTile("Fog of War", "Coming soon", null));
        grid.add(createTile("Custom Board", "Coming soon", null));
        grid.add(createTile("Random Effects", "Coming soon", null));
        grid.add(createTile("Options", "Coming soon", null));
        grid.add(createTile("Quit", "Exit the game", onQuit));

        return grid;
    }

    /**
     * Creates one tile in the grid.
     *
     * @param title    the big text on the tile, e.g. "Play Classic"
     * @param subtitle the small text under the title, e.g. "Coming soon"
     * @param action   what to run when clicked, or {@code null} for a placeholder tile
     * @return the tile (a button if it has an action, otherwise a plain label)
     */
    private JComponent createTile(String title, String subtitle, Runnable action) {
        // Swing text supports simple HTML, which gives us two lines of
        // different sizes without building a custom component.
        String text = "<html><center>"
                + "<span style='font-size:16pt'><b>" + title + "</b></span><br>"
                + "<span style='font-size:10pt'>" + subtitle + "</span>"
                + "</center></html>";

        JComponent tile;
        if (action != null) {
            // A real option: a clickable button.
            JButton button = new JButton(text);
            button.setFocusPainted(false);  // no focus outline around the text
            button.setBorderPainted(false); // no button border, so tiles look like squares
            button.addActionListener(e -> action.run());
            tile = button;
        } else {
            // Placeholder: a label, so it can't be clicked. A disabled button
            // would also work, but Swing draws its text too faint to read.
            JLabel label = new JLabel(text, SwingConstants.CENTER);
            label.setForeground(PLACEHOLDER_TEXT);
            label.setToolTipText("Not available yet");
            tile = label;
        }

        // Alternate colors like a checkerboard: even tiles light, odd tiles dark.
        tile.setBackground(tileCount % 2 == 0 ? LIGHT : DARK);
        tileCount++;

        tile.setPreferredSize(new Dimension(180, 120));
        tile.setOpaque(true); // needed for the background color to show

        return tile;
    }
}
