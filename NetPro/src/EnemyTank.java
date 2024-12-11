import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class EnemyTank {
    int x, y;
    private final int SPEED = 2;
    private Image enemyImage;

    public EnemyTank(int x, int y) {
        this.x = x;
        this.y = y;
        try {
            // 프로젝트 파일 내에 있는 Monster.png를 로드
            enemyImage = ImageIO.read(new File("Monster.png")).getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: 이미지 파일을 찾을 수 없습니다!");
        }
    }

    public void move(int playerX, int playerY) {
        double angle = Math.atan2(playerY - y, playerX - x);
        x += SPEED * Math.cos(angle);
        y += SPEED * Math.sin(angle);
    }

    public void draw(Graphics g) {
        if (enemyImage != null) {
            g.drawImage(enemyImage, x, y, 30, 30, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(x, y, 30, 30);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 30, 30);
    }
}
