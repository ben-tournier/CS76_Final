package gameClasses.pieces;

import java.util.ArrayList;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import gameClasses.board;

public abstract class piece {
    protected boolean pieceIsWhite;
    protected boolean hasMoved = false;

    private int row;
    private int col;

    // When implemented, will generate the tiles that the piece can see.
    // Will return as an ArrayList containing containing int arrays of length 2.
    // This will not take into consideration if the board state created by moving to a location would be valid.
    public abstract ArrayList<int[]> getVisibleTiles(board gameBoard);

    // When implemented, this will return a copy of the subclass.
    public abstract piece copyPiece();

    protected  void setRow(int newRow) {row=newRow;}
    protected  void setCol(int newCol) {col=newCol;}
    public void setPosition(int newRow, int newCol) {setRow(newRow); setCol(newCol);}
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

    // Returns the piece's color (white -> true, black -> false)
    public boolean pieceIsWhite() {return pieceIsWhite;}

    // Sets the piece's color (true -> white, black -> false)
    public void setColor(boolean isWhite) {pieceIsWhite = isWhite;}

    // When implemented each subclass will access and load the respective image.
    public abstract BufferedImage getPieceImage();

    // Gets the piece's image and then creates and returns the icon.
    public ImageIcon getPieceIcon(int imageSize) {
        BufferedImage pieceImage = getPieceImage();

        ImageIcon pieceIcon = new ImageIcon(pieceImage.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH));

        return pieceIcon;
    };

    public boolean hasMoved() {return hasMoved;}
    public void moved() {hasMoved = true;}
}
