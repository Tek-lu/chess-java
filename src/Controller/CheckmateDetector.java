package Controller;

import Model.Board;
import Model.Move;
import Model.Piece;
import Model.PieceColor;
import Model.Position;
import Model.piece.King;

import java.util.ArrayList;
import java.util.List;

public class CheckmateDetector {
    private Board board;
    private List<Position> movablePositions;

    /**
     * Constructs a new instance of CheckmateDetector on a given board.
     *
     * @param board The board which the detector monitors
     */
    public CheckmateDetector(Board board) {
        this.board = board;
        this.movablePositions = new ArrayList<>();
    }

    /**
     * Checks if the black king is threatened
     * @return boolean representing whether the black king is in check.
     */
    public boolean blackInCheck() {
        return board.isInCheck(PieceColor.BLACK);
    }

    /**
     * Checks if the white king is threatened
     * @return boolean representing whether the white king is in check.
     */
    public boolean whiteInCheck() {
        return board.isInCheck(PieceColor.WHITE);
    }

    /**
     * Checks whether black is in checkmate.
     * @return boolean representing if black player is checkmated.
     */
    public boolean blackCheckMated() {
        return board.isCheckmate(PieceColor.BLACK);
    }

    /**
     * Checks whether white is in checkmate.
     * @return boolean representing if white player is checkmated.
     */
    public boolean whiteCheckMated() {
        return board.isCheckmate(PieceColor.WHITE);
    }

    /**
     * Method to get a list of allowable positions that the player can move.
     * @param isWhiteTurn boolean representing whether it's white player's turn
     * @return List of positions that the player can move into.
     */
    public List<Position> getAllowablePositions(boolean isWhiteTurn) {
        PieceColor playerColor = isWhiteTurn ? PieceColor.WHITE : PieceColor.BLACK;
        List<Move> legalMoves = board.getLegalMoves(playerColor);

        // Clear previous movable positions
        movablePositions.clear();

        // Add target positions from legal moves
        for (Move move : legalMoves) {
            if (!movablePositions.contains(move.getTo())) {
                movablePositions.add(move.getTo());
            }
        }

        return movablePositions;
    }

    /**
     * Tests a move a player is about to make to prevent making an illegal move
     * that puts the player in check.
     * @param piece Piece moved
     * @param targetPosition Position to which piece is about to move
     * @return false if move would cause a check
     */
    public boolean testMove(Piece piece, Position targetPosition) {
        Position originPosition = piece.getPosition();
        Piece capturedPiece = board.getPiece(targetPosition);

        // Build the move
        Move move = new Move.Builder()
                .from(originPosition)
                .to(targetPosition)
                .piece(piece)
                .capturedPiece(capturedPiece)
                .build();

        // Create temporary board for testing
        Board tempBoard = new Board(); // Assuming Board has a copy constructor or we can clone it

        // Make the move on the temp board
        tempBoard.makeMove(move);

        // Check if the king is in check after the move
        return !tempBoard.isInCheck(piece.getColor());
    }

    /**
     * Updates the detector with the current state of the board.
     * This is necessary if the board state has changed outside of this detector.
     * @param board The updated board
     */
    public void updateBoard(Board board) {
        this.board = board;
    }

    /**
     * Determines if the king can evade check by moving.
     * @param king The king to check
     * @return true if the king can move to a safe square
     */
    private boolean canKingEvade(King king) {
        List<Move> kingMoves = king.getLegalMoves(board);
        return !kingMoves.isEmpty();
    }

    /**
     * Determines if the checking piece can be captured.
     * @param color The color of the pieces that might capture the checking piece
     * @return true if the checking piece can be captured
     */
    private boolean canCaptureAttacker(PieceColor color) {
        King king = board.getKing(color);
        PieceColor opponentColor = (color == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
        List<Piece> attackers = getCheckingPieces(king, opponentColor);

        // Can only capture if there's exactly one attacker
        if (attackers.size() != 1) {
            return false;
        }

        Piece attacker = attackers.get(0);
        List<Piece> defenders = board.getPieces(color);

        for (Piece defender : defenders) {
            List<Move> moves = defender.getLegalMoves(board);
            for (Move move : moves) {
                if (move.getTo().equals(attacker.getPosition())) {
                    // Verify this doesn't leave king in check
                    Board tempBoard = new Board(); // Need a copy constructor
                    tempBoard.makeMove(move);
                    if (!tempBoard.isInCheck(color)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Determines if the check can be blocked by interposing a piece.
     * @param color The color of the pieces that might block
     * @return true if the check can be blocked
     */
    private boolean canBlockCheck(PieceColor color) {
        King king = board.getKing(color);
        PieceColor opponentColor = (color == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
        List<Piece> attackers = getCheckingPieces(king, opponentColor);

        // Can only block if there's exactly one attacker
        if (attackers.size() != 1) {
            return false;
        }

        Piece attacker = attackers.get(0);
        String attackerType = attacker.getType();

        // Only queen, rook, or bishop can be blocked
        if (!attackerType.equals("Queen") && !attackerType.equals("Rook") && !attackerType.equals("Bishop")) {
            return false;
        }

        // Get squares between attacker and king
        List<Position> squaresBetween = getSquaresBetween(attacker.getPosition(), king.getPosition());

        // Try to find a piece that can block
        List<Piece> defenders = board.getPieces(color);

        for (Piece defender : defenders) {
            if (defender instanceof King) continue; // King can't block

            List<Move> moves = defender.getLegalMoves(board);
            for (Move move : moves) {
                if (squaresBetween.contains(move.getTo())) {
                    // Verify this doesn't leave king in check
                    Board tempBoard = new Board(); // Need a copy constructor
                    tempBoard.makeMove(move);
                    if (!tempBoard.isInCheck(color)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Gets all pieces that are currently checking the king.
     * @param king The king to check
     * @param opponentColor The color of potential attacking pieces
     * @return List of pieces checking the king
     */
    private List<Piece> getCheckingPieces(King king, PieceColor opponentColor) {
        List<Piece> checkingPieces = new ArrayList<>();
        List<Piece> opponentPieces = board.getPieces(opponentColor);
        Position kingPosition = king.getPosition();

        for (Piece piece : opponentPieces) {
            List<Position> attackPositions = piece.getAttackPositions(board);
            if (attackPositions.contains(kingPosition)) {
                checkingPieces.add(piece);
            }
        }

        return checkingPieces;
    }

    /**
     * Gets all squares between two positions (for straight lines or diagonals).
     * @param start Starting position
     * @param end Ending position
     * @return List of positions between start and end (exclusive)
     */
    private List<Position> getSquaresBetween(Position start, Position end) {
        List<Position> squares = new ArrayList<>();

        int startX = start.getX();
        int startY = start.getY();
        int endX = end.getX();
        int endY = end.getY();

        // Same row
        if (startY == endY) {
            int minX = Math.min(startX, endX);
            int maxX = Math.max(startX, endX);
            for (int x = minX + 1; x < maxX; x++) {
                squares.add(new Position(x, startY));
            }
        }
        // Same column
        else if (startX == endX) {
            int minY = Math.min(startY, endY);
            int maxY = Math.max(startY, endY);
            for (int y = minY + 1; y < maxY; y++) {
                squares.add(new Position(startX, y));
            }
        }
        // Diagonal
        else if (Math.abs(startX - endX) == Math.abs(startY - endY)) {
            int dx = (endX > startX) ? 1 : -1;
            int dy = (endY > startY) ? 1 : -1;

            int x = startX + dx;
            int y = startY + dy;

            while (x != endX && y != endY) {
                squares.add(new Position(x, y));
                x += dx;
                y += dy;
            }
        }

        return squares;
    }
}