package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

// A banner across the top of a Fog of War window that tells the player what to do next.
// It shows a big title, a line of detail underneath, and optionally a button (e.g. "End Turn").
public class FogPromptBanner extends JPanel {

    // Colors match the start menu so the prompts fit the look of the game.
    private static final Color CALM_BACKGROUND = new Color(60, 45, 35); 
    private static final Color CALM_TEXT = new Color(240, 217, 181);
    private static final Color ACTION_BACKGROUND = new Color(246, 200, 80);
    private static final Color ACTION_TEXT = new Color(40, 30, 20);

    private static final int BANNER_HEIGHT = 64;

    private JLabel titleLabel = new JLabel();
    private JLabel detailLabel = new JLabel();
    private JButton actionButton = new JButton();
    private Runnable buttonAction; // what the button does when clicked

    public FogPromptBanner() {
        setLayout(new BorderLayout(10, 0));
        setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));

        // The width is set by the board under it, so the banner only asks for its height.
        setPreferredSize(new Dimension(1, BANNER_HEIGHT));

        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        detailLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4)); // a little room so the last letter is never cut off

        JPanel text = new JPanel(new BorderLayout());
        text.setOpaque(false);
        text.add(titleLabel, BorderLayout.CENTER);
        text.add(detailLabel, BorderLayout.SOUTH);
        add(text, BorderLayout.CENTER);

        actionButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        actionButton.setFocusPainted(false);
        actionButton.addActionListener(e -> {
            if (buttonAction != null) {
                buttonAction.run();
            }
        });
        add(actionButton, BorderLayout.EAST);

        showCalm("", "");
    }

    // Shows a message that does not need the player to do anything.
    public void showCalm(String title, String detail) {
        setText(title, detail, CALM_BACKGROUND, CALM_TEXT);
        buttonAction = null;
        actionButton.setVisible(false);
    }

    // Shows a highlighted message with a button the player needs to click.
    public void showAction(String title, String detail, String buttonText, Runnable action) {
        setText(title, detail, ACTION_BACKGROUND, ACTION_TEXT);
        buttonAction = action;
        actionButton.setText(buttonText);
        actionButton.setVisible(true);
    }

    // Shows a highlighted message without a button (e.g. when the button is somewhere else on screen).
    public void showAction(String title, String detail) {
        setText(title, detail, ACTION_BACKGROUND, ACTION_TEXT);
        buttonAction = null;
        actionButton.setVisible(false);
    }

    private void setText(String title, String detail, Color background, Color textColor) {
        titleLabel.setText(title);
        detailLabel.setText(detail);
        titleLabel.setForeground(textColor);
        detailLabel.setForeground(textColor);
        setBackground(background);
    }
}
