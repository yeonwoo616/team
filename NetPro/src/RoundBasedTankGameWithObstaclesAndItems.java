import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.imageio.ImageIO;

public class RoundBasedTankGameWithObstaclesAndItems extends JPanel implements ActionListener {
    private Timer timer;
    private Tank player;
    private ArrayList<Bullet> bullets;
    private ArrayList<EnemyTank> enemies;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<Item> items;
    private ArrayList<HealthItem> healthItems; // 회복 아이템 추가
    private int score;
    private int round;
    private Image backgroundImage;

    public RoundBasedTankGameWithObstaclesAndItems() {
        setFocusable(true);
        setPreferredSize(new Dimension(800, 600));

        try {
            backgroundImage = ImageIO.read(new File("Map.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: 배경 이미지 파일을 찾을 수 없습니다!");
        }

        player = new Tank(400, 500);
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        obstacles = new ArrayList<>();
        items = new ArrayList<>();
        healthItems = new ArrayList<>();

        score = 0;
        round = 1;

        timer = new Timer(30, this);
        timer.start();

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                player.keyPressed(e);
            }

            public void keyReleased(KeyEvent e) {
                player.keyReleased(e);
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                player.rotateTo(e.getX(), e.getY());
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (player.isPoweredUp()) {
                    bullets.addAll(player.shootPoweredUp());
                } else {
                    bullets.add(player.shootNormal());
                }
            }
        });

        startNewRound();
    }

    private void startNewRound() {
        enemies.clear();
        obstacles.clear();
        items.clear();
        healthItems.clear();

        for (int i = 0; i < round * 5; i++) {
            enemies.add(new EnemyTank(randomPosition(800), randomPosition(400)));
        }

        for (int i = 0; i < 5; i++) {
            obstacles.add(new Obstacle(randomPosition(800), randomPosition(600)));
        }

        for (int i = 0; i < 3; i++) {
            items.add(new Item(randomPosition(800), randomPosition(600)));
        }

        for (int i = 0; i < 2; i++) {
            healthItems.add(new HealthItem(randomPosition(800), randomPosition(600)));
        }
    }

    private int randomPosition(int max) {
        return new Random().nextInt(max);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player.move();
        player.updatePowerUpStatus();

        for (Obstacle obstacle : obstacles) {
            if (player.getBounds().intersects(obstacle.getBounds())) {
                player.stop();
            }
        }

        bullets.removeIf(bullet -> {
            bullet.move();
            return bullet.isOutOfBounds(800, 600);
        });

        for (EnemyTank enemy : enemies) {
            enemy.move(player.x, player.y);

            // 적과 충돌 시 체력 감소
            if (player.getBounds().intersects(enemy.getBounds())) {
                player.takeDamage();
                if (player.isDestroyed()) {
                    gameOver();
                    return;
                }
            }
        }

        checkCollisions();
        repaint();

        if (enemies.isEmpty()) {
            round++;
            startNewRound();
        }
    }

    private void checkCollisions() {
        Iterator<Bullet> bulletIter = bullets.iterator();
        while (bulletIter.hasNext()) {
            Bullet bullet = bulletIter.next();
            Iterator<EnemyTank> enemyIter = enemies.iterator();
            while (enemyIter.hasNext()) {
                EnemyTank enemy = enemyIter.next();
                if (bullet.getBounds().intersects(enemy.getBounds())) {
                    bulletIter.remove();
                    enemyIter.remove();
                    score += 10;
                    break;
                }
            }
        }

        Iterator<Item> itemIter = items.iterator();
        while (itemIter.hasNext()) {
            Item item = itemIter.next();
            if (player.getBounds().intersects(item.getBounds())) {
                player.activatePowerUp();
                itemIter.remove();
                score += 20;
            }
        }

        Iterator<HealthItem> healthItemIter = healthItems.iterator();
        while (healthItemIter.hasNext()) {
            HealthItem healthItem = healthItemIter.next();
            if (player.getBounds().intersects(healthItem.getBounds())) {
                player.heal(1);
                healthItemIter.remove();
                score += 10;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }

        player.draw(g);

        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
        for (EnemyTank enemy : enemies) {
            enemy.draw(g);
        }
        for (Obstacle obstacle : obstacles) {
            obstacle.draw(g);
        }
        for (Item item : items) {
            item.draw(g);
        }
        for (HealthItem healthItem : healthItems) {
            healthItem.draw(g);
        }

        drawScoreAndRound(g);
    }

    private void drawScoreAndRound(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Round: " + round, 10, 40);
        g.drawString("Health: " + player.getHealth(), 10, 60);
    }

    private void gameOver() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! Final Score: " + score);
        System.exit(0);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Round-Based Tank Game with Obstacles and Items");
        RoundBasedTankGameWithObstaclesAndItems game = new RoundBasedTankGameWithObstaclesAndItems();
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
