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

    public String parse(){
        String nextCommand = tokenizer.nextCommand(index);
        switch(nextCommand){
            case "select":
                System.out.println("select");
                parseSelect();
                break;
            case "use": //TODO: CommandType
                System.out.println("use");
                parseUse();
                index++;
                break;
            case "create":
                System.out.println("create");
                return parseCreate();
//                break;
        }
        if(index >= tokens.size() || (!Objects.equals(tokenizer.nextCommand(index), ";"))){
            return "Missing ;";
        }
        return "OK";
    }

    public String parseUse(){ //USE DBName;
        index++;
        if(tokenizer.nextCommand(index).matches("[A-Za-z0-9]+")){ //dbname
            String path = "../" + tokenizer.nextCommand(index);
            File databaseDirectory = new File(path);
            if(databaseDirectory.exists()){
                currentDirectory = databaseDirectory;
                return "Current directory: "+tokenizer.nextCommand(index);
            }
            else{
                return "folder doesn't exist, please create";
            }
        }
        else{
            return "Invalid database name";
        }
        //TODO: add query, DBcommands s
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

    public String parseCreate(){
        index++;
        if (Objects.equals(tokenizer.nextCommand(index), "table")) {
            return parseCreateTable();
        }
        else if(Objects.equals(tokenizer.nextCommand(index), "database")) {
            return parseCreateDatabase();
        }
        else{
            return "Invalid command";
        }
    }

    public String parseCreateDatabase() { //CREATE
        index++; //create database dbname;
        if(tokenizer.nextCommand(index).matches("[A-Za-z0-9]+")){
            index++;
        }
        else {
            return "Invalid query";
        }
        return "OK";
    }

    public String parseCreateTable() { //CREATE
        index++; //create table tablename (attributelist);
        if (tokenizer.nextCommand(index).matches("[A-Za-z0-9]+")) {
            index++;
        }
        else{
            return "Invalid table name";
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
                    return "Invalid attribute name(s)";
                }
            }
            if(attributeNames.isEmpty()){
                return "Missing attribute names";
            }
            index++;
        }
        else{
            return "OK";
            //TODO: create table name
        }
        return "OK";
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
