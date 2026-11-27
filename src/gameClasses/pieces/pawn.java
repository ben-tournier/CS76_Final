package gameClasses.pieces;

import java.util.ArrayList;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import gameClasses.board;

//  To Do:
//      Implement getVisibleTiles
//          Requires enPassant
//          The ability to move two if canDoubleMove is true
//          After the pawn moves, set canDoubleMove to false
//          If the pawn moves, set canDoubleMove to false
//          If the pawn moves 2, set isEnPassantValid to true
//          When an opponent moves, set isEnPassantValid to false
//          Ensure there is a separate function to differentiate the tiles a pawn can see vs tiles the pawn can attack.

public class pawn extends piece {
    private boolean isEnPassantValid = false; // Represents if this piece can be taken by 

    public pawn(int row, int col) {
        setPosition(row, col);
    }

    public ArrayList<int[]> getVisibleTiles(board gameBoard) {
        ArrayList<int[]> validMovements = new ArrayList<int[]>();

        int rowChecking;
        int colChecking;
        int directionIndicator;

        int pawnRow = getRow();
        int pawnCol = getCol();

        piece[][] boardState = gameBoard.getBoardState();
        int numRows = boardState.length;
        int numCols = boardState[0].length;
        piece pieceChecking;

        if (this.pieceIsWhite() == true) {directionIndicator = 1;}
        else {directionIndicator = -1;}

        // Checking the tile directly in front of the pawn.
        // The tile must be empty for it to be a possible move.
        rowChecking = pawnRow + 1 * directionIndicator;
        colChecking = pawnCol;

        pieceChecking = gameBoard.getPiece(rowChecking, colChecking);
        
        if (pieceChecking == null) {
            validMovements.add(createArray(rowChecking, colChecking));
        }

        // Checking the tile two spaces in front of the pawn (if that would be valid).
        // The tile must be empty for it to be a possible move.
        if (this.canDoubleMove() == true && pieceChecking == null) {
            rowChecking = pawnRow + 2 * directionIndicator;
            colChecking = pawnCol;

            if (rowChecking >= 1 && rowChecking <= numRows) {
                pieceChecking = gameBoard.getPiece(rowChecking, colChecking);

                if (pieceChecking == null) {
                    validMovements.add(createArray(rowChecking, colChecking));
                }
            }
        }

        // Checking if the pawn can diagonally attack a piece (increasing column).
        rowChecking = pawnRow + 1 * directionIndicator;
        colChecking = pawnCol + 1;

        if (colChecking >= 1 && colChecking <= numCols) {
            pieceChecking = gameBoard.getPiece(rowChecking, colChecking);

            if (pieceChecking != null) {
                if (pieceChecking.pieceIsWhite() != this.pieceIsWhite()) {
                    validMovements.add(createArray(rowChecking, colChecking));
                }
            }

            
        }

        // Checking if the pawn can diagonally attack a piece (decreasing column).
        rowChecking = pawnRow + 1 * directionIndicator;
        colChecking = pawnCol - 1;

        if (colChecking >= 1 && colChecking <= numCols) {
            pieceChecking = gameBoard.getPiece(rowChecking, colChecking);

            if (pieceChecking != null) {
                if (pieceChecking.pieceIsWhite() != this.pieceIsWhite()) {
                    validMovements.add(createArray(rowChecking, colChecking));
                }
            }
        }

        // Checking if the pawn can take via enPassant (increasing column).
        rowChecking = pawnRow;
        colChecking = pawnCol + 1;

        if (colChecking >= 1 && colChecking <= numCols) {
            pieceChecking = gameBoard.getPiece(rowChecking, colChecking);

            if (pieceChecking != null) {

                if (pieceChecking instanceof pawn && pieceChecking.pieceIsWhite() != this.pieceIsWhite()) {

                    pawn pawnChecking = (pawn) gameBoard.getPiece(rowChecking, colChecking);

                    if (pawnChecking.canBeTakenByEnPassant()) {}
                        validMovements.add(createArray(rowChecking + 1 * directionIndicator, colChecking));
                }
            }
        }

        // Check if the pawn can take via enPassant (decreasing column).
        rowChecking = pawnRow;
        colChecking = pawnCol - 1;

        if (colChecking >= 1 && colChecking <= numCols) {
            pieceChecking = gameBoard.getPiece(rowChecking, colChecking);

            if (pieceChecking != null) {

                if (pieceChecking instanceof pawn && pieceChecking.pieceIsWhite() != this.pieceIsWhite()) {

                    pawn pawnChecking = (pawn) pieceChecking;

                    if (pawnChecking.canBeTakenByEnPassant() == true) {
                        validMovements.add(createArray(rowChecking + 1 * directionIndicator, colChecking));
                    }
                }
            }
        }

        return validMovements;
    }

    public boolean canDoubleMove() {return !hasMoved();}

    // Returns a copy of the pawn
    public pawn copyPiece() {
        pawn copiedPawn = new pawn(getRow(), getCol());

        copiedPawn.hasMoved = this.hasMoved();
        copiedPawn.isEnPassantValid = this.canBeTakenByEnPassant();

        return copiedPawn;
    }

    // See abstract class for description.
    public BufferedImage getPieceImage() {

        String imagePath;

        if (this.pieceIsWhite() == true) {imagePath = "src/gameClasses/gamePieceImages/PawnWhite.png";}
        else {imagePath = "src/gameClasses/gamePieceImages/PawnBlack.png";}

        try {
            return ImageIO.read(new File(imagePath));
        }
        
        catch (IOException exception) {
            System.err.println("Could not load image.png: " + exception.getMessage());
            return null;
        }
    
    }

    public boolean canBeTakenByEnPassant() {return isEnPassantValid;}
    public void setAllowEnPassant(boolean allowEnPassant) {isEnPassantValid = allowEnPassant;}

    public String toString() {return "pawn";}
}
