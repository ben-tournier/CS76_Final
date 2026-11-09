package src.gameClasses;

import java.util.ArrayList;
import src.gameClasses.pieces.*;

public class testingSpace {
    public static void main() {
        // testQueen();
        testKnight();
    }

    public static void printTiles(ArrayList<int[]> tilesPrinting) {
        for (int[] tile : tilesPrinting) {
            System.out.println((tile[0] + 1) + ", " + (tile[1] + 1));
        }
    }

    // Tests that the queen sees the correct spaces
    public static void testQueen() {
        board gameBoard = new board(8);
        queen queen = new queen(1, 1);

        gameBoard.placePiece(queen, 1, 1);

        printTiles(queen.getVisibleTiles(gameBoard.getBoard()));

        System.out.println();

        gameBoard.movePiece(queen, 5, 5);

        printTiles(queen.getVisibleTiles(gameBoard.getBoard()));
    }

    // Tests that the knight sees the correct spaces
    public static void testKnight() {
        board gameBoard = new board(8);
        knight knight = new knight(1, 1);

        gameBoard.placePiece(knight, 1, 1);

        printTiles(knight.getVisibleTiles(gameBoard.getBoard()));

        System.out.println();

        gameBoard.movePiece(knight, 5, 5);

        printTiles(knight.getVisibleTiles(gameBoard.getBoard()));
    }
}
