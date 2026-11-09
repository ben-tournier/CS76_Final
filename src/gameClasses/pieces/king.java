package src.gameClasses.pieces;

import java.util.ArrayList;

public class king extends piece {

    public king(int x, int y) {
        setPosition(x, y);
    }

    // See abstract class for function description.
    public ArrayList<int[]> getVisibleTiles(piece[][] gameBoard) {
        ArrayList<int[]> validMovements = new ArrayList<int[]>();

        int boardRows = gameBoard.length;
        int boardColumns = gameBoard[0].length;

        int xPosition = getX();
        int yPosition = getY();

        // Iterating through each of the possible movements a king can make.
        for (int xMovement=-1; xMovement<=1; xMovement++) {
            for (int yMovement=-1; yMovement<=1; yMovement++) {

                // Skipping the combination representing the king not moving.
                if (xMovement == 0 && yMovement == 0) {
                    continue;
                }

                // The -1 converts the position to an index in the game board.
                int xChecking = xPosition + xMovement - 1;
                int yChecking = yPosition + yMovement - 1;

                // Skipping combinations where the king would move off the board.
                if (xChecking < 0 || xChecking >= boardColumns || yChecking < 0 || yChecking >= boardRows) {
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
        }

        return validMovements;
    }

    public king copyPiece() {return new king(getX(), getY());}
}
