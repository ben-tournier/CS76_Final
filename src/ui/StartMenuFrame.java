package ui;

import javax.swing.JFrame;

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

    /**
     * Creates the menu window and centers it on screen. The window is not
     * shown until {@code setVisible(true)} is called.
     *
     * @param onPlayClassic what to run when "Play Classic" is clicked
     */
    public StartMenuFrame(Runnable onPlayClassic) {
        super("CS 67 Chess - Start Menu");
        this.onPlayClassic = onPlayClassic;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // keep the grid of tiles evenly sized

        // The menu panel is told what Play and Quit should do. It does not
        // need to know anything about the board.
        add(new StartMenuPanel(this::startClassicGame, this::dispose));

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
}
