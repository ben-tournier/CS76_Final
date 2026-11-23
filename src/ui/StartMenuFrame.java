package ui;

import java.awt.CardLayout;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The window that shows the start menu.
 * <p>
 * When the player clicks "Play Classic", this window closes and the game
 * action passed in from {@code Main} runs (which sets up the board and opens
 * the board windows).
 */
public class StartMenuFrame extends JFrame {

    /** What to run when "Play Classic" is clicked (sets up and opens the game). Takes the chosen options. */
    private final Consumer<GameOptions> onPlayClassic;

    /** What to run when a custom game is started. Takes the board size the user picked and the chosen options. */
    private final BiConsumer<Integer, GameOptions> onPlayCustom;

    // Names for the screens this window can show.
    private static final String MENU_SCREEN = "menu";
    private static final String OPTIONS_SCREEN = "options";
    private static final String CUSTOM_BOARD_SCREEN = "custom board";

    // The choices made on the options screen. Passed to the game when it starts.
    private final GameOptions options = new GameOptions();

    // A CardLayout shows one screen at a time, so the screens can be swapped in the same window.
    private final CardLayout screens = new CardLayout();
    private final JPanel screenHolder = new JPanel(screens);

    /**
     * Creates the menu window and centers it on screen. The window is not
     * shown until {@code setVisible(true)} is called.
     *
     * @param onPlayClassic what to run when "Play Classic" is clicked, given the chosen options
     * @param onPlayCustom  what to run when a custom game is started, given the chosen size and options
     */
    public StartMenuFrame(Consumer<GameOptions> onPlayClassic, BiConsumer<Integer, GameOptions> onPlayCustom) {
        super("CS 67 Chess - Start Menu");
        this.onPlayClassic = onPlayClassic;
        this.onPlayCustom = onPlayCustom;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // keep the grid of tiles evenly sized

        // The menu panel is told what each tile should do. It does not
        // need to know anything about the board.
        screenHolder.add(new StartMenuPanel(this::startClassicGame, this::showCustomBoard, this::showOptions, this::dispose), MENU_SCREEN);
        screenHolder.add(new OptionsPanel(options, this::showMenu), OPTIONS_SCREEN);
        screenHolder.add(new CustomBoardPanel(this::startCustomGame, this::showMenu), CUSTOM_BOARD_SCREEN);
        add(screenHolder);

        pack();                      // size the window to fit the menu
        setLocationRelativeTo(null); // center on screen
    }

    // Switches the window to the options screen.
    private void showOptions() {
        screens.show(screenHolder, OPTIONS_SCREEN);
    }

    // Switches the window to the custom board screen.
    private void showCustomBoard() {
        screens.show(screenHolder, CUSTOM_BOARD_SCREEN);
    }

    // Switches the window back to the start menu.
    private void showMenu() {
        screens.show(screenHolder, MENU_SCREEN);
    }

    /**
     * Closes the menu and starts a classic game.
     * <p>
     * Later, other game modes can get their own methods like this one
     * (e.g. {@code startFogOfWarGame()}) that set up the board differently.
     */
    private void startClassicGame() {
        onPlayClassic.accept(options); // opens the board window first so the program keeps running
        dispose();                     // then close the menu window
    }

    // Closes the menu and starts a game on a board of the size picked on the custom board screen.
    private void startCustomGame(int boardSize) {
        onPlayCustom.accept(boardSize, options);
        dispose();
    }
}
