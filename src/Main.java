import javax.swing.SwingUtilities;

import ui.ChessFrame;
import gameClasses.*;
import gameClasses.pieces.bishop;
import gameClasses.pieces.king;
import gameClasses.pieces.rook;

// Entry point: starts the program.
public class Main {
    
    public static void main(String[] args) {
        board gameBoard = new board(8);
        player player1 = new player(true);
        player player2 = new player(false);

        rook enemyRook1 = new rook(8, 1);
        rook enemyRook2 = new rook(7, 1);

        bishop friendlyBishop = new bishop(8, 7);
        king friendlyKing = new king(player2.controlsWhite(), 8, 8);

        gameBoard.spawnPiece(enemyRook1, player1, enemyRook1.getPosition());
        gameBoard.spawnPiece(enemyRook2, player1, enemyRook2.getPosition());
        gameBoard.spawnPiece(friendlyBishop, player2, friendlyBishop.getPosition());
        gameBoard.spawnPiece(friendlyKing, player2, friendlyKing.getPosition());
        
        // Swing UI should be created on the Event Dispatch Thread.
        SwingUtilities.invokeLater(() -> {
            ChessFrame frame = new ChessFrame();
            frame.setVisible(true);
            frame.loadState(gameBoard.getBoardState());
        });





    }
}
