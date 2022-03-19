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
        command = command.toLowerCase();
        StringTokenizer st = new StringTokenizer(command, "\t\n\r\f ();=<>*,", true);
        while(st.hasMoreElements()){
            tokens.add(st.nextToken());
        }
        tokens.removeAll(Arrays.asList(null, " "));
        return tokens;
    }

//    //TODO: tbc
//    public String stringLiteral(ArrayList<String> tokens, int index) {
//        String stringLiteral = tokens.get(index);
//        while(stringLiteral.charAt(stringLiteral.length()-1)!='\''){
//            index++;
//            stringLiteral = stringLiteral.concat(" ").concat(stringLiteral);
//        }
//        this.index = index;
//        return stringLiteral;
//    }


    public ArrayList<String> getTokens() {
        return tokens;
    }

    public void setTokens(ArrayList<String> tokens) {
        this.tokens = tokens;
    }
}
