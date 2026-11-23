package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.Timer;

import gameClasses.board;
import gameClasses.computerOpponent;
import gameClasses.game;
import gameClasses.player;

// Opens the board windows for the mode picked on the options screen.
//     Two Player: two windows side by side, one for white and one (flipped) for black.
//     vs Computer: one window for the color picked on the options screen. The other color is moved by a computerOpponent.
// Main calls startGame after it has set up the board and players.
public class GameLauncher {

    // How long the computer waits before moving, in milliseconds, so its move is not instant.
    private static final int COMPUTER_DELAY = 600;

    // Space between the two windows in two player mode, in pixels.
    private static final int WINDOW_GAP = 20;

    // fogOfWar turns on Fog of War mode for the game.
    public static void startGame(board gameBoard, player firstPlayer, player secondPlayer, GameOptions options, boolean fogOfWar) {
        game currentGame = new game(gameBoard, firstPlayer, secondPlayer);
        currentGame.setFogOfWar(fogOfWar);

        if (options.getModeChoice() == GameOptions.ModeChoice.TWO_PLAYER) {
            startTwoPlayer(currentGame);
        }
        else {
            player humanPlayer = getHumanPlayer(currentGame, options.getColorChoice());
            startVsComputer(currentGame, humanPlayer);
        }
    }

    // Returns the player the person will control based on the color they picked.
    // Random picks white or black when the game starts, so it can be different each game.
    private static player getHumanPlayer(game currentGame, GameOptions.ColorChoice colorChoice) {
        boolean playAsWhite;

        if (colorChoice == GameOptions.ColorChoice.WHITE) {
            playAsWhite = true;
        }
        else if (colorChoice == GameOptions.ColorChoice.BLACK) {
            playAsWhite = false;
        }
        else {
            playAsWhite = new Random().nextBoolean();
        }

        if (playAsWhite) {
            return currentGame.getWhitePlayer();
        }

        return currentGame.getBlackPlayer();
    }

    // Opens a window for each player next to each other.
    private static void startTwoPlayer(game currentGame) {
        // In Fog of War both windows are on the same screen, so the two windows share a handoff that
        // keeps both boards covered between turns (see FogTurnHandoff). Without fog there is no handoff.
        FogTurnHandoff handoff = null;
        if (currentGame.isFogOfWar()) {
            handoff = new FogTurnHandoff(currentGame);
        }

        JFrame whiteWindow = createWindow(currentGame, currentGame.getWhitePlayer(), false, handoff);
        JFrame blackWindow = createWindow(currentGame, currentGame.getBlackPlayer(), true, handoff);

        // Placing the windows side by side in the middle of the screen.
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int totalWidth = whiteWindow.getWidth() + WINDOW_GAP + blackWindow.getWidth();
        int left = Math.max(0, (screenSize.width - totalWidth) / 2);
        int top = Math.max(0, (screenSize.height - whiteWindow.getHeight()) / 2);

        whiteWindow.setLocation(left, top);
        blackWindow.setLocation(left + whiteWindow.getWidth() + WINDOW_GAP, top);

        whiteWindow.setVisible(true);
        blackWindow.setVisible(true);

        // Drawing the pieces after the windows are on screen so the pieces are sized to the squares.
        refreshWindow(whiteWindow);
        refreshWindow(blackWindow);
    }

    // Opens one window for the person and lets the computer play the other color.
    // The board is flipped if the person is playing black, so their pieces are at the bottom.
    private static void startVsComputer(game currentGame, player humanPlayer) {
        player computerPlayer = currentGame.getOpponent(humanPlayer);
        boolean flipped = humanPlayer.controlsWhite() == false;

        JFrame humanWindow = createWindow(currentGame, humanPlayer, flipped, null);
        humanWindow.setLocationRelativeTo(null);

        computerOpponent computer = new computerOpponent(currentGame, computerPlayer, humanPlayer);

        // After every move, if it is now the computer's turn, let it move.
        currentGame.addChangeListener(() -> {
            if (currentGame.isOver() == false && computer.controls(currentGame.getCurrentPlayer())) {
                startComputerTurn(computer);
            }

            else if (currentGame.isOver() == true) {humanWindow.dispose();}
        });

        humanWindow.setVisible(true);
        refreshWindow(humanWindow);

        // If the computer is white it moves first, so its turn has to be started here.
        // Otherwise it would wait for a move that never comes.
        if (computer.controls(currentGame.getCurrentPlayer())) {
            startComputerTurn(computer);
        }
    }

    // Waits a moment and then lets the computer move.
    // A Timer is used instead of Thread.sleep so the window does not freeze while waiting.
    private static void startComputerTurn(computerOpponent computer) {
        Timer computerTimer = new Timer(COMPUTER_DELAY, e -> computer.takeTurn());
        computerTimer.setRepeats(false);
        computerTimer.start();
    }

    // Builds a window holding a board for the passed player and a status bar underneath.
    // handoff is shared by both windows in two player Fog of War, and null otherwise.
    private static JFrame createWindow(game currentGame, player owner, boolean flipped, FogTurnHandoff handoff) {
        String title = "CS 67 Chess - " + currentGame.getColorName(owner);
        if (currentGame.isFogOfWar()) {
            title += " (Fog of War)";
        }

        JFrame window = new JFrame(title);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());

        StatusPanel statusPanel = new StatusPanel();
        PlayerBoardPanel boardPanel = new PlayerBoardPanel(statusPanel, currentGame, owner, flipped, handoff);

        if (handoff == null) {
            window.add(boardPanel, BorderLayout.CENTER);
        }

        // Two player Fog of War: the prompt banner goes above the board, and the cover card is
        // stacked on top of the board so it shows in the middle of the board while it is covered.
        else {
            window.add(boardPanel.getPromptBanner(), BorderLayout.NORTH);

            // OverlayLayout stacks its children on top of each other. The first one added is drawn on top.
            // Optimized drawing is turned off so Swing knows the children overlap and redraws them correctly.
            JPanel boardArea = new JPanel() {
                @Override
                public boolean isOptimizedDrawingEnabled() {return false;}
            };
            boardArea.setLayout(new OverlayLayout(boardArea));
            boardArea.add(boardPanel.getCoverCard());
            boardArea.add(boardPanel);

            window.add(boardArea, BorderLayout.CENTER);
        }

        window.add(statusPanel, BorderLayout.SOUTH);
        window.pack();

        // Remembering the board so refreshWindow can find it, wherever it ended up in the window.
        window.getRootPane().putClientProperty(BOARD_PANEL_KEY, boardPanel);

        return window;
    }

    // Name used to store each window's board on the window (see createWindow).
    private static final String BOARD_PANEL_KEY = "boardPanel";

    // Finds the board inside the window and redraws it.
    private static void refreshWindow(JFrame window) {
        PlayerBoardPanel boardPanel = (PlayerBoardPanel) window.getRootPane().getClientProperty(BOARD_PANEL_KEY);
        boardPanel.refreshBoard();
    }
}
