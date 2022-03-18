package edu.uob;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Parser {
    private Tokenizer tokenizer = new Tokenizer();
    public ArrayList<String> tokens;
    private int index;
    private static File currentDirectory;
    private String DBName;
    private String tableName;
    private ArrayList<String> attributeList;

    public Parser(String command){
        index = 0;
        this.tokens = tokenizer.splitCommand(command);
        tokenizer.setTokens(tokens);
        System.out.println(tokens);
    }

    public String parse() throws IOException {
        String nextCommand = tokenizer.nextCommand(index);
        switch(nextCommand){
            case "select":
                System.out.println("select");
                parseSelect();
                break;
            case "use":
                System.out.println("use");
                return parseUse();
            case "create":
                System.out.println("create");
                return parseCreate();
            case "drop":
                System.out.println("drop");
                return parseDrop();
        }
        if(index >= tokens.size() || (!Objects.equals(tokenizer.nextCommand(index), ";"))){
            return "Missing ;";
        }
        return "OK";
    }

    public String parseDrop(){ //drop <structure> <structureName>
        index++;
        if(Objects.equals(tokenizer.nextCommand(index), "table")) {
            if(tokenizer.nextCommand(index).matches("[A-Za-z0-9]+")){
                //TODO: drop table;
            }
            else{
                return "Table doesn't exist";
            }
        }
        else if(Objects.equals(tokenizer.nextCommand(index), "database")){
            if(tokenizer.nextCommand(index).matches("[A-Za-z0-9]+")){
                //TODO:drop DB;
            }
            else{
                return "Database doesn't exist";
            }
        }
        return "Invalid query";
    }

    public String parseUse(){ //USE DBName;
        index++;
        if(tokenizer.nextCommand(index).matches("[A-Za-z0-9]+")){ //dbname
            String path = "../" + tokenizer.nextCommand(index);
            File databaseDirectory = new File(path);
            if(databaseDirectory.exists()){
                currentDirectory = databaseDirectory;
                System.out.println("HERE "+ currentDirectory);
                if(index+1 >= tokens.size() || (!Objects.equals(tokenizer.nextCommand(index+1), ";"))){
                    return "Missing ;";
                }
                return "Current directory: "+tokenizer.nextCommand(index);
            }
            else{
                return "Database doesn't exist, please create";
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
        }
        return false;
    }

    public String parseCreate() throws IOException {
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
        index++; //dbname;
        if(tokenizer.nextCommand(index).matches("[A-Za-z0-9]+")){
            this.DBName = tokenizer.nextCommand(index);
            index++;
            if(index >= tokens.size() ||!Objects.equals(tokenizer.nextCommand(index), ";")){
                return "Missing ;";
            }
            String path = "../" + DBName;
            File databaseDirectory = new File(path);
            if(databaseDirectory.exists()) {
                return "Database already exists";
            }
            databaseDirectory.mkdir(); //create directory
        }
        else {
            return "Invalid / missing database name";
        }
        return "OK";
    }

    public String parseCreateTable() throws IOException { //CREATE
        index++; //create table tablename (attributelist);
        if (tokenizer.nextCommand(index).matches("[A-Za-z0-9]+")) {
            this.tableName = tokenizer.nextCommand(index) + ".tab";
            index++;
        }
        else{
            return "Invalid table name";
        }
        if(index >= tokens.size()){
            return "Missing ;";
        }
        else if(Objects.equals(tokenizer.nextCommand(index), "(")){
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
            if(index >= tokens.size() ||!Objects.equals(tokenizer.nextCommand(index), ";")){
                return "Missing ;";
            }
        }
        /* create table interp*/
        System.out.println("current dir "+currentDirectory);
        File file = new File(currentDirectory, tableName);
        if(currentDirectory == null){
            return "No database, please create";
        }
        if (!file.exists()){
            file.createNewFile();
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
