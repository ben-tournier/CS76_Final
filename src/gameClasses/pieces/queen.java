package src.gameClasses.pieces;

import java.util.ArrayList;

public class queen extends piece {

    public queen(int row, int col) {
        setPosition(row, col);
    }

    // See abstract class for function description.
    public ArrayList<int[]> getVisibleTiles(piece[][] gameBoard) {
        ArrayList<int[]> validMovements = new ArrayList<int[]>();

        int boardRows = gameBoard.length;
        int boardColumns = gameBoard[0].length;

        int row = getRow();
        int col = getCol();

        // Iterating through each of the possible directions the queen can move.
        for (int rowDirection=-1; rowDirection<=1; rowDirection++) {
            for (int colDirection=-1; colDirection<=1; colDirection++) {

                // Skipping the combination representing the queen not moving.
                if (rowDirection == 0 && colDirection == 0) {
                    continue;
                }

                int directionScalar = 1; // Represents how far the queen is checking

                // Indefinitely iterating through the next tiles in the direction.
                // The stopping conditions are running out of board space or hitting another game piece.
                while (true) {

                    // The -1 converts the position to an index in the game board.
                    int rowChecking = row + rowDirection * directionScalar - 1;
                    int colChecking = col + colDirection * directionScalar - 1;

                    // Skipping combinations where the king would move off the board (the indexes are not valid).
                    if (rowChecking < 0 || rowChecking >= boardRows || colChecking < 0 || colChecking >= boardColumns) {
                        break;
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

    public queen copyPiece() {return new queen(getRow(), getCol());}
}
