package gameClasses;

import gameClasses.pieces.piece;
import java.util.ArrayList;

public class board {
    piece[][] boardState;
    int boardSize;

    public board(int size){
        boardSize = size;
        boardState = new piece[boardSize][boardSize];
    }

    // When implemented, this will determine if a game state is valid.
    // Will return a boolean. true if valid state, false if invalid.
    // This will be called when:
    //     Determining if the game is over (there are no valid states)
    //     Determining if piece moving is valid (e.g. if a move would result in the player being in check, then it is invalid)
    public boolean isValidState() {
        return true;
    }

    // Returns a copy of the board state containing copies of the pieces.
    // Will be used for testing a state's validity without losing the original state.
    public piece[][] copyBoardState() {
        piece[][] boardCopy = new piece[boardSize][boardSize];

        for (int rowIndex=0; rowIndex<boardSize; rowIndex++) {
            for (int columnIndex=0; columnIndex<boardSize; columnIndex++) {

                if (boardState[rowIndex][columnIndex] == null) {
                    continue;
                }

                boardCopy[rowIndex][columnIndex] = boardState[rowIndex][columnIndex].copyPiece();
            }
        }

        return boardCopy;
    }

    // Returns a copy of this object containing a copy of the board state
    public board copyBoard() {
        piece[][] copiedState = copyBoardState();

        board copiedBoard = new board(boardSize);
        copiedBoard.setBoardState(copiedState);

        return copiedBoard;
    }

    // Returns the 2d array containing the pieces
    public piece[][] getBoardState() {return boardState;}

    // Takes a board state and sets this object's board to it
    public void setBoardState(piece[][] newBoardState) {boardState = newBoardState;}

    //  Determines if the game is over.
    //  Returns true if the game is over, false otherwise.
    //  Currently the game ends if a player has no possible moves, though more could be added.
    public boolean isGameOver(player attackingPlayer, player defendingPlayer) {

        // Iterating through the defending player's pieces to see if there is a valid move.
        for (piece piece : defendingPlayer.getPieces()) {

            int[] piecePosition = piece.getPosition();
            int pieceRow = piecePosition[0];
            int pieceCol = piecePosition[1];

            ArrayList<int[]> possibleMoves = piece.getVisibleTiles(boardState);

            // Iterating through the tiles the piece can see.
            for (int[] move : possibleMoves) {
                
                board clonedBoard = copyBoard();
                piece[][] clonedBoardState = clonedBoard.getBoardState();
                piece clonedPiece = clonedBoardState[pieceRow - 1][pieceCol - 1];

                // This following section is recreating the players and assigning the pieces to them.
                // This will allow the cloned board to use the 'playerIsInCheck' function.
                player clonedAttacker = attackingPlayer.copyPlayer();
                player clonedDefender = defendingPlayer.copyPlayer();

                for (piece attackingPiece : attackingPlayer.getPieces()) {
                    piecePosition = attackingPiece.getPosition();
                    clonedAttacker.addPiece(clonedBoardState[piecePosition[0] - 1][piecePosition[1] - 1]);
                }

                for (piece defendingPiece : defendingPlayer.getPieces()) {
                    piecePosition = defendingPiece.getPosition();
                    clonedDefender.addPiece(clonedBoardState[piecePosition[0] - 1][piecePosition[1] - 1]);
                }

                // Simulating moving the piece to see if the created state is valid
                clonedBoard.movePiece(attackingPlayer, clonedPiece, move[0], move[1]);

                // If the hypothetical move by the defending player created a state where they are not in check the game is not over.
                if (clonedBoard.playerIsInCheck(clonedAttacker, clonedDefender) == false) {
                    return false;
                }
            }
        }

        return true;
    }

    // Returns true if the attacking player has a piece that can see the defending player's king.
    public boolean playerIsInCheck(player attackingPlayer, player defendingPlayer) {

        // Get the position of the king we are checking.
        int[] kingPosition = defendingPlayer.getKingPosition();

        // Iterate through each attacking player's pieces.
        for (piece attackingPiece : attackingPlayer.getPieces()) {

            // Get the squares that the attacking piece can see.
            ArrayList<int[]> squaresAttacking = attackingPiece.getVisibleTiles(boardState);

            // iterate through each of the squares the attacking piece can see
            for (int[] squareAttacking : squaresAttacking) {

                // If the current square has the defending king's position return true
                // Checking by element as comparing the arrays did not work.
                if (squareAttacking[0] == kingPosition[0] && squareAttacking[1] == kingPosition[1]) {
                    return true;
                }
            }
        }

        return false;
    }

    // Places the passed piece at the given position.
    // To place at the correct spot we must convert the position to an index.
    public void placePiece(piece piecePlacing, int newRow, int newCol) {
        boardState[newRow - 1][newCol - 1] = piecePlacing;
    }

    // Overloads the function above to accept an array instead of two integers.
    public void placePiece(piece piecePlacing, int[] newPosition) {
        placePiece(piecePlacing, newPosition[0], newPosition[1]);
    }

    // Moves a piece to a new position and removes itself from the old position.
    // To Do: If there is a piece where this is moving, remove it from the ArrayList of it's owner's pieces.
    public void movePiece(player defendingPlayer, piece piecePlacing, int newRow, int newCol) {
        int[] piecePosition = piecePlacing.getPosition();
        int pieceRow = piecePosition[0];
        int pieceCol = piecePosition[1];

        if (defendingPlayer.getPieces().contains(boardState[pieceRow - 1][pieceCol - 1])) {
            defendingPlayer.getPieces().remove(boardState[pieceRow - 1][pieceCol - 1]);
        }

        placePiece(piecePlacing, newRow, newCol);
        piecePlacing.setPosition(newRow, newCol);

        boardState[pieceRow - 1][pieceCol - 1] = null;
    }

    // Overloads the function above to accept an array instead of two integers.
    public void movePiece(player defendingPlayer, piece piecePlacing, int[] newPosition) {
        movePiece(defendingPlayer, piecePlacing, newPosition[0], newPosition[1]);
    }

    // Places a piece on the board and assigns it to the given player.
    public void spawnPiece(piece pieceSpawning, player owner, int rowPlacing, int colPlacing) {
        owner.addPiece(pieceSpawning);
        pieceSpawning.setColor(owner.controlsWhite());

        placePiece(pieceSpawning, rowPlacing, colPlacing);
    }

    // Overloads the function above to accept an array instead of two integers.
    public void spawnPiece(piece pieceSpawning, player owner, int[] positionPlacing) {
        spawnPiece(pieceSpawning, owner, positionPlacing[0], positionPlacing[1]);
    }

    // Returns the piece at the given position
    public piece getPiece(int rowAccessing, int colAccessing) {
        return boardState[rowAccessing - 1][colAccessing - 1];
    }

    // Overloads the function above to accept an array
    public piece getPiece(int[] positionAccessing) {
        return getPiece(positionAccessing[0], positionAccessing[1]);
    }
}