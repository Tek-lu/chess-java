//package View;
//
//import Controller.Clock;
//import Model.Board;
//
//import java.awt.BorderLayout;
//import java.awt.GridLayout;
//import java.awt.Image;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//import javax.imageio.ImageIO;
//import javax.swing.*;
//
//
//public class GameWindow {
//    private JFrame gameWindow;
//
//    public Clock blackClock;
//    public Clock whiteClock;
//
//    private Timer timer;
//
//    private Board board;
//
//
//
//    public GameWindow(String blackName, String whiteName, int hh,
//            int mm, int ss) {
//
//        blackClock = new Clock(hh, ss, mm);
//        whiteClock = new Clock(hh, ss, mm);
//
//        gameWindow = new JFrame("Chess");
//
//
//        try {
//            Image whiteImg = ImageIO.read(getClass().getResource("/resources//wp.png"));
//            gameWindow.setIconImage(whiteImg);
//        } catch (Exception e) {
//            System.out.println("Game file wp.png not found");
//        }
//
//        gameWindow.setLocation(100, 100);
//
//
//        gameWindow.setLayout(new BorderLayout(20,20));
//
//        // Game Data window
//        JPanel gameData = gameDataPanel(blackName, whiteName, hh, mm, ss);
//        gameData.setSize(gameData.getPreferredSize());
//        gameWindow.add(gameData, BorderLayout.NORTH);
//
//        this.board = new Board(this);
//
//        gameWindow.add(board, BorderLayout.CENTER);
//
//        gameWindow.add(buttons(), BorderLayout.SOUTH);
//
//        gameWindow.setMinimumSize(gameWindow.getPreferredSize());
//        gameWindow.setSize(gameWindow.getPreferredSize());
//        gameWindow.setResizable(false);
//
//        gameWindow.pack();
//        gameWindow.setVisible(true);
//        gameWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//    }
//
//// Helper function to create data panel
//
//    private JPanel gameDataPanel(final String bn, final String wn,
//            final int hh, final int mm, final int ss) {
//
//        JPanel gameData = new JPanel();
//        gameData.setLayout(new GridLayout(3,2,0,0));
//
//
//        // PLAYER NAMES
//
//        JLabel w = new JLabel(wn);
//        JLabel b = new JLabel(bn);
//
//        w.setHorizontalAlignment(JLabel.CENTER);
//        w.setVerticalAlignment(JLabel.CENTER);
//        b.setHorizontalAlignment(JLabel.CENTER);
//        b.setVerticalAlignment(JLabel.CENTER);
//
//        w.setSize(w.getMinimumSize());
//        b.setSize(b.getMinimumSize());
//
//        gameData.add(w);
//        gameData.add(b);
//
//        // CLOCKS
//
//        final JLabel bTime = new JLabel(blackClock.getTime());
//        final JLabel wTime = new JLabel(whiteClock.getTime());
//
//        bTime.setHorizontalAlignment(JLabel.CENTER);
//        bTime.setVerticalAlignment(JLabel.CENTER);
//        wTime.setHorizontalAlignment(JLabel.CENTER);
//        wTime.setVerticalAlignment(JLabel.CENTER);
//
//        if (!(hh == 0 && mm == 0 && ss == 0)) {
//            timer = new Timer(1000, null);
//            timer.addActionListener(new ActionListener() {
//                public void actionPerformed(ActionEvent e) {
//                    boolean turn = board.getTurn();
//
//                    if (turn) {
//                        whiteClock.decr();
//                        wTime.setText(whiteClock.getTime());
//
//                        if (whiteClock.outOfTime()) {
//                            timer.stop();
//                            int n = JOptionPane.showConfirmDialog(
//                                    gameWindow,
//                                    bn + " wins by time! Play a new game? \n" +
//                                    "Choosing \"No\" quits the game.",
//                                    bn + " wins!",
//                                    JOptionPane.YES_NO_OPTION);
//
//                            if (n == JOptionPane.YES_OPTION) {
//                                new GameWindow(bn, wn, hh, mm, ss);
//                                gameWindow.dispose();
//                            } else gameWindow.dispose();
//                        }
//                    } else {
//                        blackClock.decr();
//                        bTime.setText(blackClock.getTime());
//
//                        if (blackClock.outOfTime()) {
//                            timer.stop();
//                            int n = JOptionPane.showConfirmDialog(
//                                    gameWindow,
//                                    wn + " wins by time! Play a new game? \n" +
//                                    "Choosing \"No\" quits the game.",
//                                    wn + " wins!",
//                                    JOptionPane.YES_NO_OPTION);
//
//                            if (n == JOptionPane.YES_OPTION) {
//                                new GameWindow(bn, wn, hh, mm, ss);
//                                gameWindow.dispose();
//                            } else gameWindow.dispose();
//                        }
//                    }
//                }
//            });
//            timer.start();
//        } else {
//            wTime.setText("Untimed game");
//            bTime.setText("Untimed game");
//        }
//
//        gameData.add(wTime);
//        gameData.add(bTime);
//
//        gameData.setPreferredSize(gameData.getMinimumSize());
//
//        return gameData;
//    }
//
//    private JPanel buttons() {
//        JPanel buttons = new JPanel();
//        buttons.setLayout(new GridLayout(1, 3, 10, 0));
//
//        final JButton quit = new JButton("Quit");
//
//        quit.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                int n = JOptionPane.showConfirmDialog(
//                        gameWindow,
//                        "Are you sure you want to quit?",
//                        "Confirm quit", JOptionPane.YES_NO_OPTION);
//
//                if (n == JOptionPane.YES_OPTION) {
//                    if (timer != null) timer.stop();
//                    gameWindow.dispose();
//                }
//            }
//          });
//
//        final JButton nGame = new JButton("New game");
//
//        nGame.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                int n = JOptionPane.showConfirmDialog(
//                        gameWindow,
//                        "Are you sure you want to begin a new game?",
//                        "Confirm new game", JOptionPane.YES_NO_OPTION);
//
//                if (n == JOptionPane.YES_OPTION) {
//                    SwingUtilities.invokeLater(new StartMenu());
//                    gameWindow.dispose();
//                }
//            }
//          });
//
//        final JButton instr = new JButton("How to play");
//
//        instr.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                JOptionPane.showMessageDialog(gameWindow,
//                        "Move the chess pieces on the board by clicking\n"
//                        + "and dragging. The game will watch out for illegal\n"
//                        + "moves. You can win either by your opponent running\n"
//                        + "out of time or by checkmating your opponent.\n"
//                        + "\nGood luck, hope you enjoy the game!",
//                        "How to play",
//                        JOptionPane.PLAIN_MESSAGE);
//            }
//          });
//
//        buttons.add(instr);
//        buttons.add(nGame);
//        buttons.add(quit);
//
//        buttons.setPreferredSize(buttons.getMinimumSize());
//
//        return buttons;
//    }
//
//    public void checkmateOccurred (int c) {
//        if (c == 0) {
//            if (timer != null) timer.stop();
//            int n = JOptionPane.showConfirmDialog(
//                    gameWindow,
//                    "White wins by checkmate! Set up a new game? \n" +
//                    "Choosing \"No\" lets you look at the final situation.",
//                    "White wins!",
//                    JOptionPane.YES_NO_OPTION);
//
//            if (n == JOptionPane.YES_OPTION) {
//                SwingUtilities.invokeLater(new StartMenu());
//                gameWindow.dispose();
//            }
//        } else {
//            if (timer != null) timer.stop();
//            int n = JOptionPane.showConfirmDialog(
//                    gameWindow,
//                    "Black wins by checkmate! Set up a new game? \n" +
//                    "Choosing \"No\" lets you look at the final situation.",
//                    "Black wins!",
//                    JOptionPane.YES_NO_OPTION);
//
//            if (n == JOptionPane.YES_OPTION) {
//                SwingUtilities.invokeLater(new StartMenu());
//                gameWindow.dispose();
//            }
//        }
//    }
//}
package View;

