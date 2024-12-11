import java.awt.*;

public class Bullet {
    double x, y;
    double angle;
    Color color;
    private final int SPEED = 5;

    public Bullet(double x, double y, double angle, Color color) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.color = color;
    }

    public void move() {
        x += SPEED * Math.cos(angle);
        y += SPEED * Math.sin(angle);
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect((int) x, (int) y, 5, 5);
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, 5, 5);
    }

    // 추가된 메서드: 총알이 화면 경계를 벗어났는지 확인 (public으로 변경)
    public boolean isOutOfBounds(int width, int height) {
        return (x < 0 || x > width || y < 0 || y > height);
    }
}

