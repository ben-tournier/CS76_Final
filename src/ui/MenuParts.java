package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

// Pieces shared by the menu screens (Options, Custom Board) so they all look the same.
// Uses the same colors as StartMenuPanel.
public class MenuParts {

    // Gives a screen the same spacing and background as the start menu.
    static void styleScreen(JPanel screen) {
        screen.setLayout(new BorderLayout(0, 20));
        screen.setBackground(StartMenuPanel.BACKGROUND);
        screen.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
    }

    // Makes a screen title with a Back button in the top right corner.
    static JPanel createTopBar(String titleText, Runnable onBack) {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JLabel title = new JLabel(titleText, SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 44));
        title.setForeground(StartMenuPanel.LIGHT);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Dialog", Font.BOLD, 16));
        backButton.setBackground(StartMenuPanel.LIGHT);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setOpaque(true);
        backButton.addActionListener(e -> onBack.run());

        // Putting the button at the top of its own panel so it sits in the corner instead of stretching down.
        JPanel backHolder = new JPanel(new BorderLayout());
        backHolder.setOpaque(false);
        backHolder.add(backButton, BorderLayout.NORTH);

        // An empty space the same width as the button on the left, so the title stays in the middle.
        Dimension buttonSize = backButton.getPreferredSize();
        topBar.add(Box.createRigidArea(new Dimension(buttonSize.width, 0)), BorderLayout.WEST);
        topBar.add(title, BorderLayout.CENTER);
        topBar.add(backHolder, BorderLayout.EAST);

        return topBar;
    }

    // Makes one row with its name on top and its radio buttons underneath.
    // The buttons are put in a ButtonGroup so only one in the row can be picked at a time.
    // buttonGap is the space between buttons, so rows with lots of buttons can still fit.
    static JPanel createRow(String name, Color background, int buttonGap, JRadioButton... buttons) {
        JPanel row = new JPanel(new GridLayout(2, 1));
        row.setBackground(background);
        row.setPreferredSize(new Dimension(180 * 3, 120));

        JLabel label = new JLabel(name, SwingConstants.CENTER);
        label.setFont(new Font("Dialog", Font.BOLD, 21));

        JPanel buttonHolder = new JPanel(new FlowLayout(FlowLayout.CENTER, buttonGap, 5));
        buttonHolder.setOpaque(false);

        ButtonGroup group = new ButtonGroup();

        for (JRadioButton button : buttons) {
            group.add(button);
            buttonHolder.add(button);
        }

        row.add(label);
        row.add(buttonHolder);

        return row;
    }

    // Makes one radio button. It is see through so the row's color shows behind it.
    static JRadioButton createRadioButton(String text, boolean startsSelected) {
        JRadioButton button = new JRadioButton(text, startsSelected);
        button.setFont(new Font("Dialog", Font.PLAIN, 16));
        button.setOpaque(false);
        button.setFocusPainted(false);
        return button;
    }
}
