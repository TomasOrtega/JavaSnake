package snake;

import java.util.Observable;
import java.util.Observer;

public class Console implements Observer {
    
    Board board;
    
    public Console(Board board) {
        this.board = board;
    }
    
    @Override
    public void update(Observable arg0, Object arg1) {
        System.out.print(Key.CURSOR_ORIGIN + board);
    }
    
}