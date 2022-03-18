package edu.uob;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class Parser extends DBCommands {
    private Tokenizer tokenizer = new Tokenizer();
    public ArrayList<String> tokens;
    private int index;
    private File currentDirectory;
    private String tableName;
    private ArrayList<String> attributeList;

    public Parser(String command){
        index = 0;
        this.tokens = tokenizer.splitCommand(command);
        tokenizer.setTokens(tokens);
        System.out.println(tokens);
    }

    public boolean parse(){
        String nextCommand = tokenizer.nextCommand(index);
        switch(nextCommand){
            case "select":
                System.out.println("select");
                parseSelect();
                break;
            case "use": //TODO: CommandType
                System.out.println("use");
                if(parseUse()){
                    return true;
                }
                break;
            case "create":
                System.out.println("create");
                if(parseCreate()){
                    return true;
                }
                break;
            //case "?":

        }
//        System.out.println(tokenizer.nextCommand(index));
        if(!Objects.equals(tokenizer.nextCommand(index), ";")){
            //TODO: throw error
            System.out.println("no ;");
        }
        return false;
    }

    public boolean parseUse(){ //USE DBName;
//        DBCommands use = new UseCmd();
        index++;
        if(tokenizer.nextCommand(index).matches("[A-Za-z0-9]+")){ //dbname
            String path = "../" + tokenizer.nextCommand(index);
            File databaseDirectory = new File(path);
            if(databaseDirectory.exists()){
                currentDirectory = databaseDirectory;
                return true;
//                DBCommands use = new UseCmd();
//                System.out.println("current "+currentDirectory.getPath());
//                System.out.println("EXIST");
            }
            else{
                //TODO: throw error
                System.out.println("folder doesn't exist, please create");
            }
            index++; // ;
        }
        //TODO: add query, DBcommands s
        return false;
    }

    public File getCurrentDirectory() {
        return currentDirectory;
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

    public boolean parseCreate(){
        index++;
        if (Objects.equals(tokenizer.nextCommand(index), "table")) {
            if(parseCreateTable()){
                return true;
            }
        }
        else if(Objects.equals(tokenizer.nextCommand(index), "database")) {
            if(parseCreateDatabase()){
                return true;
            };
        }
        return false;
    }

    public boolean parseCreateDatabase() { //CREATE
        index++; //create database dbname/ create table tablename (attributelist);
        if(tokenizer.nextCommand(index).matches("[A-Za-z0-9]+")){
            index++;
            return true;
        }
        else { //TODO: throw error
            System.out.println("Invalid query");
            return false;
        }
    }

    public boolean parseCreateTable() { //CREATE
        index++; //create table tablename (attributelist);
        if (tokenizer.nextCommand(index).matches("[A-Za-z0-9]+")) {
            index++;
        }
        if(Objects.equals(tokenizer.nextCommand(index), "(")){
            ArrayList<String> attributeNames = new ArrayList<>();
            index++;
            while(!(Objects.equals(tokenizer.nextCommand(index), ")"))){
                if(tokenizer.nextCommand(index).matches("[A-Za-z0-9]+")){
                    attributeNames.add(tokenizer.nextCommand(index));
                    index++;
                    if(Objects.equals(tokenizer.nextCommand(index), ",")){
                        index++;
                    }
                }
                else{
                    System.out.println("invalid name");
                }
            }
            //TODO: attributeNames add
            index++;
            return true;
        }
        else{
            //TODO: create table name
        }
//        else { //TODO: throw error
//            System.out.println("Invalid query");
//        }
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
