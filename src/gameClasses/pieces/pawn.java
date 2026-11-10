package src.gameClasses.pieces;

import java.util.ArrayList;

//  To Do:
//      Implement getVisibleTiles
//          Requires enPassant
//          The ability to move two if canDoubleMove is true
//          After the pawn moves, set canDoubleMove to false
//          If the pawn moves, set canDoubleMove to false
//          If the pawn moves 2, set isEnPassantValid to true
//          When an opponent moves, set isEnPassantValid to false
//          Ensure there is a separate function to differentiate the tiles a pawn can see vs tiles the pawn can attack.

public class pawn extends piece{
    private boolean canDoubleMove = true;
    private boolean isEnPassantValid = false;

    public pawn(int row, int col) {
        setPosition(row, col);
    }

    public ArrayList<int[]> getVisibleTiles(piece[][] gameBoard) {
        return new ArrayList<int[]>();
    }

    public boolean canDoubleMove() {
        return canDoubleMove;
    }

    public pawn copyPiece() {
        pawn copiedPawn = new pawn(getRow(), getCol());

        copiedPawn.canDoubleMove = this.canDoubleMove();

        return copiedPawn;
    }
}
