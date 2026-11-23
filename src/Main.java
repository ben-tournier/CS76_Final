import javax.swing.SwingUtilities;

import ui.GameLauncher;
import ui.GameOptions;
import ui.StartMenuFrame;
import gameClasses.*;
import gameClasses.pieces.*;

// Entry point: starts the program.
public class Main {

    public static void main(String[] args) {
        // Swing UI should be created on the Event Dispatch Thread.
        // The start menu shows first; clicking "Play Classic" calls startClassicGame().
        SwingUtilities.invokeLater(() -> {
            StartMenuFrame menu = new StartMenuFrame(Main::startClassicGame, Main::startCustomGame, Main::startFogOfWarGame);
            menu.setVisible(true);
        });
    }

    // Classic mode always uses the standard 8x8 board.
    private static void startClassicGame(GameOptions options) {
        startGame(8, options, false);
    }

    // Custom board mode uses the size the user picked in the menu.
    private static void startCustomGame(int boardSize, GameOptions options) {
        startGame(boardSize, options, false);
    }

    // Fog of War mode: a standard 8x8 board where each player only sees what their pieces can move to.
    private static void startFogOfWarGame(GameOptions options) {
        startGame(8, options, true);
    }

    // Sets up a board of the passed size and the players, then opens the chess board window.
    // Used by classic mode (size 8), Fog of War (size 8) and the custom board mode (size chosen by the user).
    // The options hold the mode and color picked on the options screen. fogOfWar turns on Fog of War mode.
    private static void startGame(int boardSize, GameOptions options, boolean fogOfWar) {
        board gameBoard = new board(boardSize);
        player player1 = new player(true);
        player player2 = new player(false);

        for (int col=1; col<=8; col++) {
            pawn newWhitePawn = new pawn(2, col);
            pawn newBlackPawn = new pawn(7, col);

            gameBoard.spawnPiece(newWhitePawn, player1, newWhitePawn.getPosition());
            gameBoard.spawnPiece(newBlackPawn, player2, newBlackPawn.getPosition());

            if (col == 1 || col == 8) {
                rook newWhiteRook = new rook(1, col);
                rook newBlackRook = new rook(8, col);

                gameBoard.spawnPiece(newWhiteRook, player1, newWhiteRook.getPosition());
                gameBoard.spawnPiece(newBlackRook, player2, newBlackRook.getPosition());
            }

            if (col == 2 || col == 7) {
                knight newWhiteKnight = new knight(1, col);
                knight newBlackKnight = new knight(8, col);

                gameBoard.spawnPiece(newWhiteKnight, player1, newWhiteKnight.getPosition());
                gameBoard.spawnPiece(newBlackKnight, player2, newBlackKnight.getPosition());
            }

            if (col == 3 || col == 6) {
                bishop newWhiteBishop = new bishop(1, col);
                bishop newBlackBishop = new bishop(8, col);

                gameBoard.spawnPiece(newWhiteBishop, player1, newWhiteBishop.getPosition());
                gameBoard.spawnPiece(newBlackBishop, player2, newBlackBishop.getPosition());
            }

            if (col == 4) {
                queen newWhiteQueen = new queen(1, col);
                king newBlackKing = new king(8, col);
                newBlackKing.connectOpponent(player1);

                gameBoard.spawnPiece(newWhiteQueen, player1, newWhiteQueen.getPosition());
                gameBoard.spawnPiece(newBlackKing, player2, newBlackKing.getPosition());
            }

            if (col == 5) {
                king newWhiteKing = new king(1, col);
                newWhiteKing.connectOpponent(player2);
                queen newBlackQueen = new queen(8, col);

                gameBoard.spawnPiece(newWhiteKing, player1, newWhiteKing.getPosition());
                gameBoard.spawnPiece(newBlackQueen, player2, newBlackQueen.getPosition());
            }
        }
        // Opens the board windows for the mode picked on the options screen.
        GameLauncher.startGame(gameBoard, player1, player2, options, fogOfWar);
    }
}
