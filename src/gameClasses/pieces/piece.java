package src.gameClasses.pieces;

import java.util.ArrayList;

public abstract class piece {
    protected boolean pieceIsWhite;

    private int x;
    private int y;

    // When implemented, will generate the tiles that the piece can see.
    // Will return as an ArrayList containing containing int arrays of length 2.
    // This will not take into consideration if the board state created by moving to a location would be valid.
    public abstract ArrayList<int[]> getVisibleTiles(piece[][] gameBoard);

    protected  void setX(int newX) {x = newX;}
    protected  void setY(int newY) {y = newY;}
    public void setPosition(int newX, int newY) {setX(newX); setY(newY);}

    protected  int getX() {return x;}
    protected  int getY() {return y;}
    public int[] getPosition() {return createArray(getX(), getY());}

    // Takes two integers and returns them as an array.
    // Reduces bloat since we create many arrays.
    protected int[] createArray(int x, int y) {
        int[] newArray = new int[2];

        newArray[0] = x;
        newArray[1] = y;

        return newArray;
    }

    public boolean pieceIsWhite() {
        return pieceIsWhite;
    }

    public void setColor(boolean isWhite) {
        pieceIsWhite = isWhite;
    }
}
