package tictactoeJava;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.swing.border.AbstractBorder;

public class Main {

    //* Size sa board
    static int boardWidth = 600;
    static int boardHeight = 700;

    static JFrame frame = new JFrame("Tic-Tac-Toe");
    static JLabel textLabel = new JLabel();
    static JPanel textPanel = new JPanel();
    static JPanel boardPanel = new JPanel();
    static JPanel controlPanel = new JPanel();

    //* Default 3x3 ang board size
    static JButton[][] board = new JButton[3][3];
    static String playerX = "Player X";
    static String playerO = "Player O";
    static String currentPlayer = playerX;

    static boolean gameOver = false;
    static int turns = 0;

    // Scores for the X or O player
    static int playerXScore = 0;
    static int playerOScore = 0;

    static JPanel menuPanel = new JPanel(); // Menu panel

    public static void main(String[] args) {
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create menu panel
        createMenu();

        frame.setVisible(true);
    }

    // Create the Menu Panel
    static void createMenu() {
        menuPanel.setLayout(new BorderLayout());

        // Title Label for the Menu
        JLabel menuLabel = new JLabel("Tic-Tac-Toe", SwingConstants.CENTER);
        menuLabel.setFont(new Font("Kristen ITC", Font.BOLD, 75));
        menuLabel.setForeground(Color.DARK_GRAY);
        menuLabel.setBackground(Color.WHITE);
        menuPanel.add(menuLabel, BorderLayout.CENTER);  // Add title at the center

        // Panel for Buttons (to keep them separate from the title)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // Vertical layout for buttons
        buttonPanel.setOpaque(false);  // Make the panel background transparent

        // Play Button
        JButton playButton = new JButton("Play Game");
        playButton.setFont(new Font("Verdana", Font.BOLD, 30));
        playButton.addActionListener(e -> startGame());
        playButton.setFocusable(false);
        playButton.setBackground(Color.green);
        playButton.setForeground(Color.white);
        buttonPanel.add(playButton);  // Add Play Button to button panel
        buttonPanel.add(Box.createVerticalStrut(20));  // Add space between buttons

        // Exit Button
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Verdana", Font.BOLD, 30));
        exitButton.addActionListener(e -> System.exit(0));
        exitButton.setFocusable(false);
        exitButton.setBackground(Color.red);
        exitButton.setForeground(Color.white);
        buttonPanel.add(exitButton);  // Add Exit Button to button panel

        menuPanel.add(buttonPanel, BorderLayout.SOUTH);  // Add button panel to the bottom of the menu

        // Set background image for the menu
        menuPanel.setBorder(new BackgroundImageBorder("src/main/resources/background_image.jpg"));

        frame.add(menuPanel); // Add menu to frame
    }

    // Start the game after clicking "Play Game"
    static void startGame() {
        frame.remove(menuPanel); // Remove the menu panel
        frame.repaint();

        // Set up the game UI
        textLabel.setBackground(Color.black);
        textLabel.setForeground(Color.white);
        textLabel.setFont(new Font("Verdana", Font.BOLD, 45));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Tic-Tac-Toe");
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(3, 3));
        boardPanel.setBackground(Color.darkGray);

        // Setting up the background 
        boardPanel.setBorder(new BackgroundImageBorder("src/main/resources/background_image.jpg"));

        frame.add(boardPanel);

        controlPanel.setLayout(new FlowLayout());
        frame.add(controlPanel, BorderLayout.SOUTH);

        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> restartGame());
        controlPanel.add(restartButton);

        updateScorePanel();

        createBoard();
    }

    static void createBoard() {
        boardPanel.removeAll();

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JButton tile = new JButton();
                board[r][c] = tile;
                boardPanel.add(tile);

                tile.setBackground(Color.darkGray);
                tile.setForeground(Color.white);
                tile.setFont(new Font("Kristen ITC", Font.BOLD, 120));
                tile.setFocusable(false);

                tile.setBorder(new RoundedBorder(30, Color.white));

                tile.addActionListener(e -> handleTileClick(tile));
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    static void handleTileClick(JButton tile) {
        if (gameOver) {
            return;
        }
        if (tile.getText().equals("")) {
            tile.setText(currentPlayer.equals(playerX) ? "X" : "O");
            turns++;
            checkWinner();
            if (!gameOver) {
                currentPlayer = currentPlayer.equals(playerX) ? playerO : playerX;
                textLabel.setText(currentPlayer + "'s turn.");
            }
        }
    }

    static void checkWinner() {
        // horizontal
        for (int r = 0; r < 3; r++) {
            if (board[r][0].getText().equals("")) {
                continue;
            }
            if (board[r][0].getText().equals(board[r][1].getText())
                    && board[r][1].getText().equals(board[r][2].getText())) {
                highlightWinner(r, 0, r, 1, r, 2);
                gameOver = true;
                return;
            }
        }

        // vertical
        for (int c = 0; c < 3; c++) {
            if (board[0][c].getText().equals("")) {
                continue;
            }
            if (board[0][c].getText().equals(board[1][c].getText())
                    && board[1][c].getText().equals(board[2][c].getText())) {
                highlightWinner(0, c, 1, c, 2, c);
                gameOver = true;
                return;
            }
        }

        // diagonal
        if (board[0][0].getText().equals(board[1][1].getText())
                && board[1][1].getText().equals(board[2][2].getText())
                && !board[0][0].getText().equals("")) {
            highlightWinner(0, 0, 1, 1, 2, 2);
            gameOver = true;
            return;
        }

        // anti-diagonal
        if (board[0][2].getText().equals(board[1][1].getText())
                && board[1][1].getText().equals(board[2][0].getText())
                && !board[0][2].getText().equals("")) {
            highlightWinner(0, 2, 1, 1, 2, 0);
            gameOver = true;
            return;
        }

        if (turns == 9) {
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    setTie(board[r][c]);
                }
            }
            gameOver = true;
        }
    }

    static void highlightWinner(int r1, int c1, int r2, int c2, int r3, int c3) {
        setWinner(board[r1][c1]);
        setWinner(board[r2][c2]);
        setWinner(board[r3][c3]);

        if (board[r1][c1].getText().equals("X")) {
            playerXScore++;
        } else {
            playerOScore++;
        }
        updateScorePanel();
    }

    static void setWinner(JButton tile) {
        tile.setForeground(Color.green);
        tile.setBackground(Color.gray);
        textLabel.setText(currentPlayer + " is the winner!");
    }

    static void setTie(JButton tile) {
        tile.setForeground(Color.orange);
        tile.setBackground(Color.gray);
        textLabel.setText("Tie!");
    }

    static void restartGame() {
        gameOver = false;
        turns = 0;
        currentPlayer = playerX;
        textLabel.setText(currentPlayer + "'s turn.");

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                board[r][c].setText("");
                board[r][c].setForeground(Color.white);
                board[r][c].setBackground(Color.darkGray);
            }
        }
    }

    static void updateScorePanel() {
        if (controlPanel.getComponentCount() > 1) {
            controlPanel.remove(1);
        }

        JLabel scoreLabel = new JLabel("X: " + playerXScore + " | O: " + playerOScore);
        scoreLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        scoreLabel.setForeground(Color.black);

        controlPanel.add(scoreLabel, BorderLayout.CENTER);

        controlPanel.revalidate();
        controlPanel.repaint();
    }
}

// Custom Border Class for the background
class BackgroundImageBorder implements Border {

    private final BufferedImage image;

    public BackgroundImageBorder(String imagePath) {
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (Exception e) {
            throw new RuntimeException("Error loading image: " + imagePath, e);
        }
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(50, 50, 50, 50); // Adjust as needed
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.drawImage(image, x, y, width, height, null);
    }
}

// Class to make the button rounder
class RoundedBorder extends AbstractBorder {

    private final int radius;
    private final Color borderColor;

    public RoundedBorder(int radius, Color borderColor) {
        this.radius = radius;
        this.borderColor = borderColor;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(10, 10, 10, 10);
    }
}
