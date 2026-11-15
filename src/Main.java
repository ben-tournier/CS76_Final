import javax.swing.SwingUtilities;

import ui.ChessFrame;
import ui.StartMenuFrame;
import gameClasses.*;
import gameClasses.pieces.*;

// Entry point: starts the program.
public class Main {

    public static void main(String[] args) {
        // Swing UI should be created on the Event Dispatch Thread.
        // The start menu shows first; clicking "Play Classic" calls startClassicGame().
        SwingUtilities.invokeLater(() -> {
            StartMenuFrame menu = new StartMenuFrame(Main::startClassicGame);
            menu.setVisible(true);
        });
    }

    // Sets up the board and players, then opens the chess board window.
    private static void startClassicGame() {
        board gameBoard = new board(8);
        player player1 = new player(true);
        player player2 = new player(false);

        // rook enemyRook1 = new rook(2, 1);
        // rook enemyRook2 = new rook(1, 1);
        // queen enemyQueen = new queen(3, 2);
        // knight enemyKnight = new knight(3, 3);
        // pawn enemyPawn = new pawn(4, 4);

        // bishop friendlyBishop = new bishop(1, 7);
        // king friendlyKing = new king(player2.controlsWhite(), 1, 8);
        // knight friendlyKnight = new knight(5, 5);

        // gameBoard.spawnPiece(enemyRook1, player1, enemyRook1.getPosition());
        // gameBoard.spawnPiece(enemyRook2, player1, enemyRook2.getPosition());
        // gameBoard.spawnPiece(enemyQueen, player1, enemyQueen.getPosition());
        // gameBoard.spawnPiece(enemyKnight, player1, enemyKnight.getPosition());
        // gameBoard.spawnPiece(enemyPawn, player1, enemyPawn.getPosition());

        // gameBoard.spawnPiece(friendlyBishop, player2, friendlyBishop.getPosition());
        // gameBoard.spawnPiece(friendlyKing, player2, friendlyKing.getPosition());
        // gameBoard.spawnPiece(friendlyKnight, player2, friendlyKnight.getPosition());

        king castlingKing = new king(1, 5);
        castlingKing.connectOpponent(player2);
        rook castlingRook1 = new rook(1, 1);
        rook castlingRook2 = new rook(1, 8);
        pawn doubleMovingPawn = new pawn(2, 8);

        gameBoard.spawnPiece(castlingKing, player1, castlingKing.getPosition());
        gameBoard.spawnPiece(castlingRook1, player1, castlingRook1.getPosition());
        gameBoard.spawnPiece(castlingRook2, player1, castlingRook2.getPosition());
        gameBoard.spawnPiece(doubleMovingPawn, player1, doubleMovingPawn.getPosition());

        bishop defendingBishop = new bishop(3, 2);
        pawn enPassantPawn = new pawn(4, 7);
        enPassantPawn.setAllowEnPassant(true);
        gameBoard.spawnPiece(defendingBishop, player2, defendingBishop.getPosition());
        gameBoard.spawnPiece(enPassantPawn, player2, enPassantPawn.getPosition());

        ChessFrame frame = new ChessFrame();
        frame.setVisible(true);
        frame.loadState(gameBoard.getBoardState());
        frame.getBoardPanel().connectBoard(gameBoard);
        frame.getBoardPanel().setPlayers(player1, player2);
    }
}
