package edu.uob;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class Parser {
    private Tokenizer tokenizer = new Tokenizer();
    public ArrayList<String> tokens;
    private int index;
    private File databaseDirectory;
    private String tableName;
    private ArrayList<String> attributeList;

    public Parser(String command){
        index = 0;
        this.tokens = tokenizer.splitCommand(command);
        tokenizer.setTokens(tokens);
//        databaseDirectory = "../" + DBName;
//        System.out.println(tokenizer.nextCommand(index));
    }

    public void parse(){
        String nextCommand = tokenizer.nextCommand(index);
        switch(nextCommand){
            case "select":
                parseSelect();
                break;
            case "use": //TODO: CommandType
                parseUse();
                break;
            //case "?":

        }
        System.out.println(tokenizer.nextCommand(index));
        if(!Objects.equals(tokenizer.nextCommand(index), ";")){
            //TODO: throw error
            System.out.println("no ;");
        }
    }

    public boolean parseUse(){ //USE DBName;
//        UseCmd use = new UseCmd();
        index++;
        if(tokenizer.nextCommand(index).matches("[A-Za-z0-9]+")){ //dbname
            String path = "../" + tokenizer.nextCommand(index);
            databaseDirectory = new File(path);
            if(databaseDirectory.exists()){
                //TODO: manipulate data
                System.out.println("EXIST");
            }
            else{
                //TODO: throw error
                System.out.println("folder doesn't exist");
            }
            index++; // ;
        }




//        UseCMD use = new UseCMD(tokenizedCommand, index);
//        index = use.getIndex();
//        databaseName = use.getDatabaseName();
//        currentFolder = homeDirectory+File.separator+databaseName;
        return false;
    }


    //select <wildattributelist> from table (where condition)
    public boolean parseSelect(){ //SELECT
        index++; //e.g. select * from marks;
        if(isWildAttribList(index)){
            index++;
            if(Objects.equals(tokenizer.nextCommand(index), "from")){
                index++;
                tableName = tokenizer.nextCommand(index);
//                System.out.println(tableName);
                index++; // ;
            }
            else{
                System.out.println("Invalid query");
            }
        }
        else{ //TODO: throw error
            System.out.println("Invalid query");
//            throw new Exception("Wrong grammar");
        }
        return false;
    }




    public boolean isWildAttribList(int index){
        System.out.println(tokenizer.nextCommand(index));
        if(Objects.equals(tokenizer.nextCommand(index), "*")){
            return true;
        }
        //else if() //TODO: wildattributelist: attributename|attributename,attributelist
        return false;
    }

}
