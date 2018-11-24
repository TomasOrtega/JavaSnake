package snake;

import java.io.*;

class Snake {
    
    public static String execute(String command) {
        StringBuilder res = new StringBuilder();
        String aux;
        String[] commands = {"/bin/bash", "-c", command};
        try {
            Process process = new ProcessBuilder(commands).start();
            process.waitFor();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((aux = stdInput.readLine()) != null) {
                res.append(aux);
                res.append(System.getProperty("line.separator"));
            }
        } catch (IOException | InterruptedException e) {
            return e.getMessage();
        }
        return res.toString();
    }

    static void setRaw() {
        execute("stty raw -echo </dev/tty");
    }

    static void unsetRaw() {
        execute("stty -raw echo </dev/tty");
    }
    
    static int getRows() {
        return Integer.parseInt(execute("tput lines </dev/tty").trim());
    }

    static int getCols() {
        return Integer.parseInt(execute("tput cols </dev/tty").trim());
    }

    public static void main(String[] args) {
        setRaw();
        int highscore = 0;
        boolean[] keepPlay = new boolean[2];
        keepPlay[0] = true; //keep = true
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (keepPlay[0]) {
            keepPlay[1] = true; //play = true;
            int rows = getRows();
            int cols = getCols();
            String message = "END GAME, press any key to start, Q to quit. Highscore " + highscore;
            if (highscore == 0) {
                message = "NEW GAME, press any key to start";
            } else {
                System.out.print(Key.CURSOR_ORIGIN + Key.DELETE_ALL);
            }
            StringBuilder welcomeGame = new StringBuilder();
            for (int j = 0; j < (cols / 2 - message.length() / 2); ++j) welcomeGame.append(' ');
            welcomeGame.append(message);
            for (int i = 0; i < rows; ++i) {
                if (i != rows / 2) System.out.print("\n");
                else {
                    System.out.println(welcomeGame);
                }
            }
            Game game = new Game(rows, cols, in, keepPlay);
            highscore = Math.max(highscore, game.startGame());
        }
        unsetRaw();
        System.out.println();
    }
}