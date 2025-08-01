import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;

public class TicTacToeGame extends JFrame implements ActionListener {
    private static final int SIZE = 3;
    JButton[][] buttons;
    private char currentPlayer;
    private boolean isGameWon;
    private JLabel statusLabel;
    public Random random;
    private Font buttonFont = new Font("Arial", Font.BOLD, 48);

    public TicTacToeGame() {
        buttons = new JButton[SIZE][SIZE];
        currentPlayer = 'X';
        isGameWon = false;
        random = new Random();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Tic-Tac-Toe Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 450);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Center the window

        JPanel boardPanel = new JPanel(new GridLayout(SIZE, SIZE, 5, 5));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        statusLabel = new JLabel("Player " + currentPlayer + "'s Turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                buttons[row][col] = new JButton();
                buttons[row][col].setFont(buttonFont);
                buttons[row][col].setBackground(Color.WHITE);
                buttons[row][col].setFocusPainted(false);
                buttons[row][col].addActionListener(this);
                boardPanel.add(buttons[row][col]);
            }
        }

        JButton restartButton = new JButton("🔄 Restart");
        restartButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        restartButton.setFocusPainted(false);
        restartButton.setBackground(new Color(30, 144, 255));
        restartButton.setForeground(Color.WHITE);
        restartButton.addActionListener(e -> restartGame());

        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.add(restartButton, BorderLayout.CENTER);

        add(statusLabel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isGameWon) return;

        JButton clickedButton = (JButton) e.getSource();
        if (!clickedButton.getText().isEmpty()) return;

        if (currentPlayer == 'X') {
            clickedButton.setText("X");
            clickedButton.setForeground(Color.RED);
            clickedButton.setEnabled(false);

            if (checkWin()) {
                showWinMessage("Player X wins!");
            } else if (isBoardFull()) {
                showWinMessage("It's a draw!");
            } else {
                currentPlayer = 'O';
                statusLabel.setText("Player O's Turn");
                computerMove();
            }
        }
    }

    private void computerMove() {
        if (!isGameWon && currentPlayer == 'O') {
            int row, col;
            do {
                row = random.nextInt(SIZE);
                col = random.nextInt(SIZE);
            } while (!buttons[row][col].getText().isEmpty());

            buttons[row][col].setText("O");
            buttons[row][col].setForeground(Color.BLUE);
            buttons[row][col].setEnabled(false);

            if (checkWin()) {
                showWinMessage("Player O wins!");
            } else if (isBoardFull()) {
                showWinMessage("It's a draw!");
            } else {
                currentPlayer = 'X';
                statusLabel.setText("Player X's Turn");
            }
        }
    }

    private boolean checkWin() {
        for (int i = 0; i < SIZE; i++) {
            // Check rows
            if (checkLine(buttons[i][0], buttons[i][1], buttons[i][2])) return true;
            // Check columns
            if (checkLine(buttons[0][i], buttons[1][i], buttons[2][i])) return true;
        }
        // Diagonals
        return checkLine(buttons[0][0], buttons[1][1], buttons[2][2]) ||
               checkLine(buttons[0][2], buttons[1][1], buttons[2][0]);
    }

    private boolean checkLine(JButton b1, JButton b2, JButton b3) {
        if (!b1.getText().isEmpty() &&
            b1.getText().equals(b2.getText()) &&
            b2.getText().equals(b3.getText())) {
            // Highlight winning buttons
            b1.setBackground(Color.GREEN);
            b2.setBackground(Color.GREEN);
            b3.setBackground(Color.GREEN);
            isGameWon = true;
            return true;
        }
        return false;
    }

    private boolean isBoardFull() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (buttons[row][col].getText().isEmpty()) return false;
            }
        }
        return true;
    }

    private void showWinMessage(String message) {
        statusLabel.setText(message);
        isGameWon = true;
        disableBoard();
        JOptionPane.showMessageDialog(this, message);
    }

    private void disableBoard() {
        for (JButton[] row : buttons) {
            for (JButton b : row) {
                b.setEnabled(false);
            }
        }
    }

    private void restartGame() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                buttons[row][col].setText("");
                buttons[row][col].setEnabled(true);
                buttons[row][col].setBackground(Color.WHITE);
            }
        }
        currentPlayer = 'X';
        isGameWon = false;
        statusLabel.setText("Player X's Turn");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToeGame::new);
    }
}
