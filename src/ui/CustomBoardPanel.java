package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.function.IntConsumer;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

// The custom board screen. It looks like the start menu.
//     Top row: radio buttons to pick the board size.
//     Bottom row: a Start Game button that starts a game on a board of that size.
// The game uses the mode and color picked on the options screen.
public class CustomBoardPanel extends JPanel {

    // Smallest and largest board sizes the user can pick.
    public static final int MIN_SIZE = 8;
    public static final int MAX_SIZE = 16;

    // The size picked right now. Starts on the smallest size.
    private int chosenSize = MIN_SIZE;

    // onStart is given the chosen size when Start Game is clicked.
    public CustomBoardPanel(IntConsumer onStart, Runnable onBack) {
        MenuParts.styleScreen(this);

        JPanel rows = new JPanel(new GridLayout(2, 1, 8, 8));
        rows.setOpaque(false);
        rows.add(createSizeRow());
        rows.add(createStartButton(onStart));

        add(MenuParts.createTopBar("Custom Board", onBack), BorderLayout.NORTH);
        add(rows, BorderLayout.CENTER);
    }

    // Makes the row of radio buttons, one for each board size.
    private JPanel createSizeRow() {
        JRadioButton[] sizeButtons = new JRadioButton[MAX_SIZE - MIN_SIZE + 1];

        for (int i = 0; i < sizeButtons.length; i++) {
            int size = MIN_SIZE + i;

            sizeButtons[i] = MenuParts.createRadioButton("" + size, size == chosenSize);
            sizeButtons[i].addActionListener(e -> chosenSize = size);
        }

        // A smaller gap than the options screen so all the sizes fit on one line.
        return MenuParts.createRow("Board size (rows and columns)", StartMenuPanel.LIGHT, 8, sizeButtons);
    }

    // Makes the Start Game button. It is styled like a tile on the start menu.
    private JButton createStartButton(IntConsumer onStart) {
        String text = "<html><center>"
                + "<span style='font-size:16pt'><b>Start Game</b></span><br>"
                + "<span style='font-size:10pt'>Uses the mode and color from Options</span>"
                + "</center></html>";

        JButton startButton = new JButton(text);
        startButton.setBackground(StartMenuPanel.DARK);
        startButton.setFocusPainted(false);
        startButton.setBorderPainted(false);
        startButton.setOpaque(true);
        startButton.setPreferredSize(new Dimension(180 * 3, 120));
        startButton.addActionListener(e -> onStart.accept(chosenSize));

        return startButton;
    }
}
