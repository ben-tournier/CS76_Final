package src.gameClasses.pieces;

import java.util.ArrayList;

public class knight extends piece {

    // See abstract class for function description.
    public ArrayList<int[]> getVisibleTiles(piece[][] gameBoard) {
        ArrayList<int[]> validMovements = new ArrayList<int[]>();

        int boardRows = gameBoard.length;
        int boardColumns = gameBoard[0].length;

        int xPosition = getX();
        int yPosition = getY();

        int[][] movements = new int[8][2];
        movements[0] = createArray(-2, -1);
        movements[1] = createArray(-1, -2);
        movements[2] = createArray(1, 2);
        movements[3] = createArray(2, 1);
        movements[4] = createArray(2, -1);
        movements[5] = createArray(1, -2);
        movements[6] = createArray(-1, -2);
        movements[7] = createArray(-2, -1);

        // Iterating through each of the possible movements a knight can make.
        for (int[] movement : movements) {
            int xMovement = movement[0];
            int yMovement = movement[1];

            int xChecking = xPosition + xMovement;
            int yChecking = yPosition + yMovement;

            // Skipping combinations where the knight would move off the board.
            if (xChecking < 1 || xChecking > boardColumns || yChecking < 1 || yChecking > boardRows) {
                continue;
            }

            piece tileChecking = gameBoard[xChecking][yChecking];

            // If the board tile is empty.
            if (tileChecking == null) {
                int[] movementAdding = createArray(xChecking, yChecking);
                validMovements.add(movementAdding);
            }

            // If the board tile contains an opponent's piece.
            else if (this.pieceIsWhite != tileChecking.pieceIsWhite) {
                int[] movementAdding = createArray(xChecking, yChecking);
                validMovements.add(movementAdding);
            }
        }

        return validMovements;
    }
}
