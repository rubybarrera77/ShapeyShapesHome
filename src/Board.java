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

        timer = new Timer(1000/60, this);
        timer.start();
    }

    public void setup(){
        actors = new ArrayList<>();
        actors.add(new Player(Color.green, getWidth()/2, getHeight()/2, 50, 50, this, game));

        if(STATS.getLevel() > 2){
            actors.add(new Powerup(Color.pink, (int)(Math.random()*(getWidth()-paddingNum)+paddingNum), (int)(Math.random()*(getHeight()-paddingNum)+paddingNum), 30, 30, this));
        }

        for(int i = 0; i < STATS.getNumFood(); i++){
            actors.add(new Food(Color.orange, (int)(Math.random()*(getWidth()-paddingNum)+paddingNum), (int)(Math.random()*(getHeight()-paddingNum)+paddingNum), 20, 20, this));
        }

        for(int i = 0; i< STATS.getNumEnemies(); i++){
            actors.add(new Enemy(Color.RED, (int)(Math.random()*(getWidth()-paddingNum)+paddingNum), (int)(Math.random()*(getHeight()-paddingNum)+paddingNum), 50, 50, this));
        }

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
            printString("Shapey Shapes", 0, getWidth()/2, 50, g);
            printString("Lives: " + (Integer.toString(STATS.getLives())), 50, 75, 50, g);
            printString("Score: " + (Integer.toString(STATS.getScore())), 50, 475, 50, g);
            for(Sprite thisGuy: actors){
                thisGuy.paint(g);
            }
        }

        if(Gamestates.isUPDATE()){
            String text = " ";
            String click = " ";

            if(STATS.getLives() == 0){
                text = "You Lose!";
                click = "Click to Play Again";
            }

            if(actors.size() <= STATS.getNumEnemies() + 1){
                text = "Congrats! You have completed level 1!";
                click = "Click to continue to level " + STATS.getLevel();
            }

            g.setFont(new Font("Comic", Font.BOLD, 50));
            printString(text, 0, getWidth()/2, getHeight()/2, g);

            g.setFont(new Font("Comic", Font.BOLD, 20));
            printString(click,0, getWidth()/2, (getHeight()/2)+50, g);
        }

    }

    public void checkCollisions(){

        for(int i = 1; i < actors.size(); i++){
            if(actors.get(0).collidesWith(actors.get(i))){
                if(actors.get(i) instanceof Enemy){
                    actors.get(i).setDx(actors.get(i).getDx()*-1);
                    actors.get(i).setDy(actors.get(i).getDy()*-1);
                    STATS.setLives(STATS.getLives()-1);
                } else if(actors.get(i) instanceof Powerup){
                    STATS.setLives(STATS.getLives()+5);
                    actors.get(i).setRemove();
                } else {
                    actors.get(i).setRemove();
                    STATS.setScore(STATS.getScore()+10);
                }
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

        if(Gamestates.isMENU()){
            STATS.setLives(5);
            STATS.setScore(0);
        }

        if(Gamestates.isMENU() && game.getIsClicked()){
            Gamestates.setMENU(false);
            Gamestates.setPLAY(true);
            game.notClicked();
        }

        if(Gamestates.isPAUSE()){
            game.notClicked();
        }

        if(Gamestates.isPLAY() && game.getIsClicked()){
            Gamestates.setPAUSE(true);
            game.notClicked();

        }

        if(Gamestates.isPAUSE() && game.getIsClicked()){
            Gamestates.setPLAY(true);
            Gamestates.setPAUSE(false);
        }

        if(Gamestates.isPLAY() && !Gamestates.isPAUSE()) {

            if ((nextMoment - game.getMoment()) >= 1500) {
                checkCollisions();
            }

            for (Sprite thisGuy : actors) {
                thisGuy.move();
            }

            if (actors.size() <= STATS.getNumEnemies() + 1) {
                System.out.println("Killed them all");
                Gamestates.setUPDATE(true);
                Gamestates.setPLAY(false);
                game.notClicked();
            }

            if(STATS.getLives() == 0) {
                Gamestates.setUPDATE(true);
                Gamestates.setPLAY(false);
                game.notClicked();
            }
        }

        if(Gamestates.isUPDATE() && game.getIsClicked()) {
            game.notClicked();
            if (STATS.getLives() == 0) {
                STATS.setLevel(1);
                STATS.updateLevel();
                setup();
                Gamestates.setMENU(true);
            }

            if (actors.size() <= STATS.getNumEnemies() + 1) {
                STATS.setLevel(STATS.getLevel()+1);
                STATS.updateLevel();
                setup();
                Gamestates.setPLAY(true);
            }


            Gamestates.setUPDATE(false);
        }

        repaint();
    }

    private void printString(String s, int width, int x, int y, Graphics g){
        int stringLen = (int)g.getFontMetrics().getStringBounds(s, g).getWidth();
        int start = width/2 - stringLen/2;
        g.drawString(s, start + x, y);
    }
}