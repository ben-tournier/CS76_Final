import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

// Acknowledgements
// Claude was used to create this demo showing how javax.swing can be used.
// Included is a button that increments a counter as well as a simple tic-tac-toe game.

public final class SwingDemo {
    private static final int SIZE = 3;

    private int clickCount;
    private JLabel statusLabel;
    private JButton actionButton;

    // Tic-tac-toe state
    private final JButton[][] cells = new JButton[SIZE][SIZE];
    private JLabel gameLabel;
    private char currentPlayer = 'X';
    private int movesMade;
    private boolean gameOver;

    // Image shown on the losing player's tiles (null if it couldn't be loaded)
    private final BufferedImage clownImage = loadClownImage();

    private static BufferedImage loadClownImage() {
        try {
            return ImageIO.read(new File("image.png"));
        } catch (IOException e) {
            System.err.println("Could not load image.png: " + e.getMessage());
            return null;
        }
    }

    private void showWindow() {
        JFrame frame = new JFrame("Swing Button, Panel, and Menu Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        statusLabel = new JLabel("Click the button to get started.", SwingConstants.CENTER);
        actionButton = new JButton("Click me");
        actionButton.addActionListener(event -> {
            clickCount++;
            statusLabel.setText("Button clicked " + clickCount + " time(s).");
        });

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(statusLabel, BorderLayout.CENTER);
        panel.add(actionButton, BorderLayout.SOUTH);

        JMenuBar menuBar = new JMenuBar();
        JMenu actionsMenu = new JMenu("Actions");

        JMenuItem resetItem = new JMenuItem("Reset");
        resetItem.addActionListener(event -> {
            clickCount = 0;
            statusLabel.setText("Counter reset.");
        });

        JMenuItem toggleItem = new JMenuItem("Disable button");
        toggleItem.addActionListener(event -> {
            boolean enabled = !actionButton.isEnabled();
            actionButton.setEnabled(enabled);
            toggleItem.setText(enabled ? "Disable button" : "Enable button");
            statusLabel.setText(enabled ? "Button enabled." : "Button disabled.");
        });

        JMenuItem newGameItem = new JMenuItem("New tic-tac-toe game");
        newGameItem.addActionListener(event -> resetGame());

        actionsMenu.add(resetItem);
        actionsMenu.add(toggleItem);
        actionsMenu.addSeparator();
        actionsMenu.add(newGameItem);
        menuBar.add(actionsMenu);

        frame.setJMenuBar(menuBar);
        frame.add(panel, BorderLayout.NORTH);
        frame.add(createGamePanel(), BorderLayout.CENTER);
        frame.setSize(360, 480);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createGamePanel() {
        JPanel grid = new JPanel(new GridLayout(SIZE, SIZE, 4, 4));
        Font cellFont = new Font(Font.SANS_SERIF, Font.BOLD, 48);
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                JButton cell = new JButton("");
                cell.setFont(cellFont);
                cell.setFocusPainted(false);
                final int r = row;
                final int c = col;
                cell.addActionListener(event -> handleMove(r, c));
                cells[row][col] = cell;
                grid.add(cell);
            }
        }

        gameLabel = new JLabel("", SwingConstants.CENTER);
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(event -> resetGame());

        JPanel bottom = new JPanel(new BorderLayout(5, 5));
        bottom.add(gameLabel, BorderLayout.CENTER);
        bottom.add(newGameButton, BorderLayout.EAST);

        JPanel gamePanel = new JPanel(new BorderLayout(10, 10));
        gamePanel.setBorder(BorderFactory.createTitledBorder("Tic-Tac-Toe"));
        gamePanel.add(grid, BorderLayout.CENTER);
        gamePanel.add(bottom, BorderLayout.SOUTH);

        resetGame();
        return gamePanel;
    }

    private void handleMove(int row, int col) {
        JButton cell = cells[row][col];
        if (gameOver || !cell.getText().isEmpty()) {
            return;
        }

        cell.setText(String.valueOf(currentPlayer));
        movesMade++;

        if (hasWon(currentPlayer)) {
            gameLabel.setText("Player " + currentPlayer + " wins!");
            endGame();
            clownLoser(currentPlayer == 'X' ? 'O' : 'X');
        } else if (movesMade == SIZE * SIZE) {
            gameLabel.setText("It's a draw!");
            endGame();
        } else {
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            gameLabel.setText("Player " + currentPlayer + "'s turn");
        }
    }

    private boolean hasWon(char player) {
        String p = String.valueOf(player);
        for (int i = 0; i < SIZE; i++) {
            if (lineMatches(p, i, 0, 0, 1) || lineMatches(p, 0, i, 1, 0)) {
                return true;
            }
        }
        return lineMatches(p, 0, 0, 1, 1) || lineMatches(p, 0, SIZE - 1, 1, -1);
    }

    /** Checks SIZE cells starting at (row, col) stepping by (dRow, dCol). */
    private boolean lineMatches(String p, int row, int col, int dRow, int dCol) {
        for (int i = 0; i < SIZE; i++) {
            if (!cells[row + i * dRow][col + i * dCol].getText().equals(p)) {
                return false;
            }
        }
        return true;
    }

    private void endGame() {
        gameOver = true;
        for (JButton[] row : cells) {
            for (JButton cell : row) {
                cell.setEnabled(false);
            }
        }
    }

    /** Replaces every tile owned by the losing player with the clown image. */
    private void clownLoser(char loser) {
        if (clownImage == null) {
            return;
        }
        String l = String.valueOf(loser);
        for (JButton[] row : cells) {
            for (JButton cell : row) {
                if (cell.getText().equals(l)) {
                    int side = Math.max(1, Math.min(cell.getWidth(), cell.getHeight()) - 10);
                    ImageIcon icon = new ImageIcon(
                            clownImage.getScaledInstance(side, side, Image.SCALE_SMOOTH));
                    cell.setText("");
                    cell.setIcon(icon);
                    // Tiles are disabled at game end; keep the clown in full color.
                    cell.setDisabledIcon(icon);
                }
            }
        }
    }

    private void resetGame() {
        currentPlayer = 'X';
        movesMade = 0;
        gameOver = false;
        for (JButton[] row : cells) {
            for (JButton cell : row) {
                cell.setText("");
                cell.setIcon(null);
                cell.setDisabledIcon(null);
                cell.setEnabled(true);
            }
        }
        gameLabel.setText("Player X's turn");
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new SwingDemo().showWindow());
    }
}
