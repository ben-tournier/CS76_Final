package ui;

import java.util.function.IntConsumer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * The window that shows the start menu.
 * <p>
 * When the player clicks "Play Classic", this window closes and the game
 * action passed in from {@code Main} runs (which sets up the board and opens
 * the {@link ChessFrame}).
 */
public class StartMenuFrame extends JFrame {

    /** What to run when "Play Classic" is clicked (sets up and opens the game). */
    private final Runnable onPlayClassic;

    /** What to run when "Custom Board" is clicked. Takes the board size the user picked. */
    private final IntConsumer onPlayCustom;

    /** Smallest and largest board sizes the user can pick for a custom board. */
    public static final int MIN_CUSTOM_SIZE = 8;
    public static final int MAX_CUSTOM_SIZE = 16;

    /**
     * Creates the menu window and centers it on screen. The window is not
     * shown until {@code setVisible(true)} is called.
     *
     * @param onPlayClassic what to run when "Play Classic" is clicked
     * @param onPlayCustom  what to run when "Custom Board" is clicked, given the chosen size
     */
    public StartMenuFrame(Runnable onPlayClassic, IntConsumer onPlayCustom) {
        super("CS 67 Chess - Start Menu");
        this.onPlayClassic = onPlayClassic;
        this.onPlayCustom = onPlayCustom;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // keep the grid of tiles evenly sized

        // The menu panel is told what Play and Quit should do. It does not
        // need to know anything about the board.
        add(new StartMenuPanel(this::startClassicGame, this::startCustomGame, this::dispose));

        pack();                      // size the window to fit the menu
        setLocationRelativeTo(null); // center on screen
    }

    /**
     * Closes the menu and starts a classic game.
     * <p>
     * Later, other game modes can get their own methods like this one
     * (e.g. {@code startFogOfWarGame()}) that set up the board differently.
     */
    private void startClassicGame() {
        onPlayClassic.run(); // opens the board window first so the program keeps running
        dispose();           // then close the menu window
    }

    /**
     * Asks the user for a board size, then closes the menu and starts a game on
     * a board of that size. Cancelling the dialog leaves the menu open.
     */
    private void startCustomGame() {
        Integer[] sizes = new Integer[MAX_CUSTOM_SIZE - MIN_CUSTOM_SIZE + 1];
        for (int i = 0; i < sizes.length; i++) {
            sizes[i] = MIN_CUSTOM_SIZE + i;
        }

        Integer chosenSize = (Integer) JOptionPane.showInputDialog(this,
                "Board size (rows and columns):", "Custom Board",
                JOptionPane.QUESTION_MESSAGE, null, sizes, sizes[0]);

        if (chosenSize == null) {
            return; // user cancelled
        }

        onPlayCustom.accept(chosenSize);
        dispose();
    }
}
