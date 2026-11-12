package ui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
/*
HOW TO IMPLEMENT (in src/Main.java)
1. import ui.ChessFrame;  ->  import ui.StartMenuFrame;
2. new ChessFrame()       ->  new StartMenuFrame()   (and change the variable type to match)
*/



/**
 * The window that shows the start menu.
 * <p>
 * When the player clicks "Play Classic", this window closes and the normal
 * {@link ChessFrame} (the board) opens in its place.
 * <p>
 * How to run it:
 * <ul>
 *   <li>Right now: run the {@code main} method in this file. This shows the
 *       menu first, then the board.</li>
 *   <li>Later: to make the menu show every time, change the one line in
 *       {@code Main.java} from {@code new ChessFrame()} to
 *       {@code new StartMenuFrame()}. That edit was left out on purpose so
 *       this feature only adds new files.</li>
 * </ul>
 */
public class StartMenuFrame extends JFrame {

    /**
     * Creates the menu window and centers it on screen. The window is not
     * shown until {@code setVisible(true)} is called.
     */
    public StartMenuFrame() {
        super("CS 67 Chess - Start Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // keep the grid of tiles evenly sized

        // The menu panel is told what Play and Quit should do. It does not
        // need to know anything about the board.
        add(new StartMenuPanel(this::startClassicGame, this::dispose));

        pack();                      // size the window to fit the menu
        setLocationRelativeTo(null); // center on screen
    }

    /**
     * Closes the menu and opens the chess board.
     * <p>
     * Later, other game modes can get their own methods like this one
     * (e.g. {@code startFogOfWarGame()}) that set up the board differently.
     */
    private void startClassicGame() {
        ChessFrame game = new ChessFrame();
        game.setVisible(true);
        dispose(); // close the menu window; the board window keeps the program running
    }

    /**
     * Starts the program at the start menu instead of going straight to the board.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        // Swing UI should be created on the Event Dispatch Thread (same as Main.java).
        SwingUtilities.invokeLater(() -> {
            StartMenuFrame menu = new StartMenuFrame();
            menu.setVisible(true);
        });
    }
}
