import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class HealthItem {
    int x, y;
    private Image healthImage;

    public HealthItem(int x, int y) {
        this.x = x;
        this.y = y;
        try {
            healthImage = ImageIO.read(new File("HealthItem.png")).getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: HealthItem 이미지 파일을 찾을 수 없습니다!");
        }
    }

    public void draw(Graphics g) {
        if (healthImage != null) {
            g.drawImage(healthImage, x, y, 20, 20, null);
        } else {
            g.setColor(Color.GREEN); // 이미지가 없을 경우 초록색 원으로 표시
            g.fillOval(x, y, 20, 20);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 20, 20);
    }
}
