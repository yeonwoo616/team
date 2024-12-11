import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;

public class Tank {
    int x, y, dx, dy;
    double angle;
    private BufferedImage tankImage;
    private BufferedImage scaledTankImage;
    private boolean poweredUp;
    private long powerUpEndTime;
    private int health;
    private boolean invincible;
    private long invincibleEndTime;
    private static final int TANK_WIDTH = 30;
    private static final int TANK_HEIGHT = 30;

    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
        this.angle = 0;
        this.poweredUp = false;
        this.powerUpEndTime = 0;
        this.health = 5;
        this.invincible = false;
        this.invincibleEndTime = 0;

        try {
            tankImage = ImageIO.read(new File("GreenTank.png"));
            scaledTankImage = new BufferedImage(TANK_WIDTH, TANK_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = scaledTankImage.createGraphics();
            g2d.drawImage(tankImage, 0, 0, TANK_WIDTH, TANK_HEIGHT, null);
            g2d.dispose();
        } catch (IOException e) {
            System.out.println("Error loading image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void move() {
        x += dx;
        y += dy;
    }

    public void stop() {
        dx = 0;
        dy = 0;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate(x + TANK_WIDTH / 2, y + TANK_HEIGHT / 2);
        g2d.rotate(angle);
        if (scaledTankImage != null) {
            g2d.drawImage(scaledTankImage, -TANK_WIDTH / 2, -TANK_HEIGHT / 2, null);
        } else {
            g2d.setColor(Color.RED);
            g2d.fillRect(-TANK_WIDTH / 2, -TANK_HEIGHT / 2, TANK_WIDTH, TANK_HEIGHT);
        }
        g2d.dispose();

        if (invincible) {
            g.setColor(new Color(255, 255, 0, 128)); // 무적 상태 시 투명한 노란색
            g.fillRect(x, y, TANK_WIDTH, TANK_HEIGHT);
        }

        g.setColor(Color.WHITE);
        g.drawString("Health: " + health, x, y - 10);
    }

    public void rotateTo(int mouseX, int mouseY) {
        angle = Math.atan2(mouseY - (y + TANK_HEIGHT / 2), mouseX - (x + TANK_WIDTH / 2));
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) dx = -2;
        if (key == KeyEvent.VK_D) dx = 2;
        if (key == KeyEvent.VK_W) dy = -2;
        if (key == KeyEvent.VK_S) dy = 2;
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_D) dx = 0;
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_S) dy = 0;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, TANK_WIDTH, TANK_HEIGHT);
    }

    public boolean isPoweredUp() {
        return poweredUp && System.currentTimeMillis() < powerUpEndTime;
    }

    public void activatePowerUp() {
        poweredUp = true;
        powerUpEndTime = System.currentTimeMillis() + 5000;
    }

    public void updatePowerUpStatus() {
        if (System.currentTimeMillis() > powerUpEndTime) {
            poweredUp = false;
        }

        if (invincible && System.currentTimeMillis() > invincibleEndTime) {
            invincible = false;
        }
    }

    public void takeDamage() {
        if (!invincible) {
            health--;
            invincible = true;
            invincibleEndTime = System.currentTimeMillis() + 1000; // 1초 무적
        }
    }

    public void heal(int amount) {
        health = Math.min(health + amount, 5);
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

    public int getHealth() {
        return health;
    }

    public Bullet shootNormal() {
        return new Bullet(x + TANK_WIDTH / 2, y + TANK_HEIGHT / 2, angle, Color.YELLOW);
    }

    public ArrayList<Bullet> shootPoweredUp() {
        ArrayList<Bullet> poweredUpBullets = new ArrayList<>();
        poweredUpBullets.add(new Bullet(x + TANK_WIDTH / 2, y + TANK_HEIGHT / 2, angle - Math.toRadians(10), Color.ORANGE));
        poweredUpBullets.add(new Bullet(x + TANK_WIDTH / 2, y + TANK_HEIGHT / 2, angle, Color.ORANGE));
        poweredUpBullets.add(new Bullet(x + TANK_WIDTH / 2, y + TANK_HEIGHT / 2, angle + Math.toRadians(10), Color.ORANGE));
        return poweredUpBullets;
    }
}

