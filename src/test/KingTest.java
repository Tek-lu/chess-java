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

public class KingTest {

    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
        // Clear the board for clean tests
        // Implementation would depend on Board class implementation
    }

    @Test
    public void testKingInitialMoves() {
        // Create a king at a specific position
        King king = new King(PieceColor.WHITE, new Position(3, 3));
        // Place the king on the board
        // Implementation would depend on Board class

        // Get legal moves
        List<Move> legalMoves = king.getLegalMoves(board);

        // King should have 8 possible moves from the center (if none are under attack)
        assertEquals(8, legalMoves.size());

        // Verify all expected positions are included
        boolean containsAllExpectedMoves = true;
        Position[] expectedPositions = {
                new Position(2, 2), new Position(2, 3), new Position(2, 4),
                new Position(3, 2), new Position(3, 4),
                new Position(4, 2), new Position(4, 3), new Position(4, 4)
        };

        for (Position expectedPos : expectedPositions) {
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

        assertTrue(containsAllExpectedMoves);
    }

    @Test
    public void testKingCaptureMove() {
        // Create a white king
        King whiteKing = new King(PieceColor.WHITE, new Position(3, 3));

        // Create a black piece at a position the king can attack
        Piece blackPiece = new Pawn(PieceColor.BLACK, new Position(4, 4));

        // Place pieces on board
        // Implementation would depend on Board class

        // Get legal moves
        List<Move> legalMoves = whiteKing.getLegalMoves(board);

        // Check that the capture move exists
        boolean captureFound = false;
        for (Move move : legalMoves) {
            if (move.getTo().equals(new Position(4, 4)) && move.getCapturedPiece() == blackPiece) {
                captureFound = true;
                break;
            }
        }

        assertTrue(captureFound);
    }

    @Test
    public void testKingBlockedByAlly() {
        // Create a white king
        King whiteKing = new King(PieceColor.WHITE, new Position(3, 3));

        // Create a white piece at a position the king could move to
        Piece whitePiece = new Pawn(PieceColor.WHITE, new Position(4, 4));

        // Place pieces on board
        // Implementation would depend on Board class

        // Get legal moves
        List<Move> legalMoves = whiteKing.getLegalMoves(board);

        // Check that the blocked position is not in legal moves
        boolean blockedMoveAbsent = true;
        for (Move move : legalMoves) {
            if (move.getTo().equals(new Position(4, 4))) {
                blockedMoveAbsent = false;
                break;
            }
        }

        assertTrue(blockedMoveAbsent);
    }

    @Test
    public void testKingEdgeOfBoard() {
        // Create a king at the edge of the board
        King king = new King(PieceColor.WHITE, new Position(0, 0));

        // Place the king on the board
        // Implementation would depend on Board class

        // Get legal moves
        List<Move> legalMoves = king.getLegalMoves(board);

        // King at corner should have only 3 possible moves
        assertEquals(3, legalMoves.size());

        // Verify the three expected positions
        boolean containsExpectedMoves = true;
        Position[] expectedPositions = {
                new Position(0, 1), new Position(1, 0), new Position(1, 1)
        };

        for (Position expectedPos : expectedPositions) {
            boolean found = false;
            for (Move move : legalMoves) {
                if (move.getTo().equals(expectedPos)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                containsExpectedMoves = false;
                break;
            }
        }

        assertTrue(containsExpectedMoves);
    }

    @Test
    public void testKingCannotMoveToAttackedPosition() {
        // Create a white king
        King whiteKing = new King(PieceColor.WHITE, new Position(3, 3));

        // Create a black rook that attacks a position the king could move to
        Rook blackRook = new Rook(PieceColor.BLACK, new Position(7, 4));

        // Place pieces on board
        // Implementation would depend on Board class

        // Get legal moves
        List<Move> legalMoves = whiteKing.getLegalMoves(board);

        // Check that the attacked position (3,4) is not in legal moves
        boolean attackedMoveAbsent = true;
        for (Move move : legalMoves) {
            if (move.getTo().equals(new Position(3, 4))) {
                attackedMoveAbsent = false;
                break;
            }
        }

        assertTrue(attackedMoveAbsent);
    }

    @Test
    public void testKingGetAttackPositions() {
        // Create a king at a specific position
        King king = new King(PieceColor.WHITE, new Position(3, 3));

        // Get attack positions
        List<Position> attackPositions = king.getAttackPositions(board);

        // King should attack 8 positions around it
        assertEquals(8, attackPositions.size());

        // Verify all expected attack positions are included
        boolean containsAllExpectedPositions = true;
        Position[] expectedPositions = {
                new Position(2, 2), new Position(2, 3), new Position(2, 4),
                new Position(3, 2), new Position(3, 4),
                new Position(4, 2), new Position(4, 3), new Position(4, 4)
        };

        for (Position expectedPos : expectedPositions) {
            boolean found = false;
            for (Position attackPos : attackPositions) {
                if (attackPos.equals(expectedPos)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                containsAllExpectedPositions = false;
                break;
            }
        }

        assertTrue(containsAllExpectedPositions);
    }

    @Test
    public void testKingType() {
        // Create a king
        King king = new King(PieceColor.WHITE, new Position(0, 0));

        // Check the type
        assertEquals("King", king.getType());
    }

    @Test
    public void testKingCopy() {
        // Create original king
        King originalKing = new King(PieceColor.WHITE, new Position(3, 3));

        // Create a copy
        Piece copiedKing = originalKing.copy();

        // Check it's a King
        assertTrue(copiedKing instanceof King);

        // Check color and position are the same
        assertEquals(PieceColor.WHITE, copiedKing.getColor());
        assertEquals(new Position(3, 3), copiedKing.getPosition());
    }
}