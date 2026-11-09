package src.gameClasses.pieces;

import java.util.ArrayList;

public class knight extends piece {

    public knight(int row, int col) {
        setPosition(row, col);
    }

    // See abstract class for function description.
    public ArrayList<int[]> getVisibleTiles(piece[][] gameBoard) {
        ArrayList<int[]> validMovements = new ArrayList<int[]>();

        int boardRows = gameBoard.length;
        int boardColumns = gameBoard[0].length;

        int row = getRow();
        int col = getCol();

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
            int rowMovement = movement[0];
            int colMovement = movement[1];

            // The -1 converts the position to an index in the game board.
            int rowChecking = row + rowMovement - 1;
            int colChecking = col + colMovement - 1;

            // Skipping combinations where the knight would move off the board.
            if (rowChecking < 0 || rowChecking >= boardColumns || colChecking < 0 || colChecking >= boardRows) {
                continue;
            }

            piece tileChecking = gameBoard[rowChecking][colChecking];

            // If the board tile is empty.
            if (tileChecking == null) {
                int[] movementAdding = createArray(rowChecking, colChecking);
                validMovements.add(movementAdding);
            }

            // If the board tile contains an opponent's piece.
            else if (this.pieceIsWhite != tileChecking.pieceIsWhite) {
                int[] movementAdding = createArray(rowChecking, colChecking);
                validMovements.add(movementAdding);
            }
        }

        return validMovements;
    }

    public knight copyPiece() {return new knight(getRow(), getCol());}
}
