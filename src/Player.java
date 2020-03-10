import java.awt.*;

public class Player extends Sprite{

    Game game;

    public Player(Color color, int x, int y, int width, int height, Board board, Game game){
        super(color, x, y, width, height, board);
        this.game = game;
    }
    @Override
    public void move(){
        x = game.getPositionX() - getWidth()/2;
        y = game.getPositionY() - getWidth()/2;
    }


    @Override
    public void paint(Graphics g) {
        g.setColor(getColor());
        g.fillRect(getX(), getY(), getWidth(), getHeight());
    }
}