package gameClasses;

import java.util.ArrayList;

import gameClasses.pieces.piece;
import gameClasses.pieces.king;

// Holds everything that is shared between the board windows:
//     The board, both players, whose turn it is, and if the game is over.
// Every window looks at the same game object, so they always agree on whose turn it is.
// This class never rearranges the board array, it only calls the board's own methods.
public class game {
    private board gameBoard;
    private player whitePlayer;
    private player blackPlayer;
    private player currentPlayer;

    private boolean gameIsOver = false;

    // When true, each player can only see the squares their pieces can move to (see fogOfWar).
    private boolean fogOfWarEnabled = false;
    private String statusMessage = "White moves first";

    // Remembers if each side had a king when the game started.
    // Used so a side with no king (like in a test setup) does not instantly lose.
    private boolean whiteStartedWithKing;
    private boolean blackStartedWithKing;

    // Code to run every time the game changes (e.g. each window redrawing itself).
    private ArrayList<Runnable> changeListeners = new ArrayList<Runnable>();

    // The two players can be passed in either order, the constructor sorts out which is white.
    public game(board gameBoard, player firstPlayer, player secondPlayer) {
        this.gameBoard = gameBoard;

        if (firstPlayer.controlsWhite()) {
            whitePlayer = firstPlayer;
            blackPlayer = secondPlayer;
        }
        else {
            whitePlayer = secondPlayer;
            blackPlayer = firstPlayer;
        }

        // White always moves first.
        currentPlayer = whitePlayer;

        whiteStartedWithKing = kingIsOnBoard(true);
        blackStartedWithKing = kingIsOnBoard(false);
    }

    public board getBoard() {return gameBoard;}
    public player getWhitePlayer() {return whitePlayer;}
    public player getBlackPlayer() {return blackPlayer;}
    public player getCurrentPlayer() {return currentPlayer;}
    public boolean isOver() {return gameIsOver;}
    public String getStatusMessage() {return statusMessage;}

    public boolean isFogOfWar() {return fogOfWarEnabled;}
    public void setFogOfWar(boolean enabled) {fogOfWarEnabled = enabled;}

    // Returns true if it is the passed player's turn and the game is still going.
    public boolean isPlayersTurn(player playerChecking) {
        return gameIsOver == false && currentPlayer == playerChecking;
    }

    // Returns the player on the other side of the passed player.
    public player getOpponent(player playerChecking) {
        if (playerChecking == whitePlayer) {return blackPlayer;}
        return whitePlayer;
    }

    // Returns "White" or "Black" for the passed player.
    public String getColorName(player playerNaming) {
        if (playerNaming.controlsWhite()) {return "White";}
        return "Black";
    }

    // Returns "White" or "Black" for whoever's turn it is.
    public String getTurnName() {return getColorName(currentPlayer);}

    // Adds code that will run every time a move is made or the game ends.
    public void addChangeListener(Runnable listener) {changeListeners.add(listener);}

    // Moves a piece for the current player, then hands the turn to the other player.
    // The windows call this instead of moving pieces on the board themselves.
    public void makeMove(piece pieceMoving, int[] newPosition) {
        if (gameIsOver) {
            return;
        }

        player movingPlayer = currentPlayer;
        player waitingPlayer = getOpponent(movingPlayer);

        gameBoard.movePiece(waitingPlayer, pieceMoving, newPosition);
        // In Fog of War the message leaves out the square so it does not reveal where the opponent went.
        if (fogOfWarEnabled) {
            statusMessage = getColorName(movingPlayer) + " moved";
        }
        else {
            statusMessage = getColorName(movingPlayer) + " moved to " + getSquareName(newPosition);
        }

        endTurn(movingPlayer, waitingPlayer);
    }

    public void performCastle(piece kingMoving, int[] newPosition) {
        if (gameIsOver) {
            return;
        }

        player movingPlayer = currentPlayer;
        player waitingPlayer = getOpponent(movingPlayer);
        piece pieceCastling;

        int kingRow = newPosition[0];
        int newKingCol = newPosition[1];
        int newOtherCol;

        System.out.println("King moved to " + kingRow  + ", " + newKingCol);
        gameBoard.movePiece(waitingPlayer, kingMoving, newPosition);

        if (newKingCol - 1 < gameBoard.getBoardSize() - newKingCol) {
            pieceCastling = gameBoard.getPiece(kingRow, 1);
            newOtherCol = newKingCol + 1;
        }

        else {
            pieceCastling = gameBoard.getPiece(kingRow, gameBoard.getBoardSize());
            newOtherCol = newKingCol - 1;
        }

        System.out.println(pieceCastling + " moved to " + kingRow + ", " + newOtherCol);
        gameBoard.movePiece(waitingPlayer, pieceCastling, kingRow, newOtherCol);

        statusMessage = getColorName(movingPlayer) + " castled to " + getSquareName(newPosition);

        endTurn(movingPlayer, waitingPlayer);

    }

    // Ends the game with the passed message (e.g. when the computer has no moves left).
    public void endGame(String message) {
        gameIsOver = true;
        statusMessage = message;
        notifyListeners();
    }

    // Swaps whose turn it is, then checks if the player whose turn it now is has lost or is in check.
    private void endTurn(player movingPlayer, player waitingPlayer) {
        currentPlayer = waitingPlayer;

        boolean waitingPlayerIsWhite = waitingPlayer.controlsWhite();
        boolean waitingPlayerStartedWithKing = (waitingPlayerIsWhite && whiteStartedWithKing) || (!waitingPlayerIsWhite && blackStartedWithKing);

        // If the waiting player's king was captured the moving player wins.
        if (waitingPlayerStartedWithKing && kingIsOnBoard(waitingPlayerIsWhite) == false) {
            endGame(getColorName(movingPlayer) + " captured the king. " + getColorName(movingPlayer) + " wins!");
            return;
        }

        // Check and checkmate can only be looked for if the waiting player has a king.
        if (kingIsOnBoard(waitingPlayerIsWhite)) {

            if (gameBoard.playerIsInCheck(movingPlayer, waitingPlayer)) {

                if (gameBoard.isGameOver(movingPlayer, waitingPlayer)) {
                    endGame("Checkmate! " + getColorName(movingPlayer) + " wins!");
                    return;
                }

                statusMessage += ". " + getColorName(waitingPlayer) + " is in check!";
            }
        }

        notifyListeners();
    }

    // Runs all the code added with addChangeListener.
    private void notifyListeners() {
        for (Runnable listener : changeListeners) {
            listener.run();
        }
    }

    // Returns true if a king of the passed color is anywhere on the board.
    // Looks at the board itself rather than the player's list of pieces.
    private boolean kingIsOnBoard(boolean kingIsWhite) {
        piece[][] boardState = gameBoard.getBoardState();

        for (piece[] boardRow : boardState) {
            for (piece tile : boardRow) {

                if (tile instanceof king && tile.pieceIsWhite() == kingIsWhite) {
                    return true;
                }
            }
        }

        return false;
    }

    // Converts a position to chess notation, e.g. {1, 1} -> "a1".
    private String getSquareName(int[] position) {
        char file = (char) ('a' + position[1] - 1);
        return "" + file + position[0];
    }
}
