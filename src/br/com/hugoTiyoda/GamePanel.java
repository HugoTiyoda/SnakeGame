package br.com.hugoTiyoda;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {
    static final int comprimentoTela = 1300;
    static final int alturaTela = 750;
    static final int tamanhoUnidadeJogo = 25;
    static final int unidadesJogo = 1560;
    static final int velocidadeGame = 95;
    final int[] x = new int[1560];
    final int[] y = new int[1560];
    int corpoCobraInicial = 4;
    int macasComidas;
    int appleX;
    int appleY;
    char direcao = 'R';
    boolean rodando = false;
    Timer timer;
    Random random = new Random();

    GamePanel() {
        this.setPreferredSize(new Dimension(1300, 750));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new GamePanel.MyKeyAdapter());
        this.startGame();
    }

    public void startGame() {
        this.gerarMaca();
        this.rodando = true;
        this.timer = new Timer(95, this);
        this.timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.draw(g);
    }

    public void draw(Graphics g) {
        if (this.rodando) {
            g.setColor(Color.red);
            g.fillOval(this.appleX, this.appleY, 25, 25);

            for(int i = 0; i < this.corpoCobraInicial; ++i) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(this.x[i], this.y[i], 25, 25);
                } else {
                    g.setColor(Color.BLUE);
                    g.fillRect(this.x[i], this.y[i], 25, 25);
                }
            }

            g.setColor(Color.white);
            g.setFont(new Font("Arial", 1, 40));
            FontMetrics metrics = this.getFontMetrics(g.getFont());
            g.drawString("Score: " + this.macasComidas, (1300 - metrics.stringWidth("Score: " + this.macasComidas)) / 2, g.getFont().getSize());
        } else {
            this.gameOver(g);
        }

    }

    public void gerarMaca() {
        this.appleX = this.random.nextInt(52) * 25;
        this.appleY = this.random.nextInt(30) * 25;
    }

    public void movimentacao() {
        for(int i = this.corpoCobraInicial; i > 0; --i) {
            this.x[i] = this.x[i - 1];
            this.y[i] = this.y[i - 1];
        }

        switch(this.direcao) {
            case 'D':
                this.y[0] += 25;
                break;
            case 'L':
                this.x[0] -= 25;
                break;
            case 'R':
                this.x[0] += 25;
                break;
            case 'U':
                this.y[0] -= 25;
        }

    }

    public void comeuMaca() {
        if (this.x[0] == this.appleX && this.y[0] == this.appleY) {
            ++this.corpoCobraInicial;
            ++this.macasComidas;
            this.gerarMaca();
        }

    }

    public void batidas() {
        for(int i = this.corpoCobraInicial; i > 0; --i) {
            if (this.x[0] == this.x[i] && this.y[0] == this.y[i]) {
                this.rodando = false;
            }
        }

        if (this.x[0] < 0) {
            this.rodando = false;
        }

        if (this.x[0] > 1300) {
            this.rodando = false;
        }

        if (this.y[0] < 0) {
            this.rodando = false;
        }

        if (this.y[0] > 750) {
            this.rodando = false;
        }

        if (!this.rodando) {
            this.timer.stop();
        }

    }

    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Arial", 1, 40));
        FontMetrics metrics1 = this.getFontMetrics(g.getFont());
        g.drawString("Score: " + this.macasComidas, (1300 - metrics1.stringWidth("Score: " + this.macasComidas)) / 2, g.getFont().getSize());
        g.setColor(Color.red);
        g.setFont(new Font("Arial", 1, 75));
        FontMetrics metrics2 = this.getFontMetrics(g.getFont());
        g.drawString("Game Over", (1300 - metrics2.stringWidth("Game Over")) / 2, 250);
        g.setFont(new Font("Ink Free", 1, 30));
        g.drawString("Space To restart", (1300 - metrics2.stringWidth("Game Over")) / 2, 375);
    }

    public void actionPerformed(ActionEvent e) {
        if (this.rodando) {
            this.movimentacao();
            this.comeuMaca();
            this.batidas();
        }

        this.repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        public MyKeyAdapter() {
        }

        public void keyPressed(KeyEvent e) {
            if (!GamePanel.this.rodando && e.getKeyCode() == 32) {
                GamePanel.this.rodando = true;
                GamePanel.this.corpoCobraInicial = 4;
                GamePanel.this.macasComidas = 0;
                GamePanel.this.x[0] = 250;
                GamePanel.this.y[0] = 300;
                GamePanel.this.repaint();
                GamePanel.this.timer.start();
            }

            switch(e.getKeyCode()) {
                case 37:
                    if (GamePanel.this.direcao != 'R') {
                        GamePanel.this.direcao = 'L';
                    }
                    break;
                case 38:
                    if (GamePanel.this.direcao != 'D') {
                        GamePanel.this.direcao = 'U';
                    }
                    break;
                case 39:
                    if (GamePanel.this.direcao != 'L') {
                        GamePanel.this.direcao = 'R';
                    }
                    break;
                case 40:
                    if (GamePanel.this.direcao != 'U') {
                        GamePanel.this.direcao = 'D';
                    }
            }

        }
    }
}
