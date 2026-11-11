package gameClasses.pieces;

import java.util.ArrayList;

public class rook extends piece {
    boolean hasMoved = false;

    public rook(int row, int col) {
        setPosition(row, col);
    }

    public rook(boolean isWhite, int row, int col) {
        setColor(isWhite);
        setPosition(row, col);
    }

    // See abstract class for function description.
    public ArrayList<int[]> getVisibleTiles(piece[][] gameBoard) {
        ArrayList<int[]> validMovements = new ArrayList<int[]>();

        int boardRows = gameBoard.length;
        int boardColumns = gameBoard[0].length;

        int row = getRow();
        int col = getCol();
        
        int[][] directions = new int[4][2];
        directions[0] = createArray(-1, 0);
        directions[1] = createArray(1, 0);
        directions[2] = createArray(0, -1);
        directions[3] = createArray(0, 1);

        // Iterating through each of the possible directions the rook can move.
        for (int[] direction : directions) {
            int rowDirection = direction[0];
            int colDirection = direction[1];

            int directionScalar = 1; // // Represents how far the rook is checking

            // Indefinitely iterating through the next tiles in the direction.
            // The stopping conditions are running out of board space or hitting another game piece.
            while (true) {

                int rowChecking = row + rowDirection * directionScalar;
                int colChecking = col + colDirection * directionScalar;

                // Exiting the loop for the current direction if the rook would move off the board.
                if (rowChecking < 1 || rowChecking > boardRows || colChecking < 1 || colChecking > boardColumns) {
                    break;
                }

                piece tileChecking = gameBoard[rowChecking - 1][colChecking - 1];

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

        return validMovements;
    }

    public rook copyPiece() {return new rook(pieceIsWhite(), getRow(), getCol());}
}
