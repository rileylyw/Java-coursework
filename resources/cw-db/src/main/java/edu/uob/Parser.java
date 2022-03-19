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
    private static String DBName;
    private String tableName;
    private String tableFile;
    private ArrayList<String> attributeList;
    private DBTable table;
    private WriteToFile writeToFile = new WriteToFile();


    public Parser(String command){
        index = 0;
        this.tokens = tokenizer.splitCommand(command);
        tokenizer.setTokens(tokens);
        System.out.println(tokens);
    }

    public String parse() throws IOException {
        String nextCommand = tokenizer.nextCommand(index);
        if(index+1 >= tokens.size() || !Objects.equals(tokens.get(tokens.size()-1), ";")){
            return "[ERROR] Invalid query | Missing ;";
        }
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
            case "alter": //alter table
                System.out.println("alter");
                return parseAlterTable();
        }
        return "[OK]";
    }

    public String parseAlterTable() throws IOException {
        index++;
        if(!Objects.equals(tokenizer.nextCommand(index), "table")){
            return "[ERROR] Invalid query: try ALTER TABLE";
        }
        index++;
        tableFile = tokenizer.nextCommand(index)+".tab";
        tableName = tokenizer.nextCommand(index);
        File file = new File(currentDirectory, tableFile);
        if(!file.exists()){
            return "[ERROR] Table "+tableFile+" does not exist";
        }
        ReadInFile readInFile = new ReadInFile(file);
        this.table = new DBTable(readInFile.getAttributeList(),
                readInFile.getAttributeValues());
        index++;
        if(Objects.equals(tokenizer.nextCommand(index), "add")){
            index++;
            String attributeName = tokenizer.nextCommand(index);
            if(attributeName.matches("[A-Za-z0-9]+")) {
                table.addColumn(attributeName);
                writeToFile.writeAttribListToFile(DBName, tableName, table);
            }
            else{
                return "[ERROR] Invalid column name";
            }
        }
        else if(Objects.equals(tokenizer.nextCommand(index), "drop")){
            index++;
            String attributeName = tokenizer.nextCommand(index);
            table.dropColumn(attributeName);
            writeToFile.writeAttribListToFile(DBName, tableName, table);
        }
        else{
            return "[ERROR] Invalid query";
        }
        return "[OK]";
    }

    public String parseDrop(){
        index++;
        if(Objects.equals(tokenizer.nextCommand(index), "table")){
            index++;
            return parseDropTable();
        }
        else if(Objects.equals(tokenizer.nextCommand(index), "database")){
            index++;
            return parseDropDatabase();
        }
        else {
            return "[ERROR] Invalid query";
        }
    }

    public String parseDropTable(){
        tableFile = tokenizer.nextCommand(index)+".tab";
        File file = new File(currentDirectory, tableFile);
        if(file.exists()){
            file.delete();
//            System.out.println("current d "+currentDirectory);
            return "[ERROR] Table deleted";
        }
        else{
            return "[ERROR] Table doesn't exist";
        }
    }

    public String parseDropDatabase(){
        File file = new File("../"+tokenizer.nextCommand(index));
        if(file.exists() && file.isDirectory()){
            String[] files = file.list();
            for(String s: files){
                File currentFile = new File(file.getPath(),s);
                currentFile.delete();
            }
            file.delete();
            currentDirectory = null;
            return "[OK] Database deleted";
        }
        else{
            return "[ERROR] Database does not exist";
        }
    }



    public String parseUse(){ //USE DBName;
        index++;
        if(tokenizer.nextCommand(index).matches("[A-Za-z0-9]+")){ //dbname
            String path = "../" + tokenizer.nextCommand(index);
            File databaseDirectory = new File(path);
            if(databaseDirectory.exists()){
                currentDirectory = databaseDirectory;
                this.DBName = tokenizer.nextCommand(index);
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
        return "[OK]";
    }

    public String parseCreateTable() throws IOException { //CREATE
        index++; //create table tablename (attributelist);
        if (tokenizer.nextCommand(index).matches("[A-Za-z0-9]+")) {
            this.tableFile = tokenizer.nextCommand(index) + ".tab";
            this.tableName = tokenizer.nextCommand(index);
            index++;
        }
        else{
            return "[ERROR] Invalid table name";
        }
        if(index >= tokens.size()){
            return "[ERROR] Missing ;";
        }
        else if(Objects.equals(tokenizer.nextCommand(index), "(")) {
            attributeList = new ArrayList<>();
            index++;
            while (!(Objects.equals(tokenizer.nextCommand(index), ")"))) {
                if (tokenizer.nextCommand(index).matches("[A-Za-z0-9]+")) {
                    attributeList.add(tokenizer.nextCommand(index));
                    index++;
                    if (Objects.equals(tokenizer.nextCommand(index), ",")) {
                        index++;
                    }
                } else {
                    return "[ERROR] Invalid attribute name(s)";
                }
            }
            if (attributeList.isEmpty()) {
                return "[ERROR] Missing attribute names";
            } else {
                table = new DBTable(attributeList, null);
                writeToFile.writeAttribListToFile(DBName, tableName, table);
            }
            index++;
        }
        /* create table interp*/
//        System.out.println("current dir "+currentDirectory);
//        File file = new File(currentDirectory, tableFile);
        if(currentDirectory == null){
            return "[ERROR] No database, please create";
        }
        if(Objects.equals(tokenizer.nextCommand(index), ";")) {
            File file = new File(currentDirectory, tableFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            return "[OK]";
        }
        return "[ERROR] Invalid query";
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
