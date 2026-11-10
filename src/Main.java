import javax.swing.SwingUtilities;

import ui.ChessFrame;

// Entry point: starts the program.
public class Main {
    public static void main(String[] args) {
        // Swing UI should be created on the Event Dispatch Thread.
        SwingUtilities.invokeLater(() -> {
            ChessFrame frame = new ChessFrame();
            frame.setVisible(true);
        });
    }
}
