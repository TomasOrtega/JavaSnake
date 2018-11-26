package snake;

public class Key {

    public static final String ESC = "\033";
    public static final String CSI = ESC + "[";
    public static final String Q = "q";
    
    public static final String LEFT_ARROW = CSI + "D";
    public static final String RIGHT_ARROW = CSI + "C";
    public static final String CTRLC = "\003";
    public static final String UP_ARROW = CSI + "A";
    public static final String DOWN_ARROW = CSI + "B";

    public static final String DELETE_ALL = CSI + "0J"; //Erases below
    public static final String CURSOR_ORIGIN = CSI + "1;1H";

    public static final int LEFT_ARROW_INT = 2;
    public static final int RIGHT_ARROW_INT = -2;
    public static final int UP_ARROW_INT = 1;
    public static final int DOWN_ARROW_INT = -1;
    public static final int CTRLC_INT = -13;
    public static final int Q_INT = -14;

    public static final int VOID = 0;
    public static final int WALL = 1;
    public static final int SNAKE = 2;
    public static final int FOOD = 3;
    public static final int VISITED = 4;
    
}