import Controller.ChessController;
import Model.GameState;
import Model.PieceColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The main window frame for the Chess game application.
 * Serves as the primary container that holds all UI components.
 */
public class GameWindow extends JFrame {
    private ChessBoardUI chessBoardUI;
    private JPanel sidePanel;
    private JLabel statusLabel;
    private JButton newGameButton;
    private JButton surrenderButton;
    private ChessController controller;
    private StartMenu startMenu;

    /**
     * Constructs a new GameWindow with the given controller.
     *
     * @param controller The chess game controller
     */
    public GameWindow(ChessController controller) {
        this.controller = controller;

        // Set up the window
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize components
        initializeComponents();

        // Display start menu initially
        showStartMenu();
        controller.setView(this); //+ -

        // Set window properties
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Initialize all UI components.
     */
    private void initializeComponents() {
        // Create the chess board UI
        chessBoardUI = new ChessBoardUI(controller);

        // Create side panel with game controls
        createSidePanel();

        // Create start menu
        startMenu = new StartMenu(this, controller);
    }

    /**
     * Creates the side panel with game controls and status information.
     */
    private void createSidePanel() {
        sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Status label
        statusLabel = new JLabel("White's turn");
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Game control buttons
        newGameButton = new JButton("New Game");
        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGameButton.addActionListener(e -> showStartMenu());

        surrenderButton = new JButton("Surrender");
        surrenderButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        surrenderButton.addActionListener(e -> {
            PieceColor currentPlayer = controller.getCurrentTurn();
            PieceColor winner = (currentPlayer == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
            String message = winner.toString() + " wins by surrender";
            showGameOver(message);
        });

        // Add components to side panel
        sidePanel.add(Box.createVerticalGlue());
        sidePanel.add(statusLabel);
        sidePanel.add(Box.createVerticalStrut(20));
        sidePanel.add(newGameButton);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(surrenderButton);
        sidePanel.add(Box.createVerticalGlue());
    }

    /**
     * Shows the start menu and hides the game board.
     */
    public void showStartMenu() {
        getContentPane().removeAll();
        getContentPane().add(startMenu, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    /**
     * Starts a new game with the specified game mode.
     *
     * @param gameMode The selected game mode (e.g., "Player vs Player")
     */
    public void startGame(String gameMode) {
        getContentPane().removeAll();
        getContentPane().add(chessBoardUI, BorderLayout.CENTER);
        getContentPane().add(sidePanel, BorderLayout.EAST);

        // Reset the game state in the controller
        controller.startNewGame(gameMode);

        updateStatus("White's turn");

        revalidate();
        repaint();
    }

    /**
     * Updates the status label with the current game state.
     *
     * @param status The status message to display
     */
    public void updateStatus(String status) {
        statusLabel.setText(status);
    }

    /**
     * Shows a game over dialog with the specified message.
     *
     * @param message The game over message
     */
    public void showGameOver(String message) {
        JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Refreshes the chess board UI to reflect the current game state.
     */
    public void refreshBoard() {
        if (chessBoardUI != null) {
            chessBoardUI.updateBoard();
        }
    }

    /**
     * Returns the ChessBoardUI component.
     *
     * @return The ChessBoardUI
     */
    public ChessBoardUI getChessBoardUI() {
        return chessBoardUI;
    }
}
