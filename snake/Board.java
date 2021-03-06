package snake;

import java.util.Observable;
import java.util.LinkedList;
import java.util.Random;

public class Board extends Observable{

    class SnakeBit {
        final int i;
        final int j;
        SnakeBit(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    private int cols;
    private int rows;
    private int direction;
    private boolean[] keepPlay;
    int[][] board;
    private LinkedList<SnakeBit> linkedSnake;
    private Random rand;

    public Board(int rows, int cols, boolean[] keepPlay) {
        this.keepPlay = keepPlay;
        this.rand = new Random(System.currentTimeMillis());
        this.cols = cols;
        this.rows = rows;
        this.direction = Key.UP_ARROW_INT;
        this.board = new int[rows][cols];
        this.linkedSnake = new LinkedList<>();
        genMap();
        genFood();
        initSnake();
    }

    public void genMap() {
        for (int i = 0; i < rows; ++i) {
            board[i][0] = Key.WALL;
            board[i][cols - 1] = Key.WALL;
        }
        for (int j = 0; j < cols; ++j) {
            board[0][j] = Key.WALL;
            board[rows - 1][j] = Key.WALL;
        }
        int nwalls = Math.max(rows, cols) / 10;        
        for (int aux = 0; aux < nwalls; ++aux) {
            int i = rand.nextInt(rows);
            int j = rand.nextInt(cols);
            boolean row = rand.nextBoolean();
            int length = 0;
            if (row) length = rand.nextInt(rows / 2);
            else length = rand.nextInt(cols / 2);
            
            if (resoluble(i, j, length, row)) {
                putWall(board, i, j, length, row);
            }            
        }        
    }
    
    public void putWall(int[][] auxboard, int i, int j, int length, boolean row) {
        for (int k = 0; k < length; ++k) {
            if (row) {
                if (j + k >= cols) return;
                auxboard[i][j + k] = Key.WALL;
            } else {
                if (i + k >= rows) return;
                auxboard[i + k][j] = Key.WALL;
            }
        }
    }
    
    public void bfs(int[][] auxboard, int i, int j) {
        LinkedList<SnakeBit> queue = new LinkedList<>();
        queue.add(new SnakeBit(i, j));
        while (!queue.isEmpty()) {
            SnakeBit elem = queue.pop();
            i = elem.i;
            j = elem.j;
            if (auxboard[i][j] == Key.VOID) { //We use walls in order to stay in the board
                auxboard[i][j] = Key.VISITED;
                queue.add(new SnakeBit(i + 1, j));
                queue.add(new SnakeBit(i - 1, j));
                queue.add(new SnakeBit(i, j + 1));
                queue.add(new SnakeBit(i, j - 1));
            }
        }
    }
    
    public boolean resoluble(int i, int j, int length, boolean row) {
        int[][] auxboard = new int[rows][cols];
        for (int k = 0; k < rows; ++k) {
            for (int l = 0; l < cols; ++l) {
                auxboard[k][l] = board[k][l];
            }
        }
        putWall(auxboard, i, j, length, row);        
        for (int k = 0; k < rows; ++k) {
            for (int l = 0; l < cols; ++l) {
                if (auxboard[k][l] == Key.VOID) {
                    bfs(auxboard, k, l);
                    return oneComponent(auxboard);
                }
            }
        }
        return true;
    }
    
    public boolean oneComponent(int[][] auxboard) {    
        for (int k = 0; k < rows; ++k) {
            for (int l = 0; l < cols; ++l) {
                if (auxboard[k][l] == Key.VOID) {
                    return false;
                }
            }
        }
        return true;
    }

    public void initSnake() {
        int i, j;
        do {
            i = rand.nextInt(rows - 2) + 1;
            j = rand.nextInt(cols - 2) + 1;
        } while (board[i][j] != Key.VOID || board[i + 1][j] != Key.VOID || board[i - 1][j] == Key.WALL);
        board[i][j] = Key.SNAKE;
        board[i + 1][j] = Key.SNAKE;
        linkedSnake.push(new SnakeBit(i + 1, j));
        linkedSnake.push(new SnakeBit(i, j));
    }
    
    public void boardChange() {
        setChanged();
        notifyObservers();
    }
    
    public void move() {
        SnakeBit top = linkedSnake.peekFirst();
        int futureI = top.i;
        int futureJ = top.j;
        switch (direction) {
            case Key.UP_ARROW_INT:
                futureI--;
                break;
            case Key.RIGHT_ARROW_INT:
                futureJ++;
                break;
            case Key.DOWN_ARROW_INT:
                futureI++;
                break;
            case Key.LEFT_ARROW_INT:
                futureJ--;
                break;
        }
        if (futureI >= rows || futureJ >= cols || futureI < 0 || futureJ < 0) {
            keepPlay[1] = false;
            return;
        }
        if (board[futureI][futureJ] == Key.WALL || board[futureI][futureJ] == Key.SNAKE) keepPlay[1] = false;
        if (board[futureI][futureJ] != Key.FOOD)  {
            SnakeBit last = linkedSnake.pollLast();
            board[last.i][last.j] = 0;
        } else {
            genFood();
        }
        linkedSnake.push(new SnakeBit(futureI, futureJ));
        board[futureI][futureJ] = Key.SNAKE;
        boardChange();
    }

    public void display() {
        boardChange();
    }

    public int getScore() {
        return linkedSnake.size();
    }
    
    public void genFood() {
        int i, j;
        do {
            i = rand.nextInt(rows - 1);
            j = rand.nextInt(cols - 1);
        } while (board[i][j] != Key.VOID);
        board[i][j] = Key.FOOD;
    }
    
    public void changeDir(int dir) {
        if (dir + this.direction == 0) return; // we are using specific Key values in order for this to work
        this.direction = dir;
        move();
    }

    public char topChar() {
        char c = ' ';
        switch (direction) {
            case Key.UP_ARROW_INT:
                c = '^';
                break;
            case Key.LEFT_ARROW_INT:
                c = '<';
                break;
            case Key.RIGHT_ARROW_INT:
                c = '>';
                break;
            case Key.DOWN_ARROW_INT:
                c = '⌄';
        }
        return c;
    }

    @Override
    public String toString() {
        SnakeBit top = linkedSnake.peekFirst();
        StringBuilder fullBoard = new StringBuilder();
        for (int i = 0; i < rows; ++i) {
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < cols; ++j) {
                switch (board[i][j]) {
                    case Key.WALL:
                        line.append('X');
                        break;
                    case Key.SNAKE:
                        char c = '·';
                        if (i == top.i && j == top.j) {
                            c = topChar();
                        }
                        line.append(c);
                        break;
                    case Key.FOOD:
                        line.append('O');
                        break;
                    default:
                        line.append(' ');
                }
            }
            if (i != rows - 1) line.append("\n\r");
            fullBoard.append(line);
        }
        return fullBoard.toString();
    }

}
