package gameClasses;

import java.util.ArrayList;
import gameClasses.pieces.*;

public class testingSpace {
    public static void main() {
        // testQueen();
        // testKnight();

        // testCheck();
        testGameOver();
    }

    public static void printTiles(ArrayList<int[]> tilesPrinting) {
        for (int[] tile : tilesPrinting) {
            System.out.println((tile[0]) + ", " + (tile[1]));
        }
    }

    // Tests that the queen sees the correct spaces
    public static void testQueen() {
        board gameBoard = new board(8);
        queen queen = new queen(1, 1);

        player player1 = new player(true);
        player player2 = new player(false);

        gameBoard.spawnPiece(queen, player1, 1, 1);

        printTiles(queen.getVisibleTiles(gameBoard.getBoardState()));

        System.out.println();

        gameBoard.movePiece(player2, queen, 5, 5);

        printTiles(queen.getVisibleTiles(gameBoard.getBoardState()));
    }

    // Tests that the knight sees the correct spaces
    public static void testKnight() {
        board gameBoard = new board(8);
        knight knight = new knight(1, 1);

        player player1 = new player(true);
        player player2 = new player(false);

        gameBoard.spawnPiece(knight, player1, 1, 1);

        printTiles(knight.getVisibleTiles(gameBoard.getBoardState()));

        System.out.println();

        gameBoard.movePiece(player2, knight, 5, 5);

        printTiles(knight.getVisibleTiles(gameBoard.getBoardState()));
    }

    // Tests that it is possible to correctly see when a piece is in check
    public static void testCheck() {
        board gameBoard = new board(8);

        player player1 = new player(true);
        player player2 = new player(false);

        bishop bishop = new bishop(1, 1);
        king king = new king(player2.controlsWhite(), 8, 8);

        gameBoard.spawnPiece(bishop, player1, 1, 1);
        gameBoard.spawnPiece(king, player2, 8, 8);

        System.out.println(gameBoard.playerIsInCheck(player1, player2));
    }

    // Testing to see if the game can correctly identify if the game is over.
    public static void testGameOver() {
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

        System.out.println(gameBoard.isGameOver(player1, player2));
    }
}
