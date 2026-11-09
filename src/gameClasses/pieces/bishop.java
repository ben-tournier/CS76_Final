package src.gameClasses.pieces;

import java.util.ArrayList;

public class bishop extends piece {

    public bishop(int x, int y) {
        setPosition(x, y);
    }

    // See abstract class for function description.
    public ArrayList<int[]> getVisibleTiles(piece[][] gameBoard) {
        ArrayList<int[]> validMovements = new ArrayList<int[]>();

        int boardRows = gameBoard.length;
        int boardColumns = gameBoard[0].length;

        int xPosition = getX();
        int yPosition = getY();

        int[] xDirections = createArray(-1, 1);
        int[] yDirections = createArray(-1, 1);

        // Iterating through each of the possible directions the bishop can use.
        for (int xDirection : xDirections) {
            for (int yDirection : yDirections) {

                int directionScalar = 1; // Represents how far the bishop is checking

                // Indefinitely iterating through the tiles in the direction.
                // The stopping conditions are running out of board space or hitting another game piece.
                while (true) {

                    // The -1 converts the position to an index in the game board.
                    int xChecking = xPosition + xDirection * directionScalar;
                    int yChecking = yPosition + yDirection * directionScalar;

                    // Breaking the loop for this direction if the bishop would move off the board.
                    if (xChecking < 0 || xChecking >= boardColumns || yChecking < 0 || yChecking >= boardRows) {
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

    public bishop copyPiece() {return new bishop(getX(), getY());}
}
