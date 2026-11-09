package src.gameClasses.pieces;

import java.util.ArrayList;

public abstract class piece {
    protected boolean pieceIsWhite;

    private int row;
    private int col;

    // When implemented, will generate the tiles that the piece can see.
    // Will return as an ArrayList containing containing int arrays of length 2.
    // This will not take into consideration if the board state created by moving to a location would be valid.
    public abstract ArrayList<int[]> getVisibleTiles(piece[][] gameBoard);

    // When implemented, this will return a copy of the subclass.
    public abstract piece copyPiece();

    protected  void setRow(int newRow) {row=newRow;}
    protected  void setCol(int newCol) {col=newCol;}
    public void setPosition(int newRow, int newCol) {setRow(newRow); setRow(newCol);}
    public void setPosition(int[] position) {setRow(position[0]); setCol(position[1]);}

    protected  int getRow() {return row;}
    protected  int getCol() {return col;}
    public int[] getPosition() {return createArray(getRow(), getCol());}

    // Takes two integers and returns them as an array.
    // Reduces bloat since we create many arrays.
    protected int[] createArray(int row, int col) {
        int[] newArray = new int[2];

        newArray[0] = row;
        newArray[1] = col;

        return newArray;
    }

    public boolean pieceIsWhite() {return pieceIsWhite;}
    public void setColor(boolean isWhite) {pieceIsWhite = isWhite;}
}
