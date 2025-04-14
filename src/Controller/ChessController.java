package Controller;

import Model.*;
import View.BoardView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ChessController acts as a mediator between the model and view
 */
public class ChessController {
    private GameState gameState;
    private BoardView boardView;

    public ChessController(GameState gameState, BoardView boardView) {
        this.gameState = gameState;
        this.boardView = boardView;

        // Initialize the view with the current state
        updateView();
    }

    public boolean makeMove(Position from, Position to) {
        Board board = gameState.getBoard();
        Piece piece = board.getPiece(from);

        // Check if there is a piece at the from position and it's the current player's turn
        if (piece == null || piece.getColor() != gameState.getCurrentTurn()) {
            return false;
        }

        // Find the corresponding move in the legal moves list
        List<Move> legalMoves = piece.getLegalMoves(board);
        Move selectedMove = null;

        for (Move move : legalMoves) {
            if (move.getTo().equals(to)) {
                selectedMove = move;
                break;
            }
        }

        // If the move is legal, make it
        if (selectedMove != null) {
            boolean success = gameState.makeMove(selectedMove);
            if (success) {
                updateView();
                checkGameOver();
                return true;
            }
        }

        return false;
    }

    private void updateView() {
        boardView.updateBoard(gameState.getBoard());
        boardView.setCurrentPlayer(gameState.getCurrentTurn());
    }

    private void checkGameOver() {
        if (gameState.isGameOver()) {
            boardView.showGameOverMessage(gameState.getGameResult());
        }
    }

    public void resetGame() {
        gameState.resetGame();
        updateView();
    }

    public List<Position> getHighlightedSquares(Position position) {
        Board board = gameState.getBoard();
        Piece piece = board.getPiece(position);

        if (piece != null && piece.getColor() == gameState.getCurrentTurn()) {
            List<Move> legalMoves = piece.getLegalMoves(board);
            List<Position> positions = new ArrayList<>();

            for (Move move : legalMoves) {
                positions.add(move.getTo());
            }

            return positions;
        }

        return Collections.emptyList();
    }
}