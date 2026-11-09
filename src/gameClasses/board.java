package src.gameClasses;

import src.gameClasses.pieces.piece;

public class board {
    piece[][] board;
    int boardSize;

    public board(int size){
        boardSize = size;
        board = new piece[boardSize][boardSize];
    }

    // When implemented, this will determine if a game state is valid.
    // Will return a boolean. true if valid state, false if invalid.
    // This will be called when:
    //     Determining if the game is over (there are no valid states)
    //     Determining if piece moving is valid (e.g. if a move would result in the player being in check, then it is invalid)
    public boolean isValidState() {
        return true;
    }

    // Returns a copy of the board containing copies of the pieces.
    // Will be used for testing a state's validity without losing the original state.
    public piece[][] copyBoard() {
        piece[][] boardCopy = new piece[boardSize][boardSize];

        for (int rowIndex=0; rowIndex<boardSize; rowIndex++) {
            for (int columnIndex=0; columnIndex<boardSize; columnIndex++) {
                boardCopy[rowIndex][columnIndex] = board[rowIndex][columnIndex].copyPiece();
            }
        }

        return boardCopy;
    }

    public piece[][] getBoard() {
        return board;
    }

    // Takes a board state and sets this object's board to it
    public void setBoard(piece[][] boardState) {board = boardState;}

    // Determines if the game is over.
    // Returns true if the game is over, false otherwise.
    public boolean isGameOver() {
        //  
        //  for (row in board)
        //      for (column in board)
        //          pieceTesting = board[row][column]
        //          pieceCopy = pieceTesting.copyPiece()
        //          valid_moves = piece.getVisibleTiles()
        //          
        //          for (position in valid_moves)
        //              boardTesting = getBoard()
        //
        //              pieceCopy.setPosition(position)
        //              
        //              boardTesting[position] = pieceCopy
        //              boardTesting[row][column] = null
        //
        //              if (testingBoard.isValidState() == true)
        //                  return false
        //
        return true;
    }

    // This function places the passed piece at the given positions.
    // To place at the correct spot we must convert the position to an index.
    public void placePiece(piece piecePlacing, int xPosition, int yPosition) {
        board[xPosition-1][yPosition-1] = piecePlacing;
    }
}