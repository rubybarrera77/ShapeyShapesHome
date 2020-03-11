import java.awt.*;

public class Obstacle extends Sprite{

    public Obstacle(Color color, int x, int y, int width, int height, Board board){
        super(color, x, y, width, height, board);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(getColor());
        g.fillRect(x, y, width, height);
    }
}