package Model;

public class Move {
    private final Position from;
    private final Position to;
    private final Piece piece;
    private final Piece capturedPiece;
    private final boolean isPromotion;
    private final boolean isCastling;
    private final boolean isEnPassant;

    private Move(Builder builder) {
        this.from = builder.from;
        this.to = builder.to;
        this.piece = builder.piece;
        this.capturedPiece = builder.capturedPiece;
        this.isPromotion = builder.isPromotion;
        this.isCastling = builder.isCastling;
        this.isEnPassant = builder.isEnPassant;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public Piece getPiece() {
        return piece;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public boolean isPromotion() {
        return isPromotion;
    }

    public boolean isCastling() {
        return isCastling;
    }

    public boolean isEnPassant() {
        return isEnPassant;
    }

    // Builder pattern for Move construction
    public static class Builder {
        private Position from;
        private Position to;
        private Piece piece;
        private Piece capturedPiece;
        private boolean isPromotion;
        private boolean isCastling;
        private boolean isEnPassant;

        public Builder from(Position from) {
            this.from = from;
            return this;
        }

        public Builder to(Position to) {
            this.to = to;
            return this;
        }

        public Builder piece(Piece piece) {
            this.piece = piece;
            return this;
        }

        public Builder capturedPiece(Piece capturedPiece) {
            this.capturedPiece = capturedPiece;
            return this;
        }

        public Builder isPromotion(boolean isPromotion) {
            this.isPromotion = isPromotion;
            return this;
        }

        public Builder isCastling(boolean isCastling) {
            this.isCastling = isCastling;
            return this;
        }

        public Builder isEnPassant(boolean isEnPassant) {
            this.isEnPassant = isEnPassant;
            return this;
        }

        public Move build() {
            return new Move(this);
        }
    }
}
