package edu.uob;

import java.util.*;

public class Tokenizer {
    private ArrayList<String> tokens = new ArrayList<>();
    public int index;

    public Tokenizer(){
        //tokens = TODO: return array list of tokens
    }

    public String nextCommand(int index) { //instructions
        return tokens.get(index);
    }

    public ArrayList<String> splitCommand(String command){ //TODO:change private
        ArrayList<String> tokens = new ArrayList<>();
//        command = command.toLowerCase();
        StringTokenizer st = new StringTokenizer(command, "\t\n\r\f ();=<>*,", true);
        while(st.hasMoreElements()){
            tokens.add(st.nextToken());
        }
        tokens.removeAll(Arrays.asList(null, " "));
        for(int i=0; i< tokens.size();){ // string literal remain case sensitive
            if(tokens.get(i).charAt(0)=='\''){
                while(tokens.get(i).charAt(tokens.get(i).length()-1)!='\''){
                    i++;
                }
            }
            else {
                tokens.set(i, tokens.get(i).toLowerCase());
            }
            i++;
        }
        System.out.println("Tokens "+tokens);
        return tokens;
    }

    public ArrayList<String> getTokens() {
        return tokens;
    }

    public void setTokens(ArrayList<String> tokens) {
        this.tokens = tokens;
    }
}
