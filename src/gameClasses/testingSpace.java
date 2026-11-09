package src.gameClasses;

import java.util.ArrayList;
import src.gameClasses.pieces.*;

public class testingSpace {
    public static void main() {
        board gameBoard = new board(8);

        queen whiteQueen = new queen(1, 1);

        gameBoard.placePiece(whiteQueen, 1, 1);

        ArrayList<int[]> visibleTiles = whiteQueen.getVisibleTiles(gameBoard.getBoard());
        System.out.println(visibleTiles);

        for (int[] tile : visibleTiles) {
            System.out.println(tile[0] + ", " + tile[1]);
        }
    }
}
