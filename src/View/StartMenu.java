//package View;
//
//import View.GameWindow;
//
//import java.awt.BorderLayout;
//import java.awt.Component;
//import java.awt.Image;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//import javax.imageio.ImageIO;
//import javax.swing.Box;
//import javax.swing.ImageIcon;
//import javax.swing.JButton;
//import javax.swing.JComboBox;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JTextField;
//
//public class StartMenu implements Runnable {
//    public void run() {
//        final JFrame startWindow = new JFrame("Chess");
//        // Set window properties
//        startWindow.setLocation(300,100);
//        startWindow.setResizable(false);
//        startWindow.setSize(260, 240);
//
//        Box components = Box.createVerticalBox();
//        startWindow.add(components);
//
//        // Game title
//        final JPanel titlePanel = new JPanel();
//        components.add(titlePanel);
//        final JLabel titleLabel = new JLabel("Chess");
//        titlePanel.add(titleLabel);
//
//        // Black player selections
//        final JPanel blackPanel = new JPanel();
//        components.add(blackPanel, BorderLayout.EAST);
//        final JLabel blackPiece = new JLabel();
//        try {
//            Image blackImg = ImageIO.read(getClass().getResource("bp.png"));
//            blackPiece.setIcon(new ImageIcon(blackImg));
//            blackPanel.add(blackPiece);
//        } catch (Exception e) {
//            System.out.println("Required game file bp.png missing");
//        }
//
//
//
//        final JTextField blackInput = new JTextField("Black", 10);
//        blackPanel.add(blackInput);
//
//        // White player selections
//        final JPanel whitePanel = new JPanel();
//        components.add(whitePanel);
//        final JLabel whitePiece = new JLabel();
//
//        try {
//            Image whiteImg = ImageIO.read(getClass().getResource("wp.png"));
//            whitePiece.setIcon(new ImageIcon(whiteImg));
//            whitePanel.add(whitePiece);
//            startWindow.setIconImage(whiteImg);
//        }  catch (Exception e) {
//            System.out.println("Required game file wp.png missing");
//        }
//
//
//        final JTextField whiteInput = new JTextField("White", 10);
//        whitePanel.add(whiteInput);
//
//        // Timer settings
//        final String[] minSecInts = new String[60];
//        for (int i = 0; i < 60; i++) {
//            if (i < 10) {
//                minSecInts[i] = "0" + Integer.toString(i);
//            } else {
//                minSecInts[i] = Integer.toString(i);
//            }
//        }
//
//        final JComboBox<String> seconds = new JComboBox<String>(minSecInts);
//        final JComboBox<String> minutes = new JComboBox<String>(minSecInts);
//        final JComboBox<String> hours =
//                new JComboBox<String>(new String[] {"0","1","2","3"});
//
//        Box timerSettings = Box.createHorizontalBox();
//
//        hours.setMaximumSize(hours.getPreferredSize());
//        minutes.setMaximumSize(minutes.getPreferredSize());
//        seconds.setMaximumSize(minutes.getPreferredSize());
//
//        timerSettings.add(hours);
//        timerSettings.add(Box.createHorizontalStrut(10));
//        timerSettings.add(seconds);
//        timerSettings.add(Box.createHorizontalStrut(10));
//        timerSettings.add(minutes);
//
//        timerSettings.add(Box.createVerticalGlue());
//
//        components.add(timerSettings);
//
//        // Buttons
//        Box buttons = Box.createHorizontalBox();
//        final JButton quit = new JButton("Quit");
//
//        quit.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//              startWindow.dispose();
//            }
//          });
//
//        final JButton instr = new JButton("Instructions");
//
//        instr.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                JOptionPane.showMessageDialog(startWindow,
//                        "To begin a new game, input player names\n" +
//                        "next to the pieces. Set the clocks and\n" +
//                        "click \"Start\". Setting the timer to all\n" +
//                        "zeroes begins a new untimed game.",
//                        "How to play",
//                        JOptionPane.PLAIN_MESSAGE);
//            }
//          });
//
//        final JButton start = new JButton("Start");
//
//        start.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                String bn = blackInput.getText();
//                String wn = whiteInput.getText();
//                int hh = Integer.parseInt((String) hours.getSelectedItem());
//                int mm = Integer.parseInt((String) minutes.getSelectedItem());
//                int ss = Integer.parseInt((String) seconds.getSelectedItem());
//
//                new GameWindow(bn, wn, hh, mm, ss);
//                startWindow.dispose();
//            }
//          });
//
//        buttons.add(start);
//        buttons.add(Box.createHorizontalStrut(10));
//        buttons.add(instr);
//        buttons.add(Box.createHorizontalStrut(10));
//        buttons.add(quit);
//        components.add(buttons);
//
//        Component space = Box.createGlue();
//        components.add(space);
//
//        startWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        startWindow.setVisible(true);
//    }
//}
package View;

import Controller.ChessController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Start menu UI that allows players to configure and start a new chess game.
 */
public class StartMenu extends JPanel implements Runnable {
    private final GameWindow parentWindow;
    private final ChessController controller;
    private JComboBox<String> gameModeSelector;
    private JButton startButton;

    /**
     * Constructs a new StartMenu within the parent window.
     *
     * @param parentWindow The parent GameWindow
     * @param controller The chess game controller
     */
    public StartMenu(GameWindow parentWindow, ChessController controller) {
        this.parentWindow = parentWindow;
        this.controller = controller;

        // Set up the panel
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Create title and content panels
        JPanel titlePanel = createTitlePanel();
        JPanel contentPanel = createContentPanel();
        JPanel buttonPanel = createButtonPanel();

        // Add panels to layout
        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates the title panel with game title and logo.
     *
     * @return The title panel
     */
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create game title
        JLabel titleLabel = new JLabel("Chess Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components to panel
        panel.add(Box.createVerticalStrut(20));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));

        return panel;
    }

    /**
     * Creates the content panel with game settings.
     *
     * @return The content panel
     */
    private JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create game mode selector
        JLabel gameModeLabel = new JLabel("Select Game Mode:");
        gameModeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameModeLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        String[] gameModes = {"Player vs Player", "Player vs Computer", "Computer vs Computer"};
        gameModeSelector = new JComboBox<>(gameModes);
        gameModeSelector.setMaximumSize(new Dimension(300, 30));
        gameModeSelector.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components to panel
        panel.add(Box.createVerticalGlue());
        panel.add(gameModeLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(gameModeSelector);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    /**
     * Creates the button panel with start game button.
     *
     * @return The button panel
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create start button
        startButton = new JButton("Start Game");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setMaximumSize(new Dimension(200, 50));
        startButton.setFont(new Font("Arial", Font.BOLD, 18));

        // Add action listener to start button
        startButton.addActionListener(e -> {
            String selectedMode = (String) gameModeSelector.getSelectedItem();
            parentWindow.startGame(selectedMode);
        });

        // Add components to panel
        panel.add(Box.createVerticalStrut(20));
        panel.add(startButton);
        panel.add(Box.createVerticalStrut(20));

        return panel;
    }

    @Override
    public void run() {

    }
}
