package gameClasses.pieces;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import gameClasses.board;
import gameClasses.player;

public class king extends piece {
    private player opponent;

    public king(int row, int col) {
        setPosition(row, col);
    }

    public king(boolean isWhite, int row, int col) {
        setColor(isWhite);
        setPosition(row, col);
    }
    
    // See abstract class for function description.
    public ArrayList<int[]> getVisibleTiles(board gameBoard) {
        ArrayList<int[]> validMovements = new ArrayList<int[]>();

        piece[][] gameState = gameBoard.getBoardState();
        int boardRows = gameState.length;
        int boardColumns = gameState[0].length;

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
        }

        if (this.hasMoved() == false) {
            int kingRow = getRow();
            int kingCol = getCol();

            for (int colChecking = kingCol - 1; colChecking >= 1; colChecking--) {
                piece pieceChecking = gameBoard.getPiece(kingRow, colChecking);

                // Breaks if you would be castling through check or into check
                if (getOpponent().canSeeTile(gameBoard, kingRow, colChecking) == true) {
                    if (kingCol - colChecking <= 2) {
                        break;
                    }
                }

                // If the tile is empty and cannot be seen by the opponent
                else if (pieceChecking == null) {continue;}

                // If the tile is not empty and is not the corner
                else if (colChecking != 1) {break;}

                // If the tile is the corner and contains a valid piece to castle with
                else {
                    if (pieceChecking.hasMoved() == false) {
                        validMovements.add(createArray(kingRow, kingCol-2));
                    }
                }
            }

            for (int colChecking = kingCol + 1; colChecking <= boardColumns; colChecking++) {
                piece pieceChecking = gameBoard.getPiece(kingRow, colChecking);

                // Breaks if you would be castling through check or into check
                if (getOpponent().canSeeTile(gameBoard, kingRow, colChecking) == true) {
                    if (colChecking - kingCol <= 2) {
                        break;
                    }
                }

                // If the tile is empty and cannot be seen by the opponent
                else if (pieceChecking == null) {continue;}

                // If the tile is empty and cannot be seen by the opponent
                else if (colChecking != boardColumns) {break;}

                // If the tile is the corner and contains a valid piece to castle with
                else {
                    if (pieceChecking.hasMoved() == false) {
                        validMovements.add(createArray(kingRow, kingCol+2));
                    }
                }
            }
        }

        return validMovements;
    }

    public king copyPiece() {return new king(pieceIsWhite(), getRow(), getCol());}

    // See abstract class for description.
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

    public void connectOpponent(player opponent) {this.opponent=opponent;}
    public player getOpponent() {return this.opponent;}
}
