import java.awt.*;

public class Food extends Sprite{

    public Food(Color color, int x, int y, int width, int height, Board board){
        super(color, x, y, width, height, board);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(getColor());
        g.fillOval(getX(), getY(), getWidth(), getHeight());
    }
}