package snake;

import java.io.*;

public class Game {

    Board board;
    Console console;
    Trie trie;
    BufferedReader in;
    boolean[] keepPlay;
    
    public Game(int rows, int cols, BufferedReader in, boolean[] keepPlay) {
        board = new Board(rows, cols, keepPlay);
        console = new Console(board);
        this.in = in;
        this.keepPlay = keepPlay;
        trie = new Trie(in);
        trie.defaultInit();
        board.addObserver(console);
    }
    
    public int read() throws IOException {
        return trie.contains(in.read());
    }

    public int startGame() {
        int r;
        try {
            r = read();
            if (r == Key.CTRLC_INT || r == Key.Q_INT) {
                keepPlay[0] = false;
                return 0;
            }
            board.display();

            TimerThread timer = new TimerThread();
            timer.start();

            while (keepPlay[0] && keepPlay[1]) {
                r = read();
                switch (r) {
                    case Key.RIGHT_ARROW_INT:
                    case Key.LEFT_ARROW_INT:
                    case Key.UP_ARROW_INT:
                    case Key.DOWN_ARROW_INT:
                        //Restart thread
                        timer.cancel = true;
                        board.changeDir(r);
                        timer = new TimerThread();
                        timer.start();
                        break;
                    case Key.CTRLC_INT:
                    case Key.Q_INT:
                        keepPlay[0] = false;
                        break;
                }
            }
            timer.cancel = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return board.getScore();
    }

    class TimerThread extends Thread {

        boolean cancel = false;

        @Override
        public void run() {
            try {
                while (!cancel) {
                    sleep(100);
                    if (!cancel && keepPlay[1] && keepPlay[0]) board.move();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
