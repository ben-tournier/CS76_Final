package src.gameClasses.pieces;

import java.util.ArrayList;

public class queen extends piece {

    public queen(int x, int y) {
        setPosition(x, y);
    }

    // See abstract class for function description.
    public ArrayList<int[]> getVisibleTiles(piece[][] gameBoard) {
        ArrayList<int[]> validMovements = new ArrayList<int[]>();

        int boardRows = gameBoard.length;
        int boardColumns = gameBoard[0].length;

        int xPosition = getX();
        int yPosition = getY();

        // Iterating through each of the possible directions the queen can move.
        for (int xDirection=-1; xDirection<=1; xDirection++) {
            for (int yDirection=-1; yDirection<=1; yDirection++) {

                // Skipping the combination representing the queen not moving.
                if (xDirection == 0 && yDirection == 0) {
                    continue;
                }

                int directionScalar = 1; // Represents how far the queen is checking

                // Indefinitely iterating through the next tiles in the direction.
                // The stopping conditions are running out of board space or hitting another game piece.
                while (true) {
                    int xChecking = xPosition + xDirection * directionScalar;
                    int yChecking = yPosition + yDirection * directionScalar;

                    // Skipping combinations where the king would move off the board.
                    if (xChecking < 1 || xChecking > boardColumns || yChecking < 1 || yChecking > boardRows) {
                        break;
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
                        break;
                    }

                    // If the board tile contains a friendly piece
                    else if (this.pieceIsWhite == tileChecking.pieceIsWhite) {
                        break;
                    }

                    directionScalar++;
                }
            }
        }

        return validMovements;
    }

    public queen copyPiece() {return new queen(getX(), getY());}
}
