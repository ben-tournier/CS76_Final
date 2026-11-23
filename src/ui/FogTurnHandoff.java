package ui;

import java.util.ArrayList;

import gameClasses.game;
import gameClasses.player;

// Controls handing the screen from one player to the other in two player Fog of War.
// Both windows are on the same screen, so only one board is ever uncovered at a time:
//     1. Both boards are covered. The player whose turn it is clicks "Start Turn" to uncover their board.
//     2. They make their move and see their updated board, then click "End Turn".
//     3. Both boards are covered again and the other player is prompted to start their turn.
// Both windows share one of these objects, so they always agree on which board is uncovered.
public class FogTurnHandoff {
    private game currentGame;

    // The player whose board is currently uncovered, or null when both boards are covered.
    private player revealedPlayer = null;

    // Code to run every time the handoff changes (each window redrawing itself).
    private ArrayList<Runnable> changeListeners = new ArrayList<Runnable>();

    public FogTurnHandoff(game currentGame) {
        this.currentGame = currentGame;
    }

    public void addChangeListener(Runnable listener) {changeListeners.add(listener);}

    // Returns true if the passed player's board is uncovered.
    public boolean isRevealedTo(player playerChecking) {
        return revealedPlayer == playerChecking;
    }

    // Returns true if both boards are covered and the passed player should be asked to start their turn.
    public boolean isWaitingToStart(player playerChecking) {
        return revealedPlayer == null && currentGame.isPlayersTurn(playerChecking);
    }

    // Returns true if the passed player has made their move and should be asked to end their turn.
    // The game has already handed the turn to the opponent, but this player's board stays uncovered until they click End Turn.
    public boolean isWaitingToEnd(player playerChecking) {
        return revealedPlayer == playerChecking && currentGame.isOver() == false && currentGame.isPlayersTurn(playerChecking) == false;
    }

    // Uncovers the passed player's board. Only works if it is their turn and both boards are covered.
    public void startTurn(player playerStarting) {
        if (isWaitingToStart(playerStarting) == false) {
            return;
        }

        revealedPlayer = playerStarting;
        notifyListeners();
    }

    // Covers both boards so the next player can sit down. Only works once the player has moved.
    public void endTurn(player playerEnding) {
        if (isWaitingToEnd(playerEnding) == false) {
            return;
        }

        revealedPlayer = null;
        notifyListeners();
    }

    private void notifyListeners() {
        for (Runnable listener : changeListeners) {
            listener.run();
        }
    }
}
