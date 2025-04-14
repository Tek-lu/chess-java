package Model;

import View.SquareView;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
//
//public abstract class Piece {
//    private final int color;
//    private SquareView currentSquare;
//    private BufferedImage img;
//
//    public Piece(int color, SquareView initSq, String img_file) {
//        this.color = color;
//        this.currentSquare = initSq;
//
//        try {
//            if (this.img == null) {
//                this.img = ImageIO.read(getClass().getResource(img_file));
//            }
//        } catch (IOException e) {
//            System.out.println("File not found: " + e.getMessage());
//        }
//    }
//
//    public boolean move(SquareView fin) {
//        Piece occup = fin.getOccupyingPiece();
//
//        if (occup != null) {
//            if (occup.getColor() == this.color) return false;
//            else fin.capture(this);
//        }
//
//        currentSquare.removePiece();
//        this.currentSquare = fin;
//        currentSquare.put(this);
//        return true;
//    }
//
//    public SquareView getPosition() {
//        return currentSquare;
//    }
//
//    public void setPosition(SquareView sq) {
//        this.currentSquare = sq;
//    }
//
//    public int getColor() {
//        return color;
//    }
//
//    public Image getImage() {
//        return img;
//    }
//
//    public void draw(Graphics g) {
//        int x = currentSquare.getX();
//        int y = currentSquare.getY();
//
//        g.drawImage(this.img, x, y, null);
//    }
//
//    public int[] getLinearOccupations(SquareView[][] board, int x, int y) {
//        int lastYabove = 0;
//        int lastXright = 7;
//        int lastYbelow = 7;
//        int lastXleft = 0;
//
//        for (int i = 0; i < y; i++) {
//            if (board[i][x].isOccupied()) {
//                if (board[i][x].getOccupyingPiece().getColor() != this.color) {
//                    lastYabove = i;
//                } else lastYabove = i + 1;
//            }
//        }
//
//        for (int i = 7; i > y; i--) {
//            if (board[i][x].isOccupied()) {
//                if (board[i][x].getOccupyingPiece().getColor() != this.color) {
//                    lastYbelow = i;
//                } else lastYbelow = i - 1;
//            }
//        }
//
//        for (int i = 0; i < x; i++) {
//            if (board[y][i].isOccupied()) {
//                if (board[y][i].getOccupyingPiece().getColor() != this.color) {
//                    lastXleft = i;
//                } else lastXleft = i + 1;
//            }
//        }
//
//        for (int i = 7; i > x; i--) {
//            if (board[y][i].isOccupied()) {
//                if (board[y][i].getOccupyingPiece().getColor() != this.color) {
//                    lastXright = i;
//                } else lastXright = i - 1;
//            }
//        }
//
//        int[] occups = {lastYabove, lastYbelow, lastXleft, lastXright};
//
//        return occups;
//    }
//
//    public List<SquareView> getDiagonalOccupations(SquareView[][] board, int x, int y) {
//        LinkedList<SquareView> diagOccup = new LinkedList<SquareView>();
//
//        int xNW = x - 1;
//        int xSW = x - 1;
//        int xNE = x + 1;
//        int xSE = x + 1;
//        int yNW = y - 1;
//        int ySW = y + 1;
//        int yNE = y - 1;
//        int ySE = y + 1;
//
//        while (xNW >= 0 && yNW >= 0) {
//            if (board[yNW][xNW].isOccupied()) {
//                if (board[yNW][xNW].getOccupyingPiece().getColor() == this.color) {
//                    break;
//                } else {
//                    diagOccup.add(board[yNW][xNW]);
//                    break;
//                }
//            } else {
//                diagOccup.add(board[yNW][xNW]);
//                yNW--;
//                xNW--;
//            }
//        }
//
//        while (xSW >= 0 && ySW < 8) {
//            if (board[ySW][xSW].isOccupied()) {
//                if (board[ySW][xSW].getOccupyingPiece().getColor() == this.color) {
//                    break;
//                } else {
//                    diagOccup.add(board[ySW][xSW]);
//                    break;
//                }
//            } else {
//                diagOccup.add(board[ySW][xSW]);
//                ySW++;
//                xSW--;
//            }
//        }
//
//        while (xSE < 8 && ySE < 8) {
//            if (board[ySE][xSE].isOccupied()) {
//                if (board[ySE][xSE].getOccupyingPiece().getColor() == this.color) {
//                    break;
//                } else {
//                    diagOccup.add(board[ySE][xSE]);
//                    break;
//                }
//            } else {
//                diagOccup.add(board[ySE][xSE]);
//                ySE++;
//                xSE++;
//            }
//        }
//
//        while (xNE < 8 && yNE >= 0) {
//            if (board[yNE][xNE].isOccupied()) {
//                if (board[yNE][xNE].getOccupyingPiece().getColor() == this.color) {
//                    break;
//                } else {
//                    diagOccup.add(board[yNE][xNE]);
//                    break;
//                }
//            } else {
//                diagOccup.add(board[yNE][xNE]);
//                yNE--;
//                xNE++;
//            }
//        }
//
//        return diagOccup;
//    }
//
//    // No implementation, to be implemented by each subclass
//    public abstract List<SquareView> getLegalMoves(Board b);
//}

public abstract class Piece {
    private final PieceColor color;
    private Position position;
    private String imageFile;

    public Piece(PieceColor color, Position position) {
        this.color = color;
        this.position = position;
        this.imageFile = determineImageFile();
    }

    protected abstract String determineImageFile();

    public PieceColor getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getImageFile() {
        return imageFile;
    }

    /**
     * Get all legal moves for this piece on the given board
     */
    public abstract List<Move> getLegalMoves(Board board);

    /**
     * Get all positions this piece can attack (useful for checking check)
     */
    public abstract List<Position> getAttackPositions(Board board);

    /**
     * Get the piece type name
     */
    public abstract String getType();

    public boolean moveTo(Position position) {
        this.position = position;
        return true;
    }
    /**
     * Check if the move would leave the king in check
     */
    protected boolean wouldLeaveKingInCheck(Board board, Move move) {
        // Create a temporary board to simulate the move
        Board tempBoard = new Board();
        // TODO: Clone the board state

        // Make the move on the temporary board
        tempBoard.makeMove(move);

        // Check if the king is in check after the move
        return tempBoard.isInCheck(this.getColor());

    }

    public abstract Piece copy();

    @Override
    public String toString() {
        return getColor() + " " + getType() + " at " + position;
    }
}
