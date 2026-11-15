package gameClasses;

import java.util.ArrayList;
import java.util.Random;

import gameClasses.pieces.piece;

// A simple automated opponent that controls one of the players in a game.
// Currently it captures a piece if it can, otherwise it makes a random move.
// It does not need its own window, it moves by calling game.makeMove like a person would.
public class computerOpponent {
    private game currentGame;
    private player controlledPlayer;
    private Random random = new Random();

    public computerOpponent(game currentGame, player controlledPlayer) {
        this.currentGame = currentGame;
        this.controlledPlayer = controlledPlayer;
    }

    // Returns true if this computer is in charge of the passed player.
    public boolean controls(player playerChecking) {return playerChecking == controlledPlayer;}

    // Picks a move and makes it. Does nothing if it is not the computer's turn.
    public void takeTurn() {
        if (currentGame.isPlayersTurn(controlledPlayer) == false) {
            return;
        }

        int[][] chosenMove = chooseMove();

        // If the computer has nothing it can move the game ends.
        if (chosenMove == null) {
            currentGame.endGame(currentGame.getColorName(controlledPlayer) + " has no moves left. Game over!");
            return;
        }

        board gameBoard = currentGame.getBoard();
        piece pieceMoving = gameBoard.getPiece(chosenMove[0]);

        currentGame.makeMove(pieceMoving, chosenMove[1]);
    }

    // Returns the move the computer wants to make as {starting position, ending position}.
    // Returns null if there are no moves.
    public int[][] chooseMove() {
        board gameBoard = currentGame.getBoard();

        ArrayList<int[][]> allMoves = new ArrayList<int[][]>();
        ArrayList<int[][]> captureMoves = new ArrayList<int[][]>();

        // Iterating through each of the computer's pieces.
        for (piece pieceChecking : controlledPlayer.getPieces()) {
            int[] startingPosition = pieceChecking.getPosition();

            // Skipping pieces that are no longer on the board at their position (e.g. ones that were captured).
            if (gameBoard.getPiece(startingPosition) != pieceChecking) {
                continue;
            }

            // Iterating through the tiles the piece can see.
            for (int[] endingPosition : pieceChecking.getVisibleTiles(gameBoard)) {
                int[][] move = new int[2][];
                move[0] = startingPosition;
                move[1] = endingPosition;

                allMoves.add(move);

                // If there is an opponent's piece on the tile this move is a capture.
                if (gameBoard.getPiece(endingPosition) != null) {
                    captureMoves.add(move);
                }
            }
        }

        if (captureMoves.size() > 0) {
            return captureMoves.get(random.nextInt(captureMoves.size()));
        }

        if (allMoves.size() > 0) {
            return allMoves.get(random.nextInt(allMoves.size()));
        }

        return null;
    }
}
