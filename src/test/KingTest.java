package test;

import Model.piece.*;
import Model.Position;
import Model.PieceColor;
import Model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class KingTest {

    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
        // Clear the board for clean tests
        clearBoard(board);
    }

    // Helper method to clear the board
    private void clearBoard(Board board) {
        // Remove all pieces to start with a clean board for each test
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Position pos = new Position(x, y);
                Piece piece = board.getPiece(pos);
                if (piece != null) {
                    // Remove the piece (implementation depends on your Board class)
                    // For this example, we're assuming setting null works
                    board.removePiece(pos);
                }
            }
        }
    }

    @Test
    public void testKingInitialMoves() {
        // Create a king at a specific position
        King king = new King(PieceColor.WHITE, new Position(3, 3));
        board.placePiece(king);

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
        board.placePiece(whiteKing);

        // Create a black piece at a position the king can attack
        Piece blackPiece = new Pawn(PieceColor.BLACK, new Position(4, 4));
        board.placePiece(blackPiece);

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
        board.placePiece(whiteKing);

        // Create a white piece at a position the king could move to
        Piece whitePiece = new Pawn(PieceColor.WHITE, new Position(4, 4));
        board.placePiece(whitePiece);

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
        board.placePiece(king);

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
        board.placePiece(whiteKing);

        // Create a black rook that attacks a position the king could move to
        Rook blackRook = new Rook(PieceColor.BLACK, new Position(7, 4));
        board.placePiece(blackRook);

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
        board.placePiece(king);

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

    // NEW TESTS FOR CASTLING FUNCTIONALITY

    @Test
    public void testKingsideCastling() {
        // Setup the board for kingside castling
        King whiteKing = new King(PieceColor.WHITE, new Position(4, 7));
        Rook whiteRook = new Rook(PieceColor.WHITE, new Position(7, 7));

        board.placePiece(whiteKing);
        board.placePiece(whiteRook);

        // Ensure the king and rook haven't moved yet
        whiteKing.setHasMoved(false);
        whiteRook.setHasMoved(false);

        // Get legal moves for the king
        List<Move> legalMoves = whiteKing.getLegalMoves(board);

        // Check for castling move
        boolean hasCastlingMove = false;
        Move castlingMove = null;

        for (Move move : legalMoves) {
            if (move.isCastling() && move.getTo().equals(new Position(6, 7))) {
                hasCastlingMove = true;
                castlingMove = move;
                break;
            }
        }

        assertTrue(hasCastlingMove, "King should have kingside castling move available");

        // Execute the castling move and verify positions
        if (castlingMove != null) {
            board.makeMove(castlingMove);

            // King should now be at g1 (6,7)
            Piece kingAfterCastling = board.getPiece(new Position(6, 7));
            assertTrue(kingAfterCastling instanceof King);

            // Rook should now be at f1 (5,7)
            Piece rookAfterCastling = board.getPiece(new Position(5, 7));
            assertTrue(rookAfterCastling instanceof Rook);

            // Original positions should be empty
            assertNull(board.getPiece(new Position(4, 7)));
            assertNull(board.getPiece(new Position(7, 7)));
        }
    }

    @Test
    public void testQueensideCastling() {
        // Setup the board for queenside castling
        King whiteKing = new King(PieceColor.WHITE, new Position(4, 7));
        Rook whiteRook = new Rook(PieceColor.WHITE, new Position(0, 7));

        board.placePiece(whiteKing);
        board.placePiece(whiteRook);

        // Ensure the king and rook haven't moved yet
        whiteKing.setHasMoved(false);
        whiteRook.setHasMoved(false);

        // Get legal moves for the king
        List<Move> legalMoves = whiteKing.getLegalMoves(board);

        // Check for castling move
        boolean hasCastlingMove = false;
        Move castlingMove = null;

        for (Move move : legalMoves) {
            if (move.isCastling() && move.getTo().equals(new Position(2, 7))) {
                hasCastlingMove = true;
                castlingMove = move;
                break;
            }
        }

        assertTrue(hasCastlingMove, "King should have queenside castling move available");

        // Execute the castling move and verify positions
        if (castlingMove != null) {
            board.makeMove(castlingMove);

            // King should now be at c1 (2,7)
            Piece kingAfterCastling = board.getPiece(new Position(2, 7));
            assertTrue(kingAfterCastling instanceof King);

            // Rook should now be at d1 (3,7)
            Piece rookAfterCastling = board.getPiece(new Position(3, 7));
            assertTrue(rookAfterCastling instanceof Rook);

            // Original positions should be empty
            assertNull(board.getPiece(new Position(4, 7)));
            assertNull(board.getPiece(new Position(0, 7)));
        }
    }

    @Test
    public void testCastlingNotAllowedAfterKingMoved() {
        // Setup the board
        King whiteKing = new King(PieceColor.WHITE, new Position(4, 7));
        Rook whiteRook = new Rook(PieceColor.WHITE, new Position(7, 7));

        board.placePiece(whiteKing);
        board.placePiece(whiteRook);

        // Set that king has already moved
        whiteKing.setHasMoved(true);
        whiteRook.setHasMoved(false);

        // Get legal moves for the king
        List<Move> legalMoves = whiteKing.getLegalMoves(board);

        // Check that no castling moves are available
        boolean hasCastlingMove = false;

        for (Move move : legalMoves) {
            if (move.isCastling()) {
                hasCastlingMove = true;
                break;
            }
        }

        assertFalse(hasCastlingMove, "Castling should not be allowed after king has moved");
    }

    @Test
    public void testCastlingNotAllowedAfterRookMoved() {
        // Setup the board
        King whiteKing = new King(PieceColor.WHITE, new Position(4, 7));
        Rook whiteRook = new Rook(PieceColor.WHITE, new Position(7, 7));

        board.placePiece(whiteKing);
        board.placePiece(whiteRook);

        // Set that rook has already moved
        whiteKing.setHasMoved(false);
        whiteRook.setHasMoved(true);

        // Get legal moves for the king
        List<Move> legalMoves = whiteKing.getLegalMoves(board);

        // Check that no kingside castling move is available
        boolean hasKingsideCastlingMove = false;

        for (Move move : legalMoves) {
            if (move.isCastling() && move.getTo().equals(new Position(6, 7))) {
                hasKingsideCastlingMove = true;
                break;
            }
        }

        assertFalse(hasKingsideCastlingMove, "Kingside castling should not be allowed after rook has moved");
    }

    @Test
    public void testCastlingNotAllowedWhenSquaresBetweenAreOccupied() {
        // Setup the board
        King whiteKing = new King(PieceColor.WHITE, new Position(4, 7));
        Rook whiteRook = new Rook(PieceColor.WHITE, new Position(7, 7));
        Knight whiteKnight = new Knight(PieceColor.WHITE, new Position(5, 7)); // Place a piece between king and rook

        board.placePiece(whiteKing);
        board.placePiece(whiteRook);
        board.placePiece(whiteKnight);

        // Ensure the king and rook haven't moved
        whiteKing.setHasMoved(false);
        whiteRook.setHasMoved(false);

        // Get legal moves for the king
        List<Move> legalMoves = whiteKing.getLegalMoves(board);

        // Check that no kingside castling move is available
        boolean hasKingsideCastlingMove = false;

        for (Move move : legalMoves) {
            if (move.isCastling() && move.getTo().equals(new Position(6, 7))) {
                hasKingsideCastlingMove = true;
                break;
            }
        }

        assertFalse(hasKingsideCastlingMove, "Castling should not be allowed when squares between king and rook are occupied");
    }

    @Test
    public void testCastlingNotAllowedWhenKingInCheck() {
        // Setup the board
        King whiteKing = new King(PieceColor.WHITE, new Position(4, 7));
        Rook whiteRook = new Rook(PieceColor.WHITE, new Position(7, 7));
        Rook blackRook = new Rook(PieceColor.BLACK, new Position(4, 0)); // Black rook checking the king

        board.placePiece(whiteKing);
        board.placePiece(whiteRook);
        board.placePiece(blackRook);

        // Ensure the king and rook haven't moved
        whiteKing.setHasMoved(false);
        whiteRook.setHasMoved(false);

        // Get legal moves for the king
        List<Move> legalMoves = whiteKing.getLegalMoves(board);

        // Check that no castling move is available when king is in check
        boolean hasCastlingMove = false;

        for (Move move : legalMoves) {
            if (move.isCastling()) {
                hasCastlingMove = true;
                break;
            }
        }

        assertFalse(hasCastlingMove, "Castling should not be allowed when king is in check");
    }

    @Test
    public void testCastlingNotAllowedWhenKingPassesThroughAttackedSquare() {
        // Setup the board
        King whiteKing = new King(PieceColor.WHITE, new Position(4, 7));
        Rook whiteRook = new Rook(PieceColor.WHITE, new Position(7, 7));
        Rook blackRook = new Rook(PieceColor.BLACK, new Position(5, 0)); // Black rook attacking f1 (5,7)

        board.placePiece(whiteKing);
        board.placePiece(whiteRook);
        board.placePiece(blackRook);

        // Ensure the king and rook haven't moved
        whiteKing.setHasMoved(false);
        whiteRook.setHasMoved(false);

        // Get legal moves for the king
        List<Move> legalMoves = whiteKing.getLegalMoves(board);

        // Check that no kingside castling move is available
        boolean hasKingsideCastlingMove = false;

        for (Move move : legalMoves) {
            if (move.isCastling() && move.getTo().equals(new Position(6, 7))) {
                hasKingsideCastlingMove = true;
                break;
            }
        }

        assertFalse(hasKingsideCastlingMove, "Castling should not be allowed when king passes through an attacked square");
    }
}