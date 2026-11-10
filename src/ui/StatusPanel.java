package ui;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Bar under the board showing whose turn it is and the latest message.
 */
public class StatusPanel extends JPanel {

    private final JLabel turnLabel;    // e.g. "White's turn"
    private final JLabel messageLabel; // most recent message, e.g. "Selected e2"

    /**
     * Creates the status bar. White moves first, so it starts on White's turn
     * with no message.
     */
    public StatusPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        turnLabel = new JLabel("White's turn");
        messageLabel = new JLabel("");

        add(turnLabel);
        add(new JLabel(" | ")); // separator between the turn and the message
        add(messageLabel);
    }

    /**
     * Updates the turn display.
     *
     * @param color the player whose turn it is, e.g. "White" or "Black"
     */
    public void setTurn(String color) {
        turnLabel.setText(color + "'s turn");
    }

    /**
     * Replaces the current message with a new one.
     *
     * @param message the text to show
     */
    public void showMessage(String message) {
        messageLabel.setText(message);
    }
}
