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

public class KnightTest {

    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
        // Clear the board for clean tests
        // Implementation would depend on Board class implementation
    }

    @Test
    public void testKnightInitialMoves() {
        // Create a knight at a specific position
        Knight knight = new Knight(PieceColor.WHITE, new Position(3, 3));
        // Place the knight on the board
        // Implementation would depend on Board class

        // Get legal moves
        List<Move> legalMoves = knight.getLegalMoves(board);

        // Knight should have 8 possible moves from the center
        assertEquals(8, legalMoves.size());

        // Verify all expected positions are included
        boolean containsAllExpectedMoves = true;
        Position[] expectedPositions = {
                new Position(1, 2), new Position(2, 1),
                new Position(5, 2), new Position(5, 4),
                new Position(4, 5), new Position(2, 5),
                new Position(1, 4), new Position(4, 1)
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
    public void testKnightCaptureMove() {
        // Create a white knight
        Knight whiteKnight = new Knight(PieceColor.WHITE, new Position(3, 3));

        // Create a black piece at a position the knight can attack
        Piece blackPiece = new Pawn(PieceColor.BLACK, new Position(5, 4));

        // Place pieces on board
        // Implementation would depend on Board class

        // Get legal moves
        List<Move> legalMoves = whiteKnight.getLegalMoves(board);

        // Check that the capture move exists
        boolean captureFound = false;
        for (Move move : legalMoves) {
            if (move.getTo().equals(new Position(5, 4)) && move.getCapturedPiece() == blackPiece) {
                captureFound = true;
                break;
            }
        }

        assertTrue(captureFound);
    }

    @Test
    public void testKnightBlockedByAlly() {
        // Create a white knight
        Knight whiteKnight = new Knight(PieceColor.WHITE, new Position(3, 3));

        // Create a white piece at a position the knight could move to
        Piece whitePiece = new Pawn(PieceColor.WHITE, new Position(5, 4));

        // Place pieces on board
        // Implementation would depend on Board class

        // Get legal moves
        List<Move> legalMoves = whiteKnight.getLegalMoves(board);

        // Check that the blocked position is not in legal moves
        boolean blockedMoveAbsent = true;
        for (Move move : legalMoves) {
            if (move.getTo().equals(new Position(5, 4))) {
                blockedMoveAbsent = false;
                break;
            }
        }

        assertTrue(blockedMoveAbsent);
    }

    @Test
    public void testKnightEdgeOfBoard() {
        // Create a knight at the edge of the board
        Knight knight = new Knight(PieceColor.WHITE, new Position(0, 0));

        // Place the knight on the board
        // Implementation would depend on Board class

        // Get legal moves
        List<Move> legalMoves = knight.getLegalMoves(board);

        // Knight at corner should have only 2 possible moves
        assertEquals(2, legalMoves.size());

        // Verify the two expected positions
        boolean containsExpectedMoves = false;
        for (Move move : legalMoves) {
            Position pos = move.getTo();
            if ((pos.equals(new Position(1, 2)) || pos.equals(new Position(2, 1)))) {
                containsExpectedMoves = true;
            } else {
                containsExpectedMoves = false;
                break;
            }
        }

        assertTrue(containsExpectedMoves);
    }
}