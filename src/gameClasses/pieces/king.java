package gameClasses.pieces;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class king extends piece {

    public king(int row, int col) {
        setPosition(row, col);
    }

    public king(boolean isWhite, int row, int col) {
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

        // Iterating through each of the possible movements a king can make.
        for (int rowMovement=-1; rowMovement<=1; rowMovement++) {
            for (int colMovement=-1; colMovement<=1; colMovement++) {

                // Skipping the combination representing the king not moving.
                if (rowMovement == 0 && colMovement == 0) {
                    continue;
                }

                int rowChecking = row + rowMovement;
                int colChecking = col + colMovement;

                // Skipping combinations where the king would move off the board.
                if (rowChecking < 1 || rowChecking > boardRows || colChecking < 1 || colChecking > boardColumns) {
                    continue;
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
                }
            }
        }

        return validMovements;
    }

    public king copyPiece() {return new king(pieceIsWhite(), getRow(), getCol());}

    public BufferedImage getPieceImage() {

        String imagePath;

        if (this.pieceIsWhite() == true) {imagePath = "src/gameClasses/gamePieceImages/KingWhite.png";}
        else {imagePath = "src/gameClasses/gamePieceImages/KingBlack.png";}

        try {
            return ImageIO.read(new File(imagePath));
        }
        
        catch (IOException exception) {
            System.err.println("Could not load image.png: " + exception.getMessage());
            return null;
        }
    
    }
}
