package ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import gameClasses.pieces.*;;

/**
 * The main window. Holds the board in the center and the status bar
 * along the bottom.
 */
public class ChessFrame extends JFrame {

    private final StatusPanel statusPanel;
    private final BoardPanel boardPanel;

    /**
     * Creates the window, adds the board and status bar, and centers it on
     * screen. The window is not shown until {@code setVisible(true)} is called.
     */
    public ChessFrame() {
        super("CS 67 Chess");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // The status panel is created first so the board can send it messages.
        statusPanel = new StatusPanel();
        boardPanel = new BoardPanel(statusPanel);

        add(boardPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

        pack();                       // size the window to fit its contents
        setLocationRelativeTo(null);  // center on screen
    }

    /**
     * @return the board panel shown in this window
     */
    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    /**
     * @return the status bar shown under the board
     */
    public StatusPanel getStatusPanel() {
        return statusPanel;
    }

    public void loadState(piece[][] boardState) {
        boardPanel.setUpStartingPieces(boardState);
    }
}
