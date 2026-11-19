package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

// A card drawn in the middle of a covered board in two player Fog of War,
public class FogCoverCard extends JPanel {

    // Colors match the start menu so the prompts fit the look of the game.
    private static final Color CARD_BACKGROUND = new Color(60, 45, 35);
    private static final Color CARD_TEXT = new Color(240, 217, 181);

    private JLabel titleLabel = new JLabel();
    private JLabel detailLabel = new JLabel();
    private JButton actionButton = new JButton();
    private Runnable buttonAction; // what the button does when clicked

    public FogCoverCard() {
        // GridBagLayout with one child centers that child.
        setLayout(new GridBagLayout());
        setOpaque(false);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_TEXT, 2),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)));

        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(CARD_TEXT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4)); // a little room so the last letter is never cut off
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        detailLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        detailLabel.setForeground(CARD_TEXT);
        detailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        actionButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        actionButton.setFocusPainted(false);
        actionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        actionButton.addActionListener(e -> {
            if (buttonAction != null) {
                buttonAction.run();
            }
        });

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(detailLabel);
        card.add(Box.createVerticalStrut(16));
        card.add(actionButton);

        add(card);
        setVisible(false);
    }

    // Shows the card with a button the player needs to click.
    public void showCard(String title, String detail, String buttonText, Runnable action) {
        titleLabel.setText(title);
        detailLabel.setText(detail);
        buttonAction = action;
        actionButton.setText(buttonText);
        actionButton.setVisible(true);
        setVisible(true);
    }

    // Shows the card with just a message.
    public void showCard(String title, String detail) {
        titleLabel.setText(title);
        detailLabel.setText(detail);
        buttonAction = null;
        actionButton.setVisible(false);
        setVisible(true);
    }

    public void hideCard() {
        setVisible(false);
    }
}
