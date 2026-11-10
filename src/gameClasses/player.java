package src.gameClasses;

import java.util.ArrayList;
import src.gameClasses.pieces.piece;
import src.gameClasses.pieces.king;

public class player {
    private boolean controlsWhite;
    private ArrayList<piece> playerPieces;

    public player(boolean controlsWhite) {
        this.controlsWhite = controlsWhite;
        this.playerPieces = new ArrayList<piece>();
    }

    public boolean controlsWhite() {return controlsWhite;}

    // When implemented this function will get the type the user wants to promote a pawn into.
    public void promptForPromotion() {}

    public void addPiece(piece newPiece) {playerPieces.add(newPiece);}
    public ArrayList<piece> getPieces() {return playerPieces;}

    // Returns the position of the player's king.
    // Finds the position by iterating through a player's pieces.
    public int[] getKingPosition() {

        for (piece piece : playerPieces) {

            // This statement is true if the piece is of type king.
            // Credit to https://hyperskill.org/university/java/runtime-type-checking-in-java for where I (Christian) learned this
            if (piece instanceof king) {
                return piece.getPosition();
            }
        }

        // There should always be a king, so throw a runtime exception if not.
        throw new RuntimeException(this.toString() + " has no king");
    }

    // Returns a copy of the player (does not copy pieces)
    public player copyPlayer() {return new player(controlsWhite());}
}
