package edu.uob;

import java.util.*;

public class Tokenizer {
    private ArrayList<String> tokens = new ArrayList<>();

    public ArrayList<String> splitCommand(String command){ //TODO:change private
        ArrayList<String> tokens = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(command, "\t\n\r\f <>!*()=;,", true);
        while(st.hasMoreElements()){
            tokens.add(st.nextToken());
        }
        tokens.removeAll(Arrays.asList(null, " "));
        return tokens;
    }

    public ArrayList<String> getTokens() {
        return tokens;
    }

    public void setTokens(ArrayList<String> tokens) {
        this.tokens = tokens;
    }
}
