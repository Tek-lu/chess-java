//package Controller;
//
//import Model.*;
//import View.BoardView;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
///**
// * ChessController acts as a mediator between the model and view
// */
//public class ChessController {
//    private GameState gameState;
//    private BoardView boardView;
//
//    public ChessController(GameState gameState, BoardView boardView) {
//        this.gameState = gameState;
//        this.boardView = boardView;
//
//        // Initialize the view with the current state
//        updateView();
//    }
//
//    public boolean makeMove(Position from, Position to) {
//        Board board = gameState.getBoard();
//        Piece piece = board.getPiece(from);
//
//        // Check if there is a piece at the from position and it's the current player's turn
//        if (piece == null || piece.getColor() != gameState.getCurrentTurn()) {
//            return false;
//        }
//
//        // Find the corresponding move in the legal moves list
//        List<Move> legalMoves = piece.getLegalMoves(board);
//        Move selectedMove = null;
//
//        for (Move move : legalMoves) {
//            if (move.getTo().equals(to)) {
//                selectedMove = move;
//                break;
//            }
//        }
//
//        // If the move is legal, make it
//        if (selectedMove != null) {
//            boolean success = gameState.makeMove(selectedMove);
//            if (success) {
//                updateView();
//                checkGameOver();
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    private void updateView() {
//        boardView.updateBoard(gameState.getBoard());
//        boardView.setCurrentPlayer(gameState.getCurrentTurn());
//    }
//
//    private void checkGameOver() {
//        if (gameState.isGameOver()) {
//            boardView.showGameOverMessage(gameState.getGameResult());
//        }
//    }
//
//    public void resetGame() {
//        gameState.resetGame();
//        updateView();
//    }
//
//    public List<Position> getHighlightedSquares(Position position) {
//        Board board = gameState.getBoard();
//        Piece piece = board.getPiece(position);
//
//        if (piece != null && piece.getColor() == gameState.getCurrentTurn()) {
//            List<Move> legalMoves = piece.getLegalMoves(board);
//            List<Position> positions = new ArrayList<>();
//
//            for (Move move : legalMoves) {
//                positions.add(move.getTo());
//            }
//
//            return positions;
//        }
//
//        return Collections.emptyList();
//    }
//}
package Controller;

import Model.Board;
import Model.GameState;
import Model.Move;
import Model.Piece;
import Model.PieceColor;
import Model.Position;
import View.GameWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Main controller for the chess game that connects the model and view.
 * Handles game logic and user interactions.
 */
public class ChessController implements ChessControllerInterface {
    private GameState gameState;
    private GameWindow view;
    private String gameMode;

    /**
     * Constructs a new ChessController.
     */
    public ChessController() {
        this.gameState = new GameState();
    }

    /**
     * Sets the game view.
     *
     * @param view The GameWindow view
     */
    public void setView(GameWindow view) {
        this.view = view;
    }

    /**
     * Starts a new game with the specified game mode.
     *
     * @param gameMode The game mode to use
     */
    public void startNewGame(String gameMode) {
        this.gameMode = gameMode;
        gameState.resetGame();
        updateView();

        // If playing against computer and computer goes first, make computer move
        if (gameMode.equals("Computer vs Computer") ||
                (gameMode.equals("Player vs Computer") && gameState.getCurrentTurn() == PieceColor.BLACK)) {
            makeComputerMove();
        }
    }

    /**
     * Makes a move from the source position to the target position.
     *
     * @param fromPosition The source position
     * @param toPosition The target position
     * @return True if the move was successful, false otherwise
     */
    public boolean makeMove(Position fromPosition, Position toPosition) {
        // Get the piece at the source position
        Board board = gameState.getBoard();
        Piece piece = board.getPiece(fromPosition);

        if (piece == null || piece.getColor() != gameState.getCurrentTurn()) {
            return false;
        }

        // Find a legal move that matches the from and to positions
        List<Move> legalMoves = getLegalMovesForPiece(piece);
        Move moveToMake = null;

        for (Move move : legalMoves) {
            if (move.getFrom().equals(fromPosition) && move.getTo().equals(toPosition)) {
                moveToMake = move;
                break;
            }
        }

        if (moveToMake == null) {
            return false;
        }

        // Make the move
        boolean successful = gameState.makeMove(moveToMake);

        if (successful) {
            updateView();

            // Check if the game is over
            if (gameState.isGameOver()) {
                view.showGameOver(gameState.getGameResult());
                return true;
            }

            // If playing against computer, make computer move
            if ((gameMode.equals("Player vs Computer") && gameState.getCurrentTurn() == PieceColor.BLACK) ||
                    gameMode.equals("Computer vs Computer")) {
                makeComputerMove();
            }
        }

        return successful;
    }

    /**
     * Makes a computer move using a simple AI.
     */
    private void makeComputerMove() {
        // Simple AI: choose a random legal move
        List<Move> legalMoves = getAllLegalMoves(gameState.getCurrentTurn());

        if (!legalMoves.isEmpty()) {
            // For now, just pick the first legal move
            // This could be improved with a proper chess AI
            Move computerMove = legalMoves.get(0);

            // Make the move after a short delay
            new Thread(() -> {
                try {
                    Thread.sleep(500); // 0.5 second delay
                    gameState.makeMove(computerMove);
                    updateView();

                    // Check if the game is over
                    if (gameState.isGameOver()) {
                        view.showGameOver(gameState.getGameResult());
                    } else if (gameMode.equals("Computer vs Computer")) {
                        // Continue with next computer move
                        makeComputerMove();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    /**
     * Updates the view to reflect the current game state.
     */
    private void updateView() {
        if (view != null) {
            view.refreshBoard();

            // Update status message
            String statusMessage = gameState.getCurrentTurn().toString() + "'s turn";

            // Add check notification if applicable
            if (gameState.getBoard().isInCheck(gameState.getCurrentTurn())) {
                statusMessage += " (CHECK)";
            }

            view.updateStatus(statusMessage);
        }
    }

    /**
     * Gets the piece at the specified position.
     *
     * @param position The position to check
     * @return The piece at the position, or null if empty
     */
    public Piece getPieceAt(Position position) {
        return gameState.getBoard().getPiece(position);
    }

    /**
     * Gets all legal moves for the piece at the specified position.
     *
     * @param position The position of the piece
     * @return A list of positions representing legal moves
     */
    public List<Position> getLegalMovePositions(Position position) {
        Piece piece = getPieceAt(position);

        if (piece == null) {
            return new ArrayList<>();
        }

        List<Move> legalMoves = getLegalMovesForPiece(piece);
        List<Position> movePositions = new ArrayList<>();

        for (Move move : legalMoves) {
            movePositions.add(move.getTo());
        }

        return movePositions;
    }

    /**
     * Gets all legal moves for the specified piece.
     *
     * @param piece The piece to check
     * @return A list of legal moves
     */
    private List<Move> getLegalMovesForPiece(Piece piece) {
        return piece.getLegalMoves(gameState.getBoard());
    }

    /**
     * Gets all legal moves for the specified color.
     *
     * @param color The color to get moves for
     * @return A list of all legal moves
     */
    private List<Move> getAllLegalMoves(PieceColor color) {
        return gameState.getBoard().getLegalMoves(color);
    }

    /**
     * Forfeits the current game for the specified color.
     */
    public void surrender() {
        PieceColor currentPlayer = gameState.getCurrentTurn();
        PieceColor winner = (currentPlayer == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
        String message = winner.toString() + " wins by surrender";
        view.showGameOver(message);
    }

    /**
     * Gets the current board state.
     *
     * @return The chess board
     */
    public Board getBoard() {
        return gameState.getBoard();
    }

    /**
     * Gets the color of the player whose turn it is.
     *
     * @return The color of the current player
     */
    public PieceColor getCurrentTurn() {
        return gameState.getCurrentTurn();
    }

    /**
     * Checks if the game is over.
     *
     * @return True if the game is over, false otherwise
     */
    public boolean isGameOver() {
        return gameState.isGameOver();
    }
}