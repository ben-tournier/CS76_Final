import javax.swing.SwingUtilities;

import ui.ChessFrame;
import gameClasses.*;
import gameClasses.pieces.*;

// Entry point: starts the program.
public class Main {
    
    public static void main(String[] args) {
        board gameBoard = new board(8);
        player player1 = new player(true);
        player player2 = new player(false);

        rook enemyRook1 = new rook(2, 1);
        rook enemyRook2 = new rook(1, 1);
        queen enemyQueen = new queen(3, 2);
        knight enemyKnight = new knight(3, 3);

        bishop friendlyBishop = new bishop(1, 7);
        king friendlyKing = new king(player2.controlsWhite(), 1, 8);

        gameBoard.spawnPiece(enemyRook1, player1, enemyRook1.getPosition());
        gameBoard.spawnPiece(enemyRook2, player1, enemyRook2.getPosition());
        gameBoard.spawnPiece(enemyQueen, player1, enemyQueen.getPosition());
        gameBoard.spawnPiece(enemyKnight, player1, enemyKnight.getPosition());
        gameBoard.spawnPiece(friendlyBishop, player2, friendlyBishop.getPosition());
        gameBoard.spawnPiece(friendlyKing, player2, friendlyKing.getPosition());
        
        ChessFrame frame = new ChessFrame();

        // Swing UI should be created on the Event Dispatch Thread.
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
            frame.loadState(gameBoard.getBoardState());
        });

        frame.getBoardPanel().connectBoard(gameBoard);
        frame.getBoardPanel().setPlayers(player1, player2);



    }
}
