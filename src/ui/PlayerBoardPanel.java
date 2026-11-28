package ui;

import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JPanel;

import gameClasses.fogOfWar;
import gameClasses.game;
import gameClasses.board;
import gameClasses.player;
import gameClasses.pieces.piece;
import gameClasses.pieces.king;

// A board drawn from one player's point of view, so their pieces are at the bottom of the screen.
// Works like BoardPanel, but:
//     It only lets its own player move, and only on their turn.
//     It can be flipped for the black player.
//     It moves pieces through the shared game object, so every window stays in sync.
// Flipping only changes the order the squares are drawn in, the board array is never changed.
public class PlayerBoardPanel extends JPanel {
    private SquareButton[][] squares;
    private StatusPanel statusPanel;
    private game currentGame;
    private board connectedBoard;
    private player owner; // The player using this window.
    private SquareButton selected; // null when nothing is selected
    private ArrayList<int[]> moveableTiles = new ArrayList<int[]>(); // Tiles the selected piece can move to.

    // Two player Fog of War: decides when this board is uncovered and shows the Start Turn / End Turn prompts.
    // null in every other mode, where the board is always shown.
    private FogTurnHandoff handoff;

    // The prompts for two player Fog of War (null in every other mode).
    // GameLauncher places them in the window: the banner above the board and the card over the board.
    private FogPromptBanner promptBanner;
    private FogCoverCard coverCard;

    // flipped should be true for the black player, so black's pieces show at the bottom.
    public PlayerBoardPanel(StatusPanel statusPanel, game currentGame, player owner, boolean flipped) {
        this(statusPanel, currentGame, owner, flipped, null);
    }

    // handoff should be the same object for both windows in two player Fog of War, and null otherwise.
    public PlayerBoardPanel(StatusPanel statusPanel, game currentGame, player owner, boolean flipped, FogTurnHandoff handoff) {
        this.statusPanel = statusPanel;
        this.currentGame = currentGame;
        this.owner = owner;
        this.handoff = handoff;

        int size = currentGame.getBoard().getBoardState().length;
        squares = new SquareButton[size][size];
        setLayout(new GridLayout(size, size));

        // Shrinking the squares on big boards so two windows still fit side by side on screen.
        // An 8x8 board keeps the normal 70px squares.
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int maxBoardPixels = Math.min(screenSize.width / 2 - 40, screenSize.height - 150);
        int squarePixels = Math.min(SquareButton.DEFAULT_PIXEL_SIZE, maxBoardPixels / size);

        // GridLayout fills top-left to bottom-right, so the order squares are added decides which way the board faces.
        //     Not flipped (white): row 8 at the top, column 1 (a-file) on the left.
        //     Flipped (black): row 1 at the top, column 8 (h-file) on the left.
        // Each square still stores its real row and column, so the rest of the code does not care which way it faces.
        for (int screenRow = 0; screenRow < size; screenRow++) {
            for (int screenCol = 0; screenCol < size; screenCol++) {

                int row;
                int col;

                if (flipped) {
                    row = screenRow + 1;
                    col = size - screenCol;
                }
                else {
                    row = size - screenRow;
                    col = screenCol + 1;
                }

                SquareButton square = new SquareButton(row, col, squarePixels);
                square.addActionListener(e -> handleSquareClick(square));
                squares[row - 1][col - 1] = square; // Converting the position to an index.
                add(square);
            }
        }

        // Every time the game changes, redraw this board.
        currentGame.addChangeListener(this::refreshBoard);

        // In two player Fog of War, also redraw whenever a player starts or ends their turn.
        if (handoff != null) {
            handoff.addChangeListener(this::refreshBoard);
            promptBanner = new FogPromptBanner();
            coverCard = new FogCoverCard();
        }
    }

    // The banner and card for two player Fog of War, or null in other modes.
    public FogPromptBanner getPromptBanner() {return promptBanner;}
    public FogCoverCard getCoverCard() {return coverCard;}

    // Redraws every square from the board and updates the status bar.
    // Empty squares are cleared so pieces that moved or were captured disappear.
    public void refreshBoard() {
        clearSelection();

        piece[][] boardState = currentGame.getBoard().getBoardState();

        // Working out what this player is allowed to see in Fog of War.
        // Once the game is over the fog is lifted so both players can see the final board.
        boolean[][] visibleSquares = null; // null means everything is visible
        boolean hideEverything = false;

        if (currentGame.isFogOfWar() && currentGame.isOver() == false) {
            if (handoff != null && handoff.isRevealedTo(owner) == false) {
                hideEverything = true;
            }
            else {
                visibleSquares = fogOfWar.getVisibleSquares(currentGame.getBoard(), owner.controlsWhite());
            }
        }

        for (int rowIndex = 0; rowIndex < boardState.length; rowIndex++) {
            for (int colIndex = 0; colIndex < boardState[0].length; colIndex++) {
                piece currentPiece = boardState[rowIndex][colIndex];
                SquareButton currentTile = squares[rowIndex][colIndex];

                boolean squareIsHidden = hideEverything || (visibleSquares != null && visibleSquares[rowIndex][colIndex] == false);
                currentTile.setHidden(squareIsHidden);

                // Hidden squares never show a piece, even if one is there.
                if (currentPiece == null || squareIsHidden) {
                    currentTile.setIcon(null);
                    continue;
                }

                // If the window is not on screen yet the tile has no size, so fall back on the default size.
                int cellSize = Math.min(currentTile.getWidth(), currentTile.getHeight());
                if (cellSize <= 0) {
                    cellSize = currentTile.getPreferredSize().width;
                }

                currentTile.setIcon(currentPiece.getPieceIcon(cellSize));
            }
        }

        statusPanel.setTurn(currentGame.getTurnName());
        updatePrompt();
    }

