import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Board extends JPanel implements ActionListener {

    Game game;
    Timer timer;
    ArrayList<Sprite> actors;
    ArrayList<Sprite>obstacles;

    int paddingNum = 25;
    long nextMoment;

    public Board(Game game){
        this.game = game;
        setPreferredSize(new Dimension(600, 800));
        setBackground(Color.BLACK);

        timer = new Timer(1000/60, this);
        timer.start();
    }

    int w = 0;
    int h = 0;

    public void setup(){
        actors = new ArrayList<>();
        obstacles = new ArrayList<>();
        actors.add(new Player(Color.green, getWidth()/2, getHeight()/2, w, h, this, game));

        if(STATS.getLevel() <= 1){
            w = 40;
            h = 40;
            actors.get(0).setWidth(w);
            actors.get(0).setHeight(h);
        }

        if(STATS.getLevel()>1){
            w+=10;
            h+=10;
        }
        if(STATS.getLevel() > 2){
            actors.add(new Powerup(Color.pink, (int)(Math.random()*(getWidth()-paddingNum)+paddingNum), (int)(Math.random()*(getHeight()-paddingNum)+paddingNum), 30, 30, this));
        }
        for(int i = 0; i < STATS.getNumFood(); i++){
            actors.add(new Food(Color.orange, (int)(Math.random()*(getWidth()-paddingNum)+paddingNum), (int)(Math.random()*(getHeight()-paddingNum)+paddingNum), 20, 20, this));
        }

        for(int i = 0; i< STATS.getNumEnemies(); i++){
            actors.add(new Enemy(Color.RED, (int)(Math.random()*(getWidth()-paddingNum)+paddingNum), (int)(Math.random()*(getHeight()-paddingNum)+paddingNum), 50, 50, this));
        }

        int space = 0;
        if(STATS.getLevel() > 1){
            for(int i = 0; i < 3; i++) {
                obstacles.add(new Obstacle(Color.blue, getWidth()/2, (getHeight()/4)+space, 70, 20, this));
                space+=200;
            }
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
            printString("Level " + (Integer.toString(STATS.getLevel())), 0, getWidth()/2, 50, g);
            printString("Lives: " + (Integer.toString(STATS.getLives())), 50, 75, 50, g);
            printString("Score: " + (Integer.toString(STATS.getScore())), 50, 475, 50, g);
            for(Sprite thisGuy: actors){
                thisGuy.paint(g);
            }
            for(Sprite o: obstacles){
                o.paint(g);
            }
        }

        if(Gamestates.isUPDATE()){
            String text = " ";
            String total = "Total Score: " + (Integer.toString(STATS.getScore()));
            String click = " ";

            if(STATS.getLives() == 0){
                text = "You Lose!";
                click = "Click to Play Again";
            }

            if(STATS.getLevel() > 5){
                text = "Congrats! You Won!";
                click = "Click to restart";
            } else if(actors.size() <= STATS.getNumEnemies() + 1){
                text = "Level "  +  (Integer.toString(STATS.getLevel())) + " Complete!";
                click = "Click to continue to level " + (Integer.toString(STATS.getLevel()+1));
            }

            g.setFont(new Font("Comic", Font.BOLD, 50));
            printString(total, 0, getWidth()/2, (getHeight()/2)-100, g);
            printString(text, 0, getWidth()/2, getHeight()/2, g);

            g.setFont(new Font("Comic", Font.BOLD, 20));
            printString(click,0, getWidth()/2, (getHeight()/2)+70, g);
        }

    }

    public void checkCollisions(){

        for(int i = 1; i < actors.size(); i++){
            for(int j = 0; j < obstacles.size(); j++){
                if(actors.get(i).collidesWith(obstacles.get(j))){
                    actors.get(i).setDx(actors.get(i).getDx()*-1);
                    actors.get(i).setDy(actors.get(i).getDy()*-1);
                }
                if(actors.get(0).collidesWith(obstacles.get(j))){
                    obstacles.get(j).setDx(actors.get(i).getDx()*-1);
                }
            }

            if(actors.get(0).collidesWith(actors.get(i))){
                if(actors.get(i) instanceof Enemy){
                    actors.get(i).setDx(actors.get(i).getDx()*-1);
                    actors.get(i).setDy(actors.get(i).getDy()*-1);
                    STATS.setLives(STATS.getLives()-1);
                    STATS.setScore(STATS.getScore()-15);
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
            STATS.setLevel(1);
            STATS.updateLevel();
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
                thisGuy.setMove(true);
                thisGuy.move();
            }

            for (Sprite o : obstacles) {
                o.setMove(false);
                o.move();
            }

            if(STATS.getLives() == 0) {
                Gamestates.setUPDATE(true);
                Gamestates.setPLAY(false);
                game.notClicked();
            }

            if(STATS.getLevel() > 5){
                Gamestates.setUPDATE(true);
                Gamestates.setPLAY(false);
                game.notClicked();
            } else if (actors.size() <= STATS.getNumEnemies() + 1) {
                System.out.println("Killed them all");
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

            if(STATS.getLevel() > 5){
                STATS.setLevel(1);
                STATS.updateLevel();
                setup();
                Gamestates.setMENU(true);
            } else if (actors.size() <= STATS.getNumEnemies() + 1){
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