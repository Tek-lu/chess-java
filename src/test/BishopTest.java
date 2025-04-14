package test;

import Model.piece.*;
import Model.Position;
import Model.PieceColor;
import Model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class BishopTest {

    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
        // Clear the board for clean tests
        // Implementation would depend on Board class implementation
    }

    @Test
    public void testBishopInitialMoves() {
        // Create a bishop at a specific position in the middle of the board
        Bishop bishop = new Bishop(PieceColor.WHITE, new Position(3, 3));
        // Place the bishop on the board
        // Implementation would depend on Board class

        // Get legal moves
        List<Move> legalMoves = bishop.getLegalMoves(board);

        // Bishop should have up to 13 possible moves from this position (if board is empty)
        // 4 diagonals with up to 3+3+3+4 moves
        assertEquals(13, legalMoves.size());

        // Verify moves along all four diagonals
        boolean containsAllExpectedMoves = true;

        // Check diagonal moves
        // Upper-right diagonal
        for (int i = 1; i <= 4; i++) {
            Position expectedPos = new Position(3 + i, 3 + i);
            if (board.isValidPosition(expectedPos)) {
                boolean found = false;
                for (Move move : legalMoves) {
                    if (move.getTo().equals(expectedPos)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    containsAllExpectedMoves = false;
                    break;
                }
            }
        }

        // Upper-left diagonal
        for (int i = 1; i <= 3; i++) {
            Position expectedPos = new Position(3 - i, 3 + i);
            if (board.isValidPosition(expectedPos)) {
                boolean found = false;
                for (Move move : legalMoves) {
                    if (move.getTo().equals(expectedPos)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    containsAllExpectedMoves = false;
                    break;
                }
            }
        }

        // Lower-right diagonal
        for (int i = 1; i <= 3; i++) {
            Position expectedPos = new Position(3 + i, 3 - i);
            if (board.isValidPosition(expectedPos)) {
                boolean found = false;
                for (Move move : legalMoves) {
                    if (move.getTo().equals(expectedPos)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    containsAllExpectedMoves = false;
                    break;
                }
            }
        }

        // Lower-left diagonal
        for (int i = 1; i <= 3; i++) {
            Position expectedPos = new Position(3 - i, 3 - i);
            if (board.isValidPosition(expectedPos)) {
                boolean found = false;
                for (Move move : legalMoves) {
                    if (move.getTo().equals(expectedPos)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    containsAllExpectedMoves = false;
                    break;
                }
            }
        }

        assertTrue(containsAllExpectedMoves);
    }

    @Test
    public void testBishopCaptureMove() {
        // Create a white bishop
        Bishop whiteBishop = new Bishop(PieceColor.WHITE, new Position(3, 3));

        // Create a black piece at a position the bishop can attack
        Piece blackPiece = new Pawn(PieceColor.BLACK, new Position(5, 5));

        // Place pieces on board
        // Implementation would depend on Board class

        // Get legal moves
        List<Move> legalMoves = whiteBishop.getLegalMoves(board);

        // Check that the capture move exists
        boolean captureFound = false;
        for (Move move : legalMoves) {
            if (move.getTo().equals(new Position(5, 5)) && move.getCapturedPiece() == blackPiece) {
                captureFound = true;
                break;
            }
        }

        assertTrue(captureFound);
    }

    @Test
    public void testBishopBlockedByAlly() {
        // Create a white bishop
        Bishop whiteBishop = new Bishop(PieceColor.WHITE, new Position(3, 3));

        // Create a white piece at a position the bishop could move to
        Piece whitePiece = new Pawn(PieceColor.WHITE, new Position(5, 5));

        // Place pieces on board
        // Implementation would depend on Board class

        // Get legal moves
        List<Move> legalMoves = whiteBishop.getLegalMoves(board);

        // Check that the blocked position is not in legal moves
        boolean blockedMoveAbsent = true;
        for (Move move : legalMoves) {
            if (move.getTo().equals(new Position(5, 5))) {
                blockedMoveAbsent = false;
                break;
            }
        }

        assertTrue(blockedMoveAbsent);

        // Also check that positions beyond the blocking piece are not in legal moves
        boolean beyondBlockAbsent = true;
        for (Move move : legalMoves) {
            if (move.getTo().equals(new Position(6, 6)) ||
                    move.getTo().equals(new Position(7, 7))) {
                beyondBlockAbsent = false;
                break;
            }
        }

        assertTrue(beyondBlockAbsent);
    }

    @Test
    public void testBishopBlockedByOpponent() {
        // Create a white bishop
        Bishop whiteBishop = new Bishop(PieceColor.WHITE, new Position(3, 3));

        // Create a black piece at a position the bishop could move to
        Piece blackPiece = new Pawn(PieceColor.BLACK, new Position(5, 5));

        // Place pieces on board
        // Implementation would depend on Board class

        // Get legal moves
        List<Move> legalMoves = whiteBishop.getLegalMoves(board);

        // Check that the position with opponent piece is in legal moves (can capture)
        boolean captureFound = false;
        for (Move move : legalMoves) {
            if (move.getTo().equals(new Position(5, 5))) {
                captureFound = true;
                break;
            }
        }

        assertTrue(captureFound);

        // Check that positions beyond the blocking piece are not in legal moves
        boolean beyondBlockAbsent = true;
        for (Move move : legalMoves) {
            if (move.getTo().equals(new Position(6, 6)) ||
                    move.getTo().equals(new Position(7, 7))) {
                beyondBlockAbsent = false;
                break;
            }
        }

        assertTrue(beyondBlockAbsent);
    }

    @Test
    public void testBishopCornerOfBoard() {
        // Create a bishop at the corner of the board
        Bishop bishop = new Bishop(PieceColor.WHITE, new Position(0, 0));

        // Place the bishop on the board
        // Implementation would depend on Board class

        // Get legal moves
        List<Move> legalMoves = bishop.getLegalMoves(board);

        // Bishop at corner should have only 7 possible moves along the diagonal
        assertEquals(7, legalMoves.size());

        // Verify all expected positions are on the diagonal from (0,0)
        boolean allMovesOnDiagonal = true;
        for (Move move : legalMoves) {
            Position pos = move.getTo();
            // Verify positions are on the diagonal (x == y)
            if (pos.getX() != pos.getY()) {
                allMovesOnDiagonal = false;
                break;
            }
        }

        assertTrue(allMovesOnDiagonal);
    }

    @Test
    public void testBishopAttackPositions() {
        // Create a bishop at a specific position
        Bishop bishop = new Bishop(PieceColor.WHITE, new Position(3, 3));

        // Create a piece that blocks one diagonal
        Piece blockingPiece = new Pawn(PieceColor.BLACK, new Position(5, 5));

        // Place pieces on board
        // Implementation would depend on Board class

        // Get attack positions
        List<Position> attackPositions = bishop.getAttackPositions(board);

        // Verify attack positions include the blocking piece position
        boolean includesBlockingPos = false;
        for (Position pos : attackPositions) {
            if (pos.equals(new Position(5, 5))) {
                includesBlockingPos = true;
                break;
            }
        }
        assertTrue(includesBlockingPos);

        // Verify positions beyond the blocking piece are not attacked
        boolean beyondBlockAbsent = true;
        for (Position pos : attackPositions) {
            if (pos.equals(new Position(6, 6)) ||
                    pos.equals(new Position(7, 7))) {
                beyondBlockAbsent = false;
                break;
            }
        }
        assertTrue(beyondBlockAbsent);
    }
}