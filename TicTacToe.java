import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToe {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);

        WelcomePage welcomePage = new WelcomePage(frame);
        frame.add(welcomePage);
        frame.setVisible(true);
    }
}

class WelcomePage extends JPanel {
    private JFrame frame;
    private Image backgroundImage;

    public WelcomePage(JFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        // Load the background image
        backgroundImage = new ImageIcon("/Users/srinivascr/Desktop/tictactoe.java/image.jpeg").getImage();
        JLabel welcomeLabel = new JLabel("Welcome to Tic Tac Toe application!");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18 ));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(welcomeLabel, gbc);

        JButton startButton = new JButton("Start Game");
        gbc.gridy = 1;
        add(startButton, gbc);

        startButton.addActionListener(e -> {
            SymbolSelectionFrame symbolSelectionFrame = new SymbolSelectionFrame(frame);
            frame.setContentPane(symbolSelectionFrame);
            frame.revalidate();
        });
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}

class SymbolSelectionFrame extends JPanel {
    private JFrame frame;
    private char player1Symbol = 'X';
    private char player2Symbol = 'O';
    private boolean vsComputer = false;
    private boolean easyMode = true;
    private JComboBox<Character> player1SymbolBox;
    private JComboBox<Character> player2SymbolBox;
    private Image backgroundImage;

