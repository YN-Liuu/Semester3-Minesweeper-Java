package BOOM;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

class Block extends JButton {
    int num = 0;
    int x, y;
    boolean isClicked = false;
    boolean isFlag = false;
    public Block(int x, int y) {
        this.x = x;
        this.y = y;
        setFont(new Font("", Font.BOLD, 14));
        setMargin(new Insets(0, 0, 0, 0));
    }
}
public class MineSweeper extends JFrame implements MouseListener {
    private static final int ROW = 10;
    private static final int COL = 10;
    private static final int MINE_COUNT = 10;
    private Block[][] blocks = new Block[ROW][COL];
    private int flagCount = 0;
    private boolean gameOver = false;
    private ImageIcon bombIcon;
    private ImageIcon flagIcon;
    public MineSweeper() {
        setTitle("扫雷");
        setSize(520, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new GridLayout(ROW, COL));
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                blocks[i][j] = new Block(i, j);
                blocks[i][j].addMouseListener(this);
                panel.add(blocks[i][j]);
            }
        }
        add(panel);
        bombIcon = loadIcon("bom3.png");
        flagIcon = loadIcon("flag.png");
        initMines();
    }
    private ImageIcon loadIcon(String name) {
        java.net.URL url = this.getClass().getResource(name);
        if (url == null) {
            System.out.println("图片未找到: " + name);
            return null;
        }
        ImageIcon icon = new ImageIcon(url);
        Image img = icon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
    private void initMines() {
        Random r = new Random();
        int count = 0;
        while (count < MINE_COUNT) {
            int x = r.nextInt(ROW);
            int y = r.nextInt(COL);
            if (blocks[x][y].num != 100) {
                blocks[x][y].num = 100;
                count++;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int nx = x + i;
                        int ny = y + j;
                        if (nx >= 0 && nx < ROW && ny >= 0 && ny < COL
                                && blocks[nx][ny].num != 100) {
                            blocks[nx][ny].num++;
                        }
                    }
                }
            }
        }
    }
    private void click(Block b) {
        if (b.isClicked || b.isFlag) return;
        b.isClicked = true;
        b.setBackground(Color.LIGHT_GRAY);
        if (b.num > 0 && b.num < 100) {
            b.setText(String.valueOf(b.num));
        } else if (b.num == 0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int nx = b.x + i;
                    int ny = b.y + j;
                    if (nx >= 0 && nx < ROW && ny >= 0 && ny < COL) {
                        click(blocks[nx][ny]);
                    }
                }
            }
        }
    }
    public void mouseClicked(MouseEvent e) {
        if (gameOver) return;
        Block b = (Block) e.getSource();
        if (e.getClickCount() == 2 && b.isClicked && b.num > 0) {
            int flagAround = 0;
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int nx = b.x + i;
                    int ny = b.y + j;
                    if (nx >= 0 && nx < ROW && ny >= 0 && ny < COL
                            && blocks[nx][ny].isFlag) {
                        flagAround++;
                    }
                }
            }
            if (flagAround == b.num) {
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int nx = b.x + i;
                        int ny = b.y + j;
                        if (nx >= 0 && nx < ROW && ny >= 0 && ny < COL) {
                            click(blocks[nx][ny]);
                        }
                    }
                }
            }
            return;
        }
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (b.isFlag) return;
            if (b.num == 100) {
                b.setIcon(bombIcon);
                b.setText("");
                JOptionPane.showMessageDialog(this, "Game Over!");
                gameOver = true;
            } else {
                click(b);
            }
        }
        if (SwingUtilities.isRightMouseButton(e)) {
            if (!b.isClicked && !b.isFlag) {
                b.setIcon(flagIcon);
                b.setText("");
                b.isFlag = true;
                flagCount++;
                if (flagCount == MINE_COUNT) {
                    JOptionPane.showMessageDialog(this, "恭喜你，成功过关！");
                    gameOver = true;
                }
            } else if (b.isFlag) {
                b.setIcon(null);
                b.setText("");
                b.isFlag = false;
                flagCount--;
            }
        }
    }
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MineSweeper().setVisible(true));
    }
}