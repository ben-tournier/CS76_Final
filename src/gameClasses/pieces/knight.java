package gameClasses.pieces;

import java.util.ArrayList;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import gameClasses.board;

public class knight extends piece {

    public knight(int row, int col) {
        setPosition(row, col);
    }

    public knight(boolean isWhite, int row, int col) {
        setColor(isWhite);
        setPosition(row, col);
    }
    
    // See abstract class for function description.
    public ArrayList<int[]> getVisibleTiles(board gameBoard) {
        ArrayList<int[]> validMovements = new ArrayList<int[]>();

        int boardSize = gameBoard.getBoardSize();
        int row = getRow();
        int col = getCol();

        int[][] movements = new int[8][2];
        movements[0] = createArray(-2, -1);
        movements[1] = createArray(-1, -2);
        movements[2] = createArray(1, -2);
        movements[3] = createArray(2, -1);
        movements[4] = createArray(2, 1);
        movements[5] = createArray(1, 2);
        movements[6] = createArray(-1, 2);
        movements[7] = createArray(-2, 1);

        // Iterating through each of the possible movements a knight can make.
        for (int[] movement : movements) {
            int rowMovement = movement[0];
            int colMovement = movement[1];

            int rowChecking = row + rowMovement;
            int colChecking = col + colMovement;

            // Skipping combinations where the knight would move off the board.
            if (rowChecking < 1 || rowChecking > boardSize || colChecking < 1 || colChecking > boardSize) {
                continue;
            }

            piece tileChecking = gameBoard.getPiece(rowChecking, colChecking);

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

        return validMovements;
    }

    public knight copyPiece() {return new knight(pieceIsWhite(), getRow(), getCol());}

    // See abstract class for description.
    public BufferedImage getPieceImage() {

        String imagePath;

        if (this.pieceIsWhite() == true) {imagePath = "src/gameClasses/gamePieceImages/KnightWhite.png";}
        else {imagePath = "src/gameClasses/gamePieceImages/KnightBlack.png";}

        try {
            return ImageIO.read(new File(imagePath));
        }
        
        catch (IOException exception) {
            System.err.println("Could not load image.png: " + exception.getMessage());
            return null;
        }
    }

    public String toString() {return "knight";}
}
