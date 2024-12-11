import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Obstacle {
    int x, y;
    private Image obstacleImage;

    public Obstacle(int x, int y) {
        this.x = x;
        this.y = y;
        try {
            obstacleImage = ImageIO.read(new File("Obstacle.png")).getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: 이미지 파일을 찾을 수 없습니다!");
        }
    }

    public void draw(Graphics g) {
        if (obstacleImage != null) {
            g.drawImage(obstacleImage, x, y, 40, 40, null);
        } else {
            g.setColor(Color.GRAY);
            g.fillRect(x, y, 40, 40);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 40, 40);
    }
}
