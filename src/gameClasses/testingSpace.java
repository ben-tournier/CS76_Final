package src.gameClasses;

import java.util.ArrayList;
import src.gameClasses.pieces.*;

public class testingSpace {
    public static void main() {
        test1();
    }

    public static void printTiles(ArrayList<int[]> tilesPrinting) {
        for (int[] tile : tilesPrinting) {
            System.out.println((tile[0] + 1) + ", " + (tile[1] + 1));
        }
    }

    // Tests that the queen sees the correct spaces
    public static void test1() {
        board gameBoard = new board(8);
        queen whiteQueen = new queen(1, 1);

        gameBoard.placePiece(whiteQueen, 1, 1);

        printTiles(whiteQueen.getVisibleTiles(gameBoard.getBoard()));

        System.out.println();

        gameBoard.movePiece(whiteQueen, 5, 5);

        printTiles(whiteQueen.getVisibleTiles(gameBoard.getBoard()));
    }
}
