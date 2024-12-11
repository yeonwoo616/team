import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Item {
    int x, y;
    private Image itemImage;

    public Item(int x, int y) {
        this.x = x;
        this.y = y;
        try {
            itemImage = ImageIO.read(new File("Item.png")).getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: 이미지 파일을 찾을 수 없습니다!");
        }
    }

    public void draw(Graphics g) {
        if (itemImage != null) {
            g.drawImage(itemImage, x, y, 20, 20, null);
        } else {
            g.setColor(Color.CYAN);
            g.fillOval(x, y, 20, 20);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 20, 20);
    }
}
