package edu.uob;

import java.util.*;

public class Tokenizer {
    private ArrayList<String> tokens;

    public Tokenizer(){
        //tokens = TODO: return array list of tokens
    }

    public void nextToken(){ //instructions
//        return
    }


    public ArrayList<String> splitCommand(String command){ //TODO:change private
        ArrayList<String> tokens = new ArrayList<>();
        command = command.toLowerCase();
        StringTokenizer st = new StringTokenizer(command, " \t\n\r\f();=<>!*,", true);
        while(st.hasMoreElements()){
            tokens.add(st.nextToken());
        }
        tokens.removeAll(Arrays.asList(null, " "));
        return tokens;
    }
}
