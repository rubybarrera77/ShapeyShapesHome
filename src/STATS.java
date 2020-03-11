public class STATS {

    private static int numFood = 5;
    private static int numEnemies = 1;
    private static int life = 3;
    private static int level = 1;
    private static int lowSpeed = 2;
    private static int rangeSpeed = 4;
    private static int score = 0;
    private static int lives = 5;

    public static int getNumFood() {
        return numFood;
    }

    public static void setNumFood(int numFood) {
        STATS.numFood = numFood;
    }

    public static int getNumEnemies() {
        return numEnemies;
    }

    public static void setNumEnemies(int numEnemies) {
        STATS.numEnemies = numEnemies;
    }

    public static int getLife() {
        return life;
    }

    public static void setLife(int life) {
        STATS.life = life;
    }

    public static int getLevel() {
        return level;
    }

    public static void setLevel(int level) {
        STATS.level = level;
    }

    public static int getLowSpeed() {
        return lowSpeed;
    }

    public static void setLowSpeed(int lowSpeed) {
        STATS.lowSpeed = lowSpeed;
    }

    public static int getRangeSpeed() {
        return rangeSpeed;
    }

    public static void setRangeSpeed(int rangeSpeed) {
        STATS.rangeSpeed = rangeSpeed;
    }

    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        STATS.score = score;
    }

    public static int getLives() {
        return lives;
    }

    public static void setLives(int lives) {
        STATS.lives = lives;
    }

    public static void updateLevel() {
        switch (level) {
            case 1:
                setNumFood(2);//5
                setNumEnemies(1);
                setLowSpeed(2);
                setRangeSpeed(4);
                break;
            case 2:
                setNumFood(4);//10
                setNumEnemies(1);
                setLowSpeed(4);
                setRangeSpeed(8);
                break;
            case 3:
                setNumFood(6);//13
                setNumEnemies(1);
                setLowSpeed(6);
                setRangeSpeed(10);
                break;
            case 4:
                setNumFood(8);//20
                setNumEnemies(1);//2
                setLowSpeed(8);
                setRangeSpeed(12);
                break;
            case 5:
                setNumFood(1);//25
                setNumEnemies(1);
                setLowSpeed(10);
                setRangeSpeed(15);
                break;
        }
    }
}