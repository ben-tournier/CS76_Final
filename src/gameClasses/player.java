package src.gameClasses;

import java.util.ArrayList;
import src.gameClasses.pieces.piece;

public class player {
    private boolean controlsWhite;
    private ArrayList<piece> playerPieces;

    public player(boolean controlsWhite) {
        this.controlsWhite = controlsWhite;
    }

    public boolean controlsWhite() {return controlsWhite;}

    // When implemented this function will get the type the user wants to promote a pawn into.
    public void promptForPromotion() {}

    public void addPiece(piece newPiece) {playerPieces.add(newPiece);}
    public ArrayList<piece> getPieces() {return playerPieces;}
}
