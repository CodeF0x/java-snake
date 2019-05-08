import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Food {
    int foodX;
    int foodY;
    Image apple;

    public void spawn(int maxHeight, int maxWidth) {
        this.foodX = (int) (Math.random() * maxWidth / 10) * 10;
        this.foodY = (int) (Math.random() * maxHeight / 10) * 10;
        try {
            this.apple = ImageIO.read(new File("resources/apple.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}