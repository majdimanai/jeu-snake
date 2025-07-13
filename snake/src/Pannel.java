import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Pannel extends JPanel implements ActionListener {
    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    static final int UNIT = 25;
    static final int DELAY = 75;
    static final int MAX_UNITS = (WIDTH * HEIGHT) / (UNIT * UNIT);

    private final int[] x = new int[MAX_UNITS];
    private final int[] y = new int[MAX_UNITS];
    private int bodyPart = 6;
    private int applesEaten;
    private int appleX;
    private int appleY;
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;
    private final Random random = new Random();

    public Pannel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    private void startGame() {
        // initialise la tête au centre
        x[0] = WIDTH / 2;
        y[0] = HEIGHT / 2;

        for (int i = 1; i < bodyPart; i++) {
            x[i] = x[0] - i * UNIT;
            y[i] = y[0];
        }
        spawnApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void spawnApple() {
        appleX = random.nextInt(WIDTH / UNIT) * UNIT;
        appleY = random.nextInt(HEIGHT / UNIT) * UNIT;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (running) {
            drawGame(g);
        } else {
            drawGameOver(g);
        }
    }

    private void drawGame(Graphics g) {

        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i <= WIDTH / UNIT; i++) {
            g.drawLine(i * UNIT, 0, i * UNIT, HEIGHT);
        }
        for (int i = 0; i <= HEIGHT / UNIT; i++) {
            g.drawLine(0, i * UNIT, WIDTH, i * UNIT);
        }

        g.setColor(Color.GREEN);
        g.fillOval(appleX, appleY, UNIT, UNIT);

        for (int i = 0; i < bodyPart; i++) {
            g.setColor(i == 0 ? Color.RED : Color.YELLOW);
            g.fillRect(x[i], y[i], UNIT, UNIT);
        }
    }

    private void move() {

        for (int i = bodyPart; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U' -> y[0] -= UNIT;
            case 'D' -> y[0] += UNIT;
            case 'L' -> x[0] -= UNIT;
            case 'R' -> x[0] += UNIT;
        }
    }

    private void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            bodyPart++;
            applesEaten++;
            spawnApple();
        }
    }

    private void checkCollisions() {

        for (int i = bodyPart; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }

        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            running = false;
        }
        if (!running) {
            timer.stop();
        }
    }

    private void drawGameOver(Graphics g) {

        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 75));
        int w = g.getFontMetrics().stringWidth("GAME OVER");
        g.drawString("GAME OVER", (WIDTH - w) / 2, HEIGHT / 2);

        g.setFont(new Font("Arial", Font.PLAIN, 30));
        String score = "Score : " + applesEaten;
        w = g.getFontMetrics().stringWidth(score);
        g.drawString(score, (WIDTH - w) / 2, HEIGHT / 2 + 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> { if (direction != 'R') direction = 'L'; }
                case KeyEvent.VK_RIGHT -> { if (direction != 'L') direction = 'R'; }
                case KeyEvent.VK_UP -> { if (direction != 'D') direction = 'U'; }
                case KeyEvent.VK_DOWN -> { if (direction != 'U') direction = 'D'; }
            }
        }
    }
}
