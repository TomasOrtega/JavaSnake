package snake;

import java.util.ArrayList;
import java.io.*;

public class Trie {
    
    Node base;
    Reader in;
    
    public Trie (Reader inReader) {
        base = new Node();
        in = inReader;
    }
    
    public void defaultInit() {
        this.addSon(Key.LEFT_ARROW, Key.LEFT_ARROW_INT);
        this.addSon(Key.RIGHT_ARROW, Key.RIGHT_ARROW_INT);
        this.addSon(Key.CTRLC, Key.CTRLC_INT);
        this.addSon(Key.Q, Key.Q_INT);
        this.addSon(Key.UP_ARROW, Key.UP_ARROW_INT);
        this.addSon(Key.DOWN_ARROW, Key.DOWN_ARROW_INT);
    }
    
    public int contains(int c) throws IOException{
        return base.contains(c);
    }
    
    public void addSon(String s, int value) {
        if (s.length() == 0) return;
        base.addSon(s, 0 , value);
    }

    class Node {
        
        int idx;
        ArrayList<Node> sons;
        boolean ends;
        int value;
        
        Node() {
            sons = new ArrayList<>();
            ends = false;
        }
        
        void addSon(String s, int i,int newValue) { //String s must be non-empty
            int c = (int) s.charAt(i);
            for (Node son : sons) {
                if (son.idx == c) {
                    if (i == s.length() - 1) {
                        son.ends = true;
                        son.value = newValue;
                    } else {
                        son.addSon(s, i + 1, newValue);
                    }
                    return;
                }
            }
            Node son = new Node();
            son.idx = c;
            if (i == s.length() - 1) {
                son.ends = true;
                son.value = newValue;
            } else {
                son.addSon(s, i + 1, newValue);
            }
            sons.add(son);
        }
        
        int contains(int c) throws IOException {
            for (Node son : sons) {
                if (son.idx == c){
                    if (son.ends) return son.value;
                    return son.contains(in.read());
                }
            }
            return c;
        }
        
    }
}

 
