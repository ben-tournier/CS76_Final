package ui;

// Holds the choices made on the options screen.
// The start menu keeps one of these and passes it to the game when it starts.
public class GameOptions {

    // The color the person wants to play as against the computer.
    public enum ColorChoice {WHITE, BLACK, RANDOM}

    // Who the person is playing against.
    public enum ModeChoice {TWO_PLAYER, VS_COMPUTER}

    // vs Computer is the default mode, and the person plays white unless they pick something else.
    private ColorChoice colorChoice = ColorChoice.WHITE;
    private ModeChoice modeChoice = ModeChoice.VS_COMPUTER;

    public ColorChoice getColorChoice() {return colorChoice;}
    public ModeChoice getModeChoice() {return modeChoice;}

    public void setColorChoice(ColorChoice newColorChoice) {colorChoice = newColorChoice;}
    public void setModeChoice(ModeChoice newModeChoice) {modeChoice = newModeChoice;}
}
