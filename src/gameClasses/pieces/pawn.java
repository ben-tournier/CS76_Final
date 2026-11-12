package gameClasses.pieces;

import java.util.ArrayList;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
}
