package src.gameClasses.pieces;

import java.util.ArrayList;

public class bishop extends piece {

    public bishop(int row, int col) {
        setPosition(row, col);
    }

    // See abstract class for function description.
    public ArrayList<int[]> getVisibleTiles(piece[][] gameBoard) {
        ArrayList<int[]> validMovements = new ArrayList<int[]>();

        int boardRows = gameBoard.length;
        int boardColumns = gameBoard[0].length;

        int row = getRow();
        int col = getCol();

        int[] rowDirections = createArray(-1, 1);
        int[] colDirections = createArray(-1, 1);

        // Iterating through each of the possible directions the bishop can use.
        for (int rowDirection : rowDirections) {
            for (int colDirection : colDirections) {

                int directionScalar = 1; // Represents how far the bishop is checking

                // Indefinitely iterating through the tiles in the direction.
                // The stopping conditions are running out of board space or hitting another game piece.
                while (true) {

                    // The -1 converts the position to an index in the game board.
                    int rowChecking = row + rowDirection * directionScalar - 1;
                    int colChecking = col + colDirection * directionScalar - 1;

                    // Breaking the loop for this direction if the bishop would move off the board.
                    if (rowChecking < 0 || rowChecking >= boardColumns || colChecking < 0 || colChecking >= boardRows) {
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

    public bishop copyPiece() {return new bishop(getRow(), getCol());}
}