    public SymbolSelectionFrame(JFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Load the background image
        backgroundImage = new ImageIcon("/Users/srinivascr/Desktop/tictactoe.java/image.jpeg").getImage();

        JLabel titleLabel = new JLabel("Select Symbols and Game Mode");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18 ));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        JLabel player1Label = new JLabel("Player 1 Symbol:");
        player1Label.setForeground(Color.WHITE);
        gbc.gridy = 1;
        add(player1Label, gbc);

        Character[] symbols = {'X', 'O', 'A', 'B', 'C','$','#','*'};
        player1SymbolBox = new JComboBox<>(symbols);
        player1SymbolBox.setSelectedItem(player1Symbol);
        player1SymbolBox.addActionListener(e -> validateSymbols());
        gbc.gridx = 1;
        add(player1SymbolBox, gbc);

        JLabel player2Label = new JLabel("Player 2 Symbol:");
        player2Label.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(player2Label, gbc);

        player2SymbolBox = new JComboBox<>(symbols);
        player2SymbolBox.setSelectedItem(player2Symbol);
        player2SymbolBox.addActionListener(e -> validateSymbols());
        gbc.gridx = 1;
        add(player2SymbolBox, gbc);

        JButton twoPlayerButton = new JButton("Two Player");
        twoPlayerButton.addActionListener(e -> {
            vsComputer = false;
            enableDifficultyButtons(false);
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(twoPlayerButton, gbc);

        JButton vsComputerButton = new JButton("VS Computer");
        vsComputerButton.addActionListener(e -> {
            vsComputer = true;
            enableDifficultyButtons(true);
        });
        gbc.gridx = 1;
        add(vsComputerButton, gbc);

        JLabel difficultyLabel = new JLabel("Select Difficulty:");
        difficultyLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(difficultyLabel, gbc);

        JRadioButton easyButton = new JRadioButton("Easy");
        easyButton.setForeground(Color.WHITE);
        easyButton.setSelected(true);
        JRadioButton hardButton = new JRadioButton("Hard");
        hardButton.setForeground(Color.WHITE);

        ButtonGroup difficultyGroup = new ButtonGroup();
        difficultyGroup.add(easyButton);
        difficultyGroup.add(hardButton);

        gbc.gridwidth = 1;
        gbc.gridy = 5;
        add(easyButton, gbc);
        gbc.gridx = 1;
        add(hardButton, gbc);

        easyButton.addActionListener(e -> easyMode = true);
        hardButton.addActionListener(e -> easyMode = false);

        // Initially disable difficulty buttons until VS Computer is selected
        enableDifficultyButtons(false);

        JButton startGameButton = new JButton("Start Game");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        add(startGameButton, gbc);

        startGameButton.addActionListener(e -> {
            if (validateSymbols()) {
                GameBoard gameBoard = new GameBoard(frame, (char) player1SymbolBox.getSelectedItem(), (char) player2SymbolBox.getSelectedItem(), vsComputer, easyMode);
                frame.setContentPane(gameBoard);
                frame.revalidate();
            } else {
                JOptionPane.showMessageDialog(frame, "Player 1 and Player 2 cannot have the same symbol.", "Symbol Selection Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private boolean validateSymbols() {
        return player1SymbolBox.getSelectedItem() != player2SymbolBox.getSelectedItem();
    }

    private void enableDifficultyButtons(boolean enable) {
        Component[] components = this.getComponents();
        for (Component component : components) {
            if (component instanceof JRadioButton) {
                component.setEnabled(enable);
            }
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
class GameBoard extends JPanel {
    private JFrame frame;
    private JButton[][] buttons;
    private char currentPlayer;
    private char player1Symbol;
    private char player2Symbol;
    private boolean vsComputer;
    private boolean easyMode;
    private Image backgroundImage;

    public GameBoard(JFrame frame, char player1Symbol, char player2Symbol, boolean vsComputer, boolean easyMode) {
        this.frame = frame;
        this.buttons = new JButton[3][3];
        this.currentPlayer = player1Symbol;
        this.player1Symbol = player1Symbol;
        this.player2Symbol = player2Symbol;
        this.vsComputer = vsComputer;
        this.easyMode = easyMode;
        setLayout(new BorderLayout());
        
        JLabel gameLabel = new JLabel( "\t\t\t\t\t\t\t\t\t\t\tTic Tac Toe Game Board:\t\t\t\t\tplayer1 starts the game!");
        add(gameLabel, BorderLayout.NORTH);

        JButton backButton = new JButton("Back to Symbol Selection");
        backButton.addActionListener(e -> {
            SymbolSelectionFrame symbolSelectionFrame = new SymbolSelectionFrame(frame);
            frame.setContentPane(symbolSelectionFrame);
            frame.revalidate();
        });
        add(backButton, BorderLayout.SOUTH);

        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 60));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                boardPanel.add(buttons[i][j]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        if (vsComputer && currentPlayer == player2Symbol) {
            computerMove();
        }
    }

    private void restartGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
            }
        }
        currentPlayer = player1Symbol;
    }

    private boolean checkWinner() {
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(buttons[i][1].getText()) &&
                    buttons[i][1].getText().equals(buttons[i][2].getText()) &&
                    !buttons[i][0].getText().equals("")) {
                return true;
            }
            if (buttons[0][i].getText().equals(buttons[1][i].getText()) &&
                    buttons[1][i].getText().equals(buttons[2][i].getText()) &&
                    !buttons[0][i].getText().equals("")) {
                return true;
            }
        }
        if (buttons[0][0].getText().equals(buttons[1][1].getText()) &&
                buttons[1][1].getText().equals(buttons[2][2].getText()) &&
                !buttons[0][0].getText().equals("")) {
            return true;
        }
        if (buttons[0][2].getText().equals(buttons[1][1].getText()) &&
                buttons[1][1].getText().equals(buttons[2][0].getText()) &&
                !buttons[0][2].getText().equals("")) {
            return true;
        }
        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void computerMove() {
        int[] move;
        if (easyMode) {
            move = getRandomMove();
        } else {
            move = minimax(buttons, player2Symbol);
        }
        buttons[move[1]][move[2]].setText(String.valueOf(player2Symbol));
        buttons[move[1]][move[2]].setEnabled(false);
        if (checkWinner()) {
            JOptionPane.showMessageDialog(frame, "Computer wins!");
            restartGame();
        } else if (isBoardFull()) {
            JOptionPane.showMessageDialog(frame, "The game is a tie!");
            restartGame();
        } else {
            currentPlayer = player1Symbol;
        }
    }

    private int[] getRandomMove() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    return new int[]{0, i, j};
                }
            }
        }
        return new int[]{0, -1, -1};
    }

    private int[] minimax(JButton[][] board, char player) {
        int bestScore = (player == player2Symbol) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int[] bestMove = {-1, -1, -1};

        if (checkWinner()) {
            return new int[]{(player == player2Symbol) ? -1 : 1, -1, -1};
        } else if (isBoardFull()) {
            return new int[]{0, -1, -1};
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].getText().equals("")) {
                    board[i][j].setText(String.valueOf(player));
                    int score = minimax(board, (player == player2Symbol) ? player1Symbol : player2Symbol)[0];
                    board[i][j].setText("");

                    if ((player == player2Symbol && score > bestScore) ||
                            (player == player1Symbol && score < bestScore)) {
                        bestScore = score;
                        bestMove = new int[]{score, i, j};
                    }
                }
            }
        }
        return bestMove;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
    private class ButtonClickListener implements ActionListener {
        private int x, y;

        public ButtonClickListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void actionPerformed(ActionEvent e) {
            if (buttons[x][y].getText().equals("")) {
                buttons[x][y].setText(String.valueOf(currentPlayer));
                buttons[x][y].setEnabled(false);
                if (checkWinner()) {
                    JOptionPane.showMessageDialog(frame, "Player " + currentPlayer + " wins!");
                    restartGame();
                } else if (isBoardFull()) {
                    JOptionPane.showMessageDialog(frame, "The game is a tie!");
                    restartGame();
                } else {
                    currentPlayer = (currentPlayer == player1Symbol) ? player2Symbol : player1Symbol;
                    if (vsComputer && currentPlayer == player2Symbol) {
                        computerMove();
                    }
                }
            }
        }
    }
}