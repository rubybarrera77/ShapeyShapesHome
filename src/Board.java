import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Board extends JPanel implements ActionListener {

    Game game;
    Timer timer;
    ArrayList<Sprite> actors;
    int paddingNum = 25;
    long nextMoment;

    public Board(Game game){
        this.game = game;
        setPreferredSize(new Dimension(600, 800));
        setBackground(Color.BLACK);
    }

    public void setup(){
        actors = new ArrayList<>();
        actors.add(new Player(Color.green, getWidth()/2, getHeight()/2, 50, 50, this, game));

        for(int i = 0; i < STATS.getNumFood(); i++){
            actors.add(new Food(Color.orange, (int)(Math.random()*(getWidth()-paddingNum)+paddingNum), (int)(Math.random()*(getHeight()-paddingNum)+paddingNum), 20, 20, this));
        }

        for(int i = 0; i< STATS.getNumEnemies(); i++){
            actors.add(new Enemy(Color.RED, (int)(Math.random()*(getWidth()-paddingNum)+paddingNum), (int)(Math.random()*(getHeight()-paddingNum)+paddingNum), 50, 50, this));
        }

        timer = new Timer(1000/60, this);
        timer.start();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.white);

        if(Gamestates.isMENU()){
            g.setFont(new Font("Comic", Font.BOLD, 75));
            printString("Shapey Shapes", 0, getWidth()/2, (getHeight()/2)-50, g);
            g.setFont(new Font("Comic", Font.BOLD, 50));
            printString("Click to Play!", 0, getWidth()/2, (getHeight()/2)+50, g);
        }

        if(Gamestates.isPLAY()){
            g.setFont(new Font("Comic", Font.BOLD, 20));
            printString("Shapey Shapes", 0, getWidth()/2, getHeight()+20, g);
            for(Sprite thisGuy: actors){
                thisGuy.paint(g);
            }
        }

    }

    public void checkCollisions(){

        for(int i = 1; i < actors.size(); i++){
            if(actors.get(0).collidesWith(actors.get(i))){
                if(actors.get(i) instanceof Enemy){
                    Gamestates.setPAUSE(true);
                } else
                    actors.get(i).setRemove();
            }

        }

        for(int i = actors.size() - 1; i >= 0; i--){
            if(actors.get(i).isRemove()){
                actors.remove(i);
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e){

        nextMoment = System.currentTimeMillis();

        if(Gamestates.isMENU() && game.getIsClicked()){
            Gamestates.setMENU(false);
            Gamestates.setPLAY(true);
        }

        if(Gamestates.isPAUSE()){
            game.notClicked();
        }

        if(Gamestates.isPLAY() && !Gamestates.isPAUSE()){

            if((nextMoment - game.getMoment()) >= 1500){
                checkCollisions();
            }

            for(Sprite thisGuy: actors){
                thisGuy.move();
            }

            if(actors.size() <= STATS.getNumEnemies()+1){
                System.out.println("Killed them all");
                Gamestates.setPAUSE(true);
            }
        }

        repaint();
    }

    private void printString(String s, int width, int x, int y, Graphics g){
        int stringLen = (int)g.getFontMetrics().getStringBounds(s, g).getWidth();
        int start = width/2 - stringLen/2;
        g.drawString(s, start + x, y);
    }
}