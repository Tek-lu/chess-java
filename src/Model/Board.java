package Model;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

@SuppressWarnings("serial")
public class Board extends JPanel implements MouseListener, MouseMotionListener {
	// Resource location constants for piece images
    private static final String RESOURCES_WBISHOP_PNG = "wbishop.png";
	private static final String RESOURCES_BBISHOP_PNG = "bbishop.png";
	private static final String RESOURCES_WKNIGHT_PNG = "wknight.png";
	private static final String RESOURCES_BKNIGHT_PNG = "bknight.png";
	private static final String RESOURCES_WROOK_PNG = "wrook.png";
	private static final String RESOURCES_BROOK_PNG = "brook.png";
	private static final String RESOURCES_WKING_PNG = "wking.png";
	private static final String RESOURCES_BKING_PNG = "bking.png";
	private static final String RESOURCES_BQUEEN_PNG = "bqueen.png";
	private static final String RESOURCES_WQUEEN_PNG = "wqueen.png";
	private static final String RESOURCES_WPAWN_PNG = "wpawn.png";
	private static final String RESOURCES_BPAWN_PNG = "bpawn.png";
	
	// Logical and graphical representations of board
	private final Square[][] board;
    private final GameWindow g;
    
    // List of pieces and whether they are movable
    public final LinkedList<Piece> Bpieces;
    public final LinkedList<Piece> Wpieces;
    public List<Square> movable;
    
    private boolean whiteTurn;

    private Piece currPiece;
    private int currX;
    private int currY;
    
    private CheckmateDetector cmd;
    
    public Board(GameWindow g) {
        this.g = g;
        board = new Square[8][8];
        Bpieces = new LinkedList<Piece>();
        Wpieces = new LinkedList<Piece>();
        setLayout(new GridLayout(8, 8, 0, 0));

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                int xMod = x % 2;
                int yMod = y % 2;

                if ((xMod == 0 && yMod == 0) || (xMod == 1 && yMod == 1)) {
                    board[x][y] = new Square(this, 1, y, x);
                    this.add(board[x][y]);
                } else {
                    board[x][y] = new Square(this, 0, y, x);
                    this.add(board[x][y]);
                }
            }
        }

        initializePieces();

        this.setPreferredSize(new Dimension(400, 400));
        this.setMaximumSize(new Dimension(400, 400));
        this.setMinimumSize(this.getPreferredSize());
        this.setSize(new Dimension(400, 400));

        whiteTurn = true;

    }

    private void initializePieces() {
    	
        for (int x = 0; x < 8; x++) {
            board[1][x].put(new Pawn(0, board[1][x], RESOURCES_BPAWN_PNG));
            board[6][x].put(new Pawn(1, board[6][x], RESOURCES_WPAWN_PNG));
        }
        
        board[7][3].put(new Queen(1, board[7][3], RESOURCES_WQUEEN_PNG));
        board[0][3].put(new Queen(0, board[0][3], RESOURCES_BQUEEN_PNG));
        
        King bk = new King(0, board[0][4], RESOURCES_BKING_PNG);
        King wk = new King(1, board[7][4], RESOURCES_WKING_PNG);
        board[0][4].put(bk);
        board[7][4].put(wk);

        board[0][0].put(new Rook(0, board[0][0], RESOURCES_BROOK_PNG));
        board[0][7].put(new Rook(0, board[0][7], RESOURCES_BROOK_PNG));
        board[7][0].put(new Rook(1, board[7][0], RESOURCES_WROOK_PNG));
        board[7][7].put(new Rook(1, board[7][7], RESOURCES_WROOK_PNG));

        board[0][1].put(new Knight(0, board[0][1], RESOURCES_BKNIGHT_PNG));
        board[0][6].put(new Knight(0, board[0][6], RESOURCES_BKNIGHT_PNG));
        board[7][1].put(new Knight(1, board[7][1], RESOURCES_WKNIGHT_PNG));
        board[7][6].put(new Knight(1, board[7][6], RESOURCES_WKNIGHT_PNG));

        board[0][2].put(new Bishop(0, board[0][2], RESOURCES_BBISHOP_PNG));
        board[0][5].put(new Bishop(0, board[0][5], RESOURCES_BBISHOP_PNG));
        board[7][2].put(new Bishop(1, board[7][2], RESOURCES_WBISHOP_PNG));
        board[7][5].put(new Bishop(1, board[7][5], RESOURCES_WBISHOP_PNG));
        
        
        for(int y = 0; y < 2; y++) {
            for (int x = 0; x < 8; x++) {
                Bpieces.add(board[y][x].getOccupyingPiece());
                Wpieces.add(board[7-y][x].getOccupyingPiece());
            }
        }
        
        cmd = new CheckmateDetector(this, Wpieces, Bpieces, wk, bk);
    }

    public Square[][] getSquareArray() {
        return this.board;
    }

    public boolean getTurn() {
        return whiteTurn;
    }

    public void setCurrPiece(Piece p) {
        this.currPiece = p;
    }

    public Piece getCurrPiece() {
        return this.currPiece;
    }

    @Override
    public void paintComponent(Graphics g) {
        // super.paintComponent(g);

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Square sq = board[y][x];
                sq.paintComponent(g);
            }
        }

        if (currPiece != null) {
            if ((currPiece.getColor() == 1 && whiteTurn)
                    || (currPiece.getColor() == 0 && !whiteTurn)) {
                final Image i = currPiece.getImage();
                g.drawImage(i, currX, currY, null);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        currX = e.getX();
        currY = e.getY();

        Square sq = (Square) this.getComponentAt(new Point(e.getX(), e.getY()));

        if (sq.isOccupied()) {
            currPiece = sq.getOccupyingPiece();
            if (currPiece.getColor() == 0 && whiteTurn)
                return;
            if (currPiece.getColor() == 1 && !whiteTurn)
                return;
            sq.setDisplay(false);
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Square sq = (Square) this.getComponentAt(new Point(e.getX(), e.getY()));

        if (currPiece != null) {
            if (currPiece.getColor() == 0 && whiteTurn)
                return;
            if (currPiece.getColor() == 1 && !whiteTurn)
                return;

            List<Square> legalMoves = currPiece.getLegalMoves(this);
            movable = cmd.getAllowableSquares(whiteTurn);

            if (legalMoves.contains(sq) && movable.contains(sq)
                    && cmd.testMove(currPiece, sq)) {
                sq.setDisplay(true);
                currPiece.move(sq);
                cmd.update();

                if (cmd.blackCheckMated()) {
                    currPiece = null;
                    repaint();
                    this.removeMouseListener(this);
                    this.removeMouseMotionListener(this);
                    g.checkmateOccurred(0);
                } else if (cmd.whiteCheckMated()) {
                    currPiece = null;
                    repaint();
                    this.removeMouseListener(this);
                    this.removeMouseMotionListener(this);
                    g.checkmateOccurred(1);
                } else {
                    currPiece = null;
                    whiteTurn = !whiteTurn;
                    movable = cmd.getAllowableSquares(whiteTurn);
                }

            } else {
                currPiece.getPosition().setDisplay(true);
                currPiece = null;
            }
        }

        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currX = e.getX() - 24;
        currY = e.getY() - 24;

        repaint();
    }

    // Irrelevant methods, do nothing for these mouse behaviors
    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Component of the Chess game that detects check mates in the game.
     *
     * @author Jussi Lundstedt
     *
     */
    public static class CheckmateDetector {
        private Board b;
        private LinkedList<Piece> wPieces;
        private LinkedList<Piece> bPieces;
        private LinkedList<Square> movableSquares;
        private final LinkedList<Square> squares;
        private King bk;
        private King wk;
        private HashMap<Square,List<Piece>> wMoves;
        private HashMap<Square,List<Piece>> bMoves;

        /**
         * Constructs a new instance of Model.Board.CheckmateDetector on a given board. By
         * convention should be called when the board is in its initial state.
         *
         * @param b The board which the detector monitors
         * @param wPieces White pieces on the board.
         * @param bPieces Black pieces on the board.
         * @param wk Piece object representing the white king
         * @param bk Piece object representing the black king
         */
        public CheckmateDetector(Board b, LinkedList<Piece> wPieces,
                                 LinkedList<Piece> bPieces, King wk, King bk) {
            this.b = b;
            this.wPieces = wPieces;
            this.bPieces = bPieces;
            this.bk = bk;
            this.wk = wk;

            // Initialize other fields
            squares = new LinkedList<Square>();
            movableSquares = new LinkedList<Square>();
            wMoves = new HashMap<Square,List<Piece>>();
            bMoves = new HashMap<Square,List<Piece>>();

            Square[][] brd = b.getSquareArray();

            // add all squares to squares list and as hashmap keys
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    squares.add(brd[y][x]);
                    wMoves.put(brd[y][x], new LinkedList<Piece>());
                    bMoves.put(brd[y][x], new LinkedList<Piece>());
                }
            }

            // update situation
            update();
        }

        /**
         * Updates the object with the current situation of the game.
         */
        public void update() {
            // Iterators through pieces
            Iterator<Piece> wIter = wPieces.iterator();
            Iterator<Piece> bIter = bPieces.iterator();

            // empty moves and movable squares at each update
            for (List<Piece> pieces : wMoves.values()) {
                pieces.removeAll(pieces);
            }

            for (List<Piece> pieces : bMoves.values()) {
                pieces.removeAll(pieces);
            }

            movableSquares.removeAll(movableSquares);

            // Add each move white and black can make to map
            while (wIter.hasNext()) {
                Piece p = wIter.next();

                if (!p.getClass().equals(King.class)) {
                    if (p.getPosition() == null) {
                        wIter.remove();
                        continue;
                    }

                    List<Square> mvs = p.getLegalMoves(b);
                    Iterator<Square> iter = mvs.iterator();
                    while (iter.hasNext()) {
                        List<Piece> pieces = wMoves.get(iter.next());
                        pieces.add(p);
                    }
                }
            }

            while (bIter.hasNext()) {
                Piece p = bIter.next();

                if (!p.getClass().equals(King.class)) {
                    if (p.getPosition() == null) {
                        wIter.remove();
                        continue;
                    }

                    List<Square> mvs = p.getLegalMoves(b);
                    Iterator<Square> iter = mvs.iterator();
                    while (iter.hasNext()) {
                        List<Piece> pieces = bMoves.get(iter.next());
                        pieces.add(p);
                    }
                }
            }
        }

        /**
         * Checks if the black king is threatened
         * @return boolean representing whether the black king is in check.
         */
        public boolean blackInCheck() {
            update();
            Square sq = bk.getPosition();
            if (wMoves.get(sq).isEmpty()) {
                movableSquares.addAll(squares);
                return false;
            } else return true;
        }

        /**
         * Checks if the white king is threatened
         * @return boolean representing whether the white king is in check.
         */
        public boolean whiteInCheck() {
            update();
            Square sq = wk.getPosition();
            if (bMoves.get(sq).isEmpty()) {
                movableSquares.addAll(squares);
                return false;
            } else return true;
        }

        /**
         * Checks whether black is in checkmate.
         * @return boolean representing if black player is checkmated.
         */
        public boolean blackCheckMated() {
            boolean checkmate = true;
            // Check if black is in check
            if (!this.blackInCheck()) return false;

            // If yes, check if king can evade
            if (canEvade(wMoves, bk)) checkmate = false;

            // If no, check if threat can be captured
            List<Piece> threats = wMoves.get(bk.getPosition());
            if (canCapture(bMoves, threats, bk)) checkmate = false;

            // If no, check if threat can be blocked
            if (canBlock(threats, bMoves, bk)) checkmate = false;

            // If no possible ways of removing check, checkmate occurred
            return checkmate;
        }

        /**
         * Checks whether white is in checkmate.
         * @return boolean representing if white player is checkmated.
         */
        public boolean whiteCheckMated() {
            boolean checkmate = true;
            // Check if white is in check
            if (!this.whiteInCheck()) return false;

            // If yes, check if king can evade
            if (canEvade(bMoves, wk)) checkmate = false;

            // If no, check if threat can be captured
            List<Piece> threats = bMoves.get(wk.getPosition());
            if (canCapture(wMoves, threats, wk)) checkmate = false;

            // If no, check if threat can be blocked
            if (canBlock(threats, wMoves, wk)) checkmate = false;

            // If no possible ways of removing check, checkmate occurred
            return checkmate;
        }

        /*
         * Helper method to determine if the king can evade the check.
         * Gives a false positive if the king can capture the checking piece.
         */
        private boolean canEvade(Map<Square,List<Piece>> tMoves, King tKing) {
            boolean evade = false;
            List<Square> kingsMoves = tKing.getLegalMoves(b);
            Iterator<Square> iterator = kingsMoves.iterator();

            // If king is not threatened at some square, it can evade
            while (iterator.hasNext()) {
                Square sq = iterator.next();
                if (!testMove(tKing, sq)) continue;
                if (tMoves.get(sq).isEmpty()) {
                    movableSquares.add(sq);
                    evade = true;
                }
            }

            return evade;
        }

        /*
         * Helper method to determine if the threatening piece can be captured.
         */
        private boolean canCapture(Map<Square,List<Piece>> poss,
                                   List<Piece> threats, King k) {

            boolean capture = false;
            if (threats.size() == 1) {
                Square sq = threats.get(0).getPosition();

                if (k.getLegalMoves(b).contains(sq)) {
                    movableSquares.add(sq);
                    if (testMove(k, sq)) {
                        capture = true;
                    }
                }

                List<Piece> caps = poss.get(sq);
                ConcurrentLinkedDeque<Piece> capturers = new ConcurrentLinkedDeque<Piece>();
                capturers.addAll(caps);

                if (!capturers.isEmpty()) {
                    movableSquares.add(sq);
                    for (Piece p : capturers) {
                        if (testMove(p, sq)) {
                            capture = true;
                        }
                    }
                }
            }

            return capture;
        }

        /*
         * Helper method to determine if check can be blocked by a piece.
         */
        private boolean canBlock(List<Piece> threats,
                                 Map <Square,List<Piece>> blockMoves, King k) {
            boolean blockable = false;

            if (threats.size() == 1) {
                Square ts = threats.get(0).getPosition();
                Square ks = k.getPosition();
                Square[][] brdArray = b.getSquareArray();

                if (ks.getXNum() == ts.getXNum()) {
                    int max = Math.max(ks.getYNum(), ts.getYNum());
                    int min = Math.min(ks.getYNum(), ts.getYNum());

                    for (int i = min + 1; i < max; i++) {
                        List<Piece> blks =
                                blockMoves.get(brdArray[i][ks.getXNum()]);
                        ConcurrentLinkedDeque<Piece> blockers =
                                new ConcurrentLinkedDeque<Piece>();
                        blockers.addAll(blks);

                        if (!blockers.isEmpty()) {
                            movableSquares.add(brdArray[i][ks.getXNum()]);

                            for (Piece p : blockers) {
                                if (testMove(p,brdArray[i][ks.getXNum()])) {
                                    blockable = true;
                                }
                            }

                        }
                    }
                }

                if (ks.getYNum() == ts.getYNum()) {
                    int max = Math.max(ks.getXNum(), ts.getXNum());
                    int min = Math.min(ks.getXNum(), ts.getXNum());

                    for (int i = min + 1; i < max; i++) {
                        List<Piece> blks =
                                blockMoves.get(brdArray[ks.getYNum()][i]);
                        ConcurrentLinkedDeque<Piece> blockers =
                                new ConcurrentLinkedDeque<Piece>();
                        blockers.addAll(blks);

                        if (!blockers.isEmpty()) {

                            movableSquares.add(brdArray[ks.getYNum()][i]);

                            for (Piece p : blockers) {
                                if (testMove(p, brdArray[ks.getYNum()][i])) {
                                    blockable = true;
                                }
                            }

                        }
                    }
                }

                Class<? extends Piece> tC = threats.get(0).getClass();

                if (tC.equals(Queen.class) || tC.equals(Bishop.class)) {
                    int kX = ks.getXNum();
                    int kY = ks.getYNum();
                    int tX = ts.getXNum();
                    int tY = ts.getYNum();

                    if (kX > tX && kY > tY) {
                        for (int i = tX + 1; i < kX; i++) {
                            tY++;
                            List<Piece> blks =
                                    blockMoves.get(brdArray[tY][i]);
                            ConcurrentLinkedDeque<Piece> blockers =
                                    new ConcurrentLinkedDeque<Piece>();
                            blockers.addAll(blks);

                            if (!blockers.isEmpty()) {
                                movableSquares.add(brdArray[tY][i]);

                                for (Piece p : blockers) {
                                    if (testMove(p, brdArray[tY][i])) {
                                        blockable = true;
                                    }
                                }
                            }
                        }
                    }

                    if (kX > tX && tY > kY) {
                        for (int i = tX + 1; i < kX; i++) {
                            tY--;
                            List<Piece> blks =
                                    blockMoves.get(brdArray[tY][i]);
                            ConcurrentLinkedDeque<Piece> blockers =
                                    new ConcurrentLinkedDeque<Piece>();
                            blockers.addAll(blks);

                            if (!blockers.isEmpty()) {
                                movableSquares.add(brdArray[tY][i]);

                                for (Piece p : blockers) {
                                    if (testMove(p, brdArray[tY][i])) {
                                        blockable = true;
                                    }
                                }
                            }
                        }
                    }

                    if (tX > kX && kY > tY) {
                        for (int i = tX - 1; i > kX; i--) {
                            tY++;
                            List<Piece> blks =
                                    blockMoves.get(brdArray[tY][i]);
                            ConcurrentLinkedDeque<Piece> blockers =
                                    new ConcurrentLinkedDeque<Piece>();
                            blockers.addAll(blks);

                            if (!blockers.isEmpty()) {
                                movableSquares.add(brdArray[tY][i]);

                                for (Piece p : blockers) {
                                    if (testMove(p, brdArray[tY][i])) {
                                        blockable = true;
                                    }
                                }
                            }
                        }
                    }

                    if (tX > kX && tY > kY) {
                        for (int i = tX - 1; i > kX; i--) {
                            tY--;
                            List<Piece> blks =
                                    blockMoves.get(brdArray[tY][i]);
                            ConcurrentLinkedDeque<Piece> blockers =
                                    new ConcurrentLinkedDeque<Piece>();
                            blockers.addAll(blks);

                            if (!blockers.isEmpty()) {
                                movableSquares.add(brdArray[tY][i]);

                                for (Piece p : blockers) {
                                    if (testMove(p, brdArray[tY][i])) {
                                        blockable = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return blockable;
        }

        /**
         * Method to get a list of allowable squares that the player can move.
         * Defaults to all squares, but limits available squares if player is in
         * check.
         * @param b boolean representing whether it's white player's turn (if yes,
         * true)
         * @return List of squares that the player can move into.
         */
        public List<Square> getAllowableSquares(boolean b) {
            movableSquares.removeAll(movableSquares);
            if (whiteInCheck()) {
                whiteCheckMated();
            } else if (blackInCheck()) {
                blackCheckMated();
            }
            return movableSquares;
        }

        /**
         * Tests a move a player is about to make to prevent making an illegal move
         * that puts the player in check.
         * @param p Piece moved
         * @param sq Square to which p is about to move
         * @return false if move would cause a check
         */
        public boolean testMove(Piece p, Square sq) {
            Piece c = sq.getOccupyingPiece();

            boolean movetest = true;
            Square init = p.getPosition();

            p.move(sq);
            update();

            if (p.getColor() == 0 && blackInCheck()) movetest = false;
            else if (p.getColor() == 1 && whiteInCheck()) movetest = false;

            p.move(init);
            if (c != null) sq.put(c);

            update();

            movableSquares.addAll(squares);
            return movetest;
        }

    }
}