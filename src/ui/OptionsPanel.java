package ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JRadioButton;

import ui.GameOptions.ColorChoice;
import ui.GameOptions.ModeChoice;

// The options screen. It looks like the start menu, but holds settings instead of game modes.
//     Play as: White, Black, or Random.
//     Mode: Two Player or vs Computer.
// Every click is saved to the GameOptions object right away, so the Back button only has to go back to the menu.
public class OptionsPanel extends JPanel {
    private GameOptions options;

    // Kept so they can be turned off when Two Player is picked, since the color only matters against the computer.
    private JRadioButton whiteButton;
    private JRadioButton blackButton;
    private JRadioButton randomButton;

    public OptionsPanel(GameOptions options, Runnable onBack) {
        this.options = options;

        MenuParts.styleScreen(this);
        add(MenuParts.createTopBar("Options", onBack), BorderLayout.NORTH);
        add(createOptionRows(), BorderLayout.CENTER);
    }

    // Makes the two rows of choices, colored like board squares.
    private JPanel createOptionRows() {
        JPanel rows = new JPanel(new GridLayout(2, 1, 8, 8));
        rows.setOpaque(false);

        // Color row
        whiteButton = MenuParts.createRadioButton("White", options.getColorChoice() == ColorChoice.WHITE);
        blackButton = MenuParts.createRadioButton("Black", options.getColorChoice() == ColorChoice.BLACK);
        randomButton = MenuParts.createRadioButton("Random", options.getColorChoice() == ColorChoice.RANDOM);

        whiteButton.addActionListener(e -> options.setColorChoice(ColorChoice.WHITE));
        blackButton.addActionListener(e -> options.setColorChoice(ColorChoice.BLACK));
        randomButton.addActionListener(e -> options.setColorChoice(ColorChoice.RANDOM));

        // Mode row
        JRadioButton twoPlayerButton = MenuParts.createRadioButton("Two Player", options.getModeChoice() == ModeChoice.TWO_PLAYER);
        JRadioButton vsComputerButton = MenuParts.createRadioButton("vs Computer", options.getModeChoice() == ModeChoice.VS_COMPUTER);

        twoPlayerButton.addActionListener(e -> {
            options.setModeChoice(ModeChoice.TWO_PLAYER);
            setColorButtonsEnabled(false);
        });

        vsComputerButton.addActionListener(e -> {
            options.setModeChoice(ModeChoice.VS_COMPUTER);
            setColorButtonsEnabled(true);
        });

        // Matching the color buttons to the mode the screen starts on.
        setColorButtonsEnabled(options.getModeChoice() == ModeChoice.VS_COMPUTER);

        rows.add(MenuParts.createRow("Play as", StartMenuPanel.LIGHT, 25, whiteButton, blackButton, randomButton));
        rows.add(MenuParts.createRow("Mode", StartMenuPanel.DARK, 25, twoPlayerButton, vsComputerButton));

        return rows;
    }

    // Turns the color buttons on or off.
    private void setColorButtonsEnabled(boolean enabled) {
        whiteButton.setEnabled(enabled);
        blackButton.setEnabled(enabled);
        randomButton.setEnabled(enabled);
    }
}