    // Updates the status bar, and in two player Fog of War also the banner and the card over the board.
    private void updatePrompt() {
        if (handoff == null) {
            statusPanel.showMessage(currentGame.getStatusMessage());
            return;
        }

        String ownerName = currentGame.getColorName(owner);
        String opponentName = currentGame.getColorName(currentGame.getOpponent(owner));

        // Game over: the fog is lifted, so just show the result.
        if (currentGame.isOver()) {
            statusPanel.showMessage(currentGame.getStatusMessage());
            promptBanner.showCalm("Game over", currentGame.getStatusMessage());
            coverCard.hideCard();
        }

        // This player has moved and is looking at their updated board.
        else if (handoff.isWaitingToEnd(owner)) {
            statusPanel.showMessage(currentGame.getStatusMessage());
            promptBanner.showAction("Review your move", "Click End Turn when you are ready to pass to " + opponentName,
                    "End Turn", () -> handoff.endTurn(owner));
            coverCard.hideCard();
        }

        // Both boards are covered and it is this player's turn.
        else if (handoff.isWaitingToStart(owner)) {
            statusPanel.showMessage(currentGame.getStatusMessage());
            promptBanner.showAction("Your turn is ready", "Click Start Turn on the board below");
            coverCard.showCard(ownerName + "'s turn", "Make sure " + opponentName + " isn't looking",
                    "Start Turn", () -> handoff.startTurn(owner));
        }

        // This player is taking their turn.
        else if (handoff.isRevealedTo(owner)) {
            statusPanel.showMessage(currentGame.getStatusMessage());
            promptBanner.showCalm("Your turn", "Make your move");
            coverCard.hideCard();
        }

        // The opponent is taking their turn (or has moved and not clicked End Turn yet).
        else {
            statusPanel.showMessage("Board hidden");
            promptBanner.showCalm("Board hidden", "Waiting for " + opponentName + " to finish their turn");
            coverCard.showCard("Board hidden", opponentName + " is taking their turn");
        }
    }

    // Handles a click on a square.
    //     Click one of your pieces to select it.
    //     Click a tile it can move to in order to move it.
    //     Click it again (or an empty tile) to clear the selection.
    private void handleSquareClick(SquareButton square) {

        if (currentGame.isOver()) {
            statusPanel.showMessage(currentGame.getStatusMessage());
            return;
        }

        // In two player Fog of War, blocking clicks while this board is covered or waiting for End Turn.
        // The prompt in the status bar already says what to do, so it is shown again.
        if (handoff != null && (handoff.isRevealedTo(owner) == false || handoff.isWaitingToEnd(owner))) {
            updatePrompt();
            return;
        }

        // Blocking clicks when it is not this window's player's turn.
        if (currentGame.isPlayersTurn(owner) == false) {
            statusPanel.showMessage("Wait for " + currentGame.getTurnName() + " to move");
            return;
        }

        // Clicking the already selected square clears the selection.
        if (selected == square) {
            clearSelection();
            statusPanel.showMessage("Selection cleared");
            return;
        }

        // If a piece is selected and the clicked square is one it can move to, make the move.
        // The game moves the piece and then tells every window to redraw.
        if (selected != null && isMoveableTile(square.getPosition())) {
            piece pieceMoving = currentGame.getBoard().getPiece(selected.getPosition());
            
            int[] oldPosition = selected.getPosition();
            int oldCol = oldPosition[1];
            
            int[] newPosition = square.getPosition();
            int newCol = newPosition[1]; 

            if (pieceMoving instanceof king) {

                if (Math.abs(newCol - oldCol) >= 2) {
                    currentGame.performCastle(pieceMoving, newPosition);
                    clearSelection();
                }
                return;
            }

            clearSelection();

            currentGame.makeMove(pieceMoving, square.getPosition());
            return;
        }

        piece clickedPiece = currentGame.getBoard().getPiece(square.getPosition());

        // If the player clicked one of their own pieces, select it.
        if (clickedPiece != null && clickedPiece.pieceIsWhite() == owner.controlsWhite()) {
            clearSelection();

            selected = square;
            square.setSelected(true);
            moveableTiles = clickedPiece.getVisibleTiles(currentGame.getBoard());

            statusPanel.showMessage("Selected " + square.getSquareName());
            return;
        }

        // Otherwise the player clicked an empty tile or an opponent's piece they cannot move to.
        clearSelection();
    }

    // Returns true if the selected piece can move to the passed position.
    // Comparing by element since contains() compares the arrays' addresses.
    private boolean isMoveableTile(int[] position) {
        for (int[] tile : moveableTiles) {
            if (tile[0] == position[0] && tile[1] == position[1]) {
                return true;
            }
        }

        return false;
    }

    // Unhighlights the selected square and forgets its moves.
    private void clearSelection() {
        if (selected != null) {
            selected.setSelected(false);
            selected = null;
        }

        moveableTiles.clear();
    }

    public void connectBoard(board gameBoard) {
        connectedBoard = gameBoard;
    }

    public piece[][] getConnectedGameState() {
        return connectedBoard.getBoardState();
    }

}
