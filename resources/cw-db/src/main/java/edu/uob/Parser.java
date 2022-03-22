package edu.uob;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
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
    private ArrayList<String> attributeValues;
    private DBTable table;
    private WriteToFile writeToFile = new WriteToFile();


    public Parser(String command){
        index = 0;
        this.tokens = tokenizer.splitCommand(command);
        tokenizer.setTokens(tokens);
        System.out.println(tokens);
    }

    public String parse() throws IOException {
        String nextCommand = tokens.get(index).toLowerCase();
        if(index+1 >= tokens.size() || !Objects.equals(tokens.get(tokens.size()-1), ";")){
            return "[ERROR] Invalid query | Missing ;";
        }
        switch (nextCommand) {
            case "select" -> {
                System.out.println("select");
                return parseSelect();
            }
            case "use" -> {
                System.out.println("use");
                return parseUse();
            }
            case "create" -> {
                System.out.println("create");
                return parseCreate();
            }
            case "drop" -> {
                System.out.println("drop");
                return parseDrop();
            }
            case "alter" -> { //alter table
                System.out.println("alter");
                return parseAlterTable();
            }
            case "insert" -> { //insert into
                System.out.println("insert");
                return parseInsertInto();
            }
        }
        return "[OK]";
    }


    //select <wildattributelist> from table (where condition)
    public String parseSelect() throws IOException { //SELECT
        index++; //e.g. select * from marks;
        if(Objects.equals(tokens.get(index), "*")){
            index++;
        }
        else{
            attributeList = new ArrayList<>();
            while (!(Objects.equals(tokens.get(index).toLowerCase(), "from"))) {
                if (tokens.get(index).matches("[A-Za-z0-9]+")) {
                    attributeList.add(tokens.get(index));
                    index++;
                    if (Objects.equals(tokens.get(index), ",")) {
                        index++;
                    }
                } else {
                    return "[ERROR] Invalid attribute name(s)";
                }
            }
        }
        if(!Objects.equals(tokens.get(index).toLowerCase(), "from")){
            return "[ERROR]";
        }
        index++;
        tableFile = tokens.get(index)+".tab";
        tableName = tokens.get(index);
        File file = new File(currentDirectory, tableFile);
        System.out.println("file "+file);
        ReadInFile readInFile = new ReadInFile(file);
        if(!file.exists()){
            return "[ERROR] Table does not exist";
        }
        if(attributeList==null){
            attributeList = readInFile.getAttributeList();
        }
        table = new DBTable(readInFile.getAttributeList(), readInFile.getAttributeValues());
        index++; //WHERE
        if(!Objects.equals(tokens.get(index).toLowerCase(), "where")){
            String str = writeToFile.displayTableToClient(table, attributeList);
            return "[OK]\n"+str;
        }
        DBTable filteredTable = conditions(tokens, index,table);


        String str = writeToFile.displayTableToClient(filteredTable, attributeList);
        //TODO where conditions
        return "[OK]\n"+str;
    }

    public DBTable conditions(ArrayList<String> tokens, int index, DBTable currentTable) {
        Stack filteredTables = new Stack(10);
        DBTable filteredtable = null;
        attributeValues = new ArrayList<>();
        index++;
        //        Stack operator = new Stack(10);
        String attributeName = tokens.get(index);
        if(attributeName.matches("[A-Za-z0-9]+")) { //attribName, op, value
            index++;
            switch(tokens.get(index)){
                case "=":
                    index++;
                    if(Objects.equals(tokens.get(index), "=")){
                        index++;
                        isValue(tokens, index, attributeValues); //TODO: if not
                        ArrayList<HashMap<String, String>> rows = currentTable.getAttributeValues();
                        ArrayList<HashMap<String, String>> filteredRecords = new ArrayList<>();
                        for(HashMap map: rows){
                            if(map.containsKey(attributeName)){
                                if (attributeValues.contains(map.get(attributeName))) {
                                    filteredRecords.add(map);
                                }
                            }
                        }
                        filteredtable = new DBTable(attributeList, filteredRecords);
                    }
                case "!":
                    index++;
                    if(Objects.equals(tokens.get(index), "=")){
                        index++;
                        isValue(tokens, index, attributeValues); //TODO: if not
                        ArrayList<HashMap<String, String>> rows = currentTable.getAttributeValues();
                        ArrayList<HashMap<String, String>> filteredRecords = new ArrayList<>();
                        for(HashMap map: rows){
                            if(map.containsKey(attributeName)){
                                if (!attributeValues.contains(map.get(attributeName))) {
                                    filteredRecords.add(map);
                                }
                            }
                        }
                        filteredtable = new DBTable(attributeList, filteredRecords);
                    }
                case ">":
                    index++;
                    isValue(tokens, index, attributeValues); //TODO: if not
                    int value = Integer.valueOf(attributeValues.get(0));
                    ArrayList<HashMap<String, String>> rows = currentTable.getAttributeValues();
                    ArrayList<HashMap<String, String>> filteredRecords = new ArrayList<>();
                    for(HashMap map: rows){
                        if(map.containsKey(attributeName)){
                            if(Integer.valueOf((String)map.get(attributeName))>value){
                                filteredRecords.add(map);
                            }
                        }
                    }
                    filteredtable = new DBTable(attributeList, filteredRecords);
            }
        }
        this.index = index;
        return filteredtable;
    }

    public boolean isValue(ArrayList<String> tokens, int index, ArrayList<String> attributeValues){
//        ArrayList<String> values = new ArrayList<>();
        if(Objects.equals(tokens.get(index), "'")){
            index++;
            String sl = "";
            while(!Objects.equals(tokens.get(index), "'")&&
                    index< tokens.size()-1){
                sl = sl.concat(tokens.get(index));
                index++;
            }
            index++;
            attributeValues.add(sl);
            if(Objects.equals(tokens.get(index), ",")){
                index++;
            }
        }
        else if(tokens.get(index).charAt(0)=='\''){ //check if is value
            String sl = stringLiteral(tokens, index);
            attributeValues.add(sl);
            index++;
            if(Objects.equals(tokens.get(index), ",")){
                index++;
            }
        }
        else if(tokens.get(index).matches("^[+-]?\\d+[.]?{1}\\d+")){
            attributeValues.add(tokens.get(index));
            index++;
            if(Objects.equals(tokens.get(index), ",")){
                index++;
            }
        }
        else if(Objects.equals(tokens.get(index), "TRUE")||
                Objects.equals(tokens.get(index), "FALSE")||
                Objects.equals(tokens.get(index), "NULL")){
            attributeValues.add(tokens.get(index).toUpperCase());
            index++;
            if(Objects.equals(tokens.get(index), ",")){
                index++;
            }
        }
        else{
            return false;
        }
        this.index = index;
        return true;
    }


    public String parseInsertInto() throws IOException {
        index++;
        if(!Objects.equals(tokens.get(index).toLowerCase(), "into")){
            return "[ERROR] Invalid query: try INSERT INTO";
        }
        index++;
        tableFile = tokens.get(index)+".tab";
        tableName = tokens.get(index);
        File file = new File(currentDirectory, tableFile);
        if(!file.exists()){
            return "[ERROR] Table does not exist";
        }
        index++;
        if(!Objects.equals(tokens.get(index).toLowerCase(), "values")){
            return "[ERROR] Invalid query: try INSERT INTO <Table Name> VALUES (<Value List>);";
        }
        index++;

        if(Objects.equals(tokens.get(index), "(")) {
            attributeValues = new ArrayList<>();
            index++;
            while (!(Objects.equals(tokens.get(index), ")"))) {
                if(!isValue(tokens, index, attributeValues)){
                    return "[ERROR] Invalid attribute name(s) | Invalid query";
                }
            }
            if (attributeValues.isEmpty()) { //TODO: if more col than existed, error
                return "[ERROR] Missing attribute values";
            }
//            System.out.println("HERE "+attributeValues);
            ReadInFile readInFile = new ReadInFile(file);
            table = new DBTable(readInFile.getAttributeList(), readInFile.getAttributeValues());
            table.addAttributeValues(attributeValues,readInFile.getAttributeList());
            writeToFile.writeAttribListToFile(DBName,tableName,table);
            System.out.println(table.getAttributeValues());
        }
        return "[OK]";
    }



    public String stringLiteral(ArrayList<String> tokens, int index) {
        String sl = tokens.get(index);
        while(sl.charAt(sl.length()-1)!='\''
                && index<tokens.size()-1){
            index++;
            sl = sl.concat(tokens.get(index));
//            stringLiteral = stringLiteral.concat(" ").concat(tokens.get(index));
        }
        this.index = index;
        System.out.println(sl);
        sl = sl.substring(1, sl.length()-1);
        return sl;
    }

    public String parseAlterTable() throws IOException {
        index++;
        if(!Objects.equals(tokens.get(index).toLowerCase(), "table")){
            return "[ERROR] Invalid query: try ALTER TABLE";
        }
        index++;
        tableFile = tokens.get(index)+".tab";
        tableName = tokens.get(index);
        File file = new File(currentDirectory, tableFile);
        if(!file.exists()){
            return "[ERROR] Table "+tableFile+" does not exist";
        }
        ReadInFile readInFile = new ReadInFile(file);
        this.table = new DBTable(readInFile.getAttributeList(),
                readInFile.getAttributeValues());
        index++;
        if(Objects.equals(tokens.get(index).toLowerCase(), "add")){
            index++;
            String attributeName = tokens.get(index);
            if(attributeName.matches("[A-Za-z0-9]+")) {
                table.addColumn(attributeName);
                writeToFile.writeAttribListToFile(DBName, tableName, table);
            }
            else{
                return "[ERROR] Invalid column name";
            }
        }
        else if(Objects.equals(tokens.get(index).toLowerCase(), "drop")){
            index++;
            String attributeName = tokens.get(index);
            if(table.dropColumn(attributeName)){
                writeToFile.writeAttribListToFile(DBName, tableName, table);
                return attributeName+" dropped";
            }
            else{
                return "id column cannot be dropped | column does not exist";
            }
        }
        else{
            return "[ERROR] Invalid query";
        }
        return "[OK]";
    }

    public String parseDrop(){
        index++;
        if(Objects.equals(tokens.get(index).toLowerCase(), "table")){
            index++;
            return parseDropTable();
        }
        else if(Objects.equals(tokens.get(index).toLowerCase(), "database")){
            index++;
            return parseDropDatabase();
        }
        else {
            return "[ERROR] Invalid query";
        }
    }

    public String parseDropTable(){
        tableFile = tokens.get(index)+".tab";
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
        File file = new File("../"+tokens.get(index));
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
        if(tokens.get(index).matches("[A-Za-z0-9]+")){ //dbname
            String path = "../" + tokens.get(index);
            File databaseDirectory = new File(path);
            if(databaseDirectory.exists()){
                currentDirectory = databaseDirectory;
                this.DBName = tokens.get(index);
                return "Current directory: "+tokens.get(index);
            }
            else{
                return "Database doesn't exist, please create";
            }
        }
        else{
            return "Invalid database name";
        }
    }

    public File getCurrentDirectory() {
        return currentDirectory;
    }

    public String parseCreate() throws IOException {
        index++;
        if (Objects.equals(tokens.get(index).toLowerCase(), "table")) {
            return parseCreateTable();
        }
        else if(Objects.equals(tokens.get(index).toLowerCase(), "database")) {
            return parseCreateDatabase();
        }
        else{
            return "Invalid command";
        }
    }

    public String parseCreateDatabase() { //CREATE
        index++; //dbname;
        if(tokens.get(index).matches("[A-Za-z0-9]+")){
            this.DBName = tokens.get(index);
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
        if (tokens.get(index).matches("[A-Za-z0-9]+")) {
            this.tableFile = tokens.get(index) + ".tab";
            this.tableName = tokens.get(index);
            index++;
        }
        else{
            return "[ERROR] Invalid table name";
        }
        if(index >= tokens.size()){
            return "[ERROR] Missing ;";
        }
        else if(Objects.equals(tokens.get(index), "(")) {
            attributeList = new ArrayList<>();
            index++;
            while (!(Objects.equals(tokens.get(index), ")"))) {
                if (tokens.get(index).matches("[A-Za-z0-9]+")) {
                    attributeList.add(tokens.get(index));
                    index++;
                    if (Objects.equals(tokens.get(index), ",")) {
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
        if(currentDirectory == null){
            return "[ERROR] No database, please create";
        }
        if(Objects.equals(tokens.get(index), ";")) {
            File file = new File(currentDirectory, tableFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            return "[OK]";
        }
        return "[ERROR] Invalid query";
    }






}
