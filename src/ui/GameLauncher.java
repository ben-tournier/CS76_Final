package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import gameClasses.board;
import gameClasses.computerOpponent;
import gameClasses.game;
import gameClasses.player;

// Asks which mode to play and opens the board windows for it.
//     Two Player: two windows side by side, one for white and one (flipped) for black.
//     vs Computer: one window for white, black is moved by a computerOpponent.
// Main calls startGame after it has set up the board and players.
public class GameLauncher {

    // How long the computer waits before moving, in milliseconds, so its move is not instant.
    private static final int COMPUTER_DELAY = 600;

    // Space between the two windows in two player mode, in pixels.
    private static final int WINDOW_GAP = 20;

    public static void startGame(board gameBoard, player firstPlayer, player secondPlayer) {
        String[] options = {"Two Player", "vs Computer"};

        int choice = JOptionPane.showOptionDialog(null, "How do you want to play?", "Choose a Mode",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        // Closing the dialog without picking cancels the game.
        if (choice == JOptionPane.CLOSED_OPTION) {
            return;
        }

        game currentGame = new game(gameBoard, firstPlayer, secondPlayer);

        if (choice == 0) {
            startTwoPlayer(currentGame);
        }
        else {
            startVsComputer(currentGame);
        }
    }

    // Opens a window for each player next to each other.
    private static void startTwoPlayer(game currentGame) {
        JFrame whiteWindow = createWindow(currentGame, currentGame.getWhitePlayer(), false);
        JFrame blackWindow = createWindow(currentGame, currentGame.getBlackPlayer(), true);

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

    // Opens one window for the person playing white and lets the computer play black.
    private static void startVsComputer(game currentGame) {
        JFrame humanWindow = createWindow(currentGame, currentGame.getWhitePlayer(), false);
        humanWindow.setLocationRelativeTo(null);

        computerOpponent computer = new computerOpponent(currentGame, currentGame.getBlackPlayer());

        // After every move, if it is now the computer's turn, wait a moment and then let it move.
        // A Timer is used instead of Thread.sleep so the window does not freeze while waiting.
        currentGame.addChangeListener(() -> {
            if (currentGame.isOver() == false && computer.controls(currentGame.getCurrentPlayer())) {
                Timer computerTimer = new Timer(COMPUTER_DELAY, e -> computer.takeTurn());
                computerTimer.setRepeats(false);
                computerTimer.start();
            }
        });

        humanWindow.setVisible(true);
        refreshWindow(humanWindow);
    }

    // Builds a window holding a board for the passed player and a status bar underneath.
    private static JFrame createWindow(game currentGame, player owner, boolean flipped) {
        JFrame window = new JFrame("CS 67 Chess - " + currentGame.getColorName(owner));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());

        StatusPanel statusPanel = new StatusPanel();
        PlayerBoardPanel boardPanel = new PlayerBoardPanel(statusPanel, currentGame, owner, flipped);

        window.add(boardPanel, BorderLayout.CENTER);
        window.add(statusPanel, BorderLayout.SOUTH);
        window.pack();

        return window;
    }

    // Finds the board inside the window and redraws it.
    private static void refreshWindow(JFrame window) {
        BorderLayout layout = (BorderLayout) window.getContentPane().getLayout();
        PlayerBoardPanel boardPanel = (PlayerBoardPanel) layout.getLayoutComponent(BorderLayout.CENTER);
        boardPanel.refreshBoard();
    }
}
