package edu.uob;

import edu.uob.DBCommands.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;


public class Parser {
//    private final Tokenizer tokenizer = new Tokenizer();
    private final ArrayList<String> tokens;
    private int index;
    private static File currentDirectory;
    private static String DBName;
    private String tableName;
    private String tableFile;
    private ArrayList<String> attributeList;
    private ArrayList<String> attributeValues;
    private DBTable table;
    private final WriteToFile writeToFile = new WriteToFile();


    public Parser(String command){
        Tokenizer tokenizer = new Tokenizer();
        index = 0;
        this.tokens = tokenizer.splitCommand(command);
        tokenizer.setTokens(tokens);
    }

    public String parse() throws IOException {
        String nextCommand = tokens.get(index).toLowerCase();
        if(index+1 >= tokens.size() || !Objects.equals(tokens.get(tokens.size()-1), ";")){
            return "[ERROR] Invalid query | Missing ;";
        }
        switch (nextCommand) {
            case "select" -> {
                return parseSelect();
            }
            case "use" -> {
                return parseUse();
            }
            case "create" -> {
                return parseCreate();
            }
            case "drop" -> {
                return parseDrop();
            }
            case "alter" -> { //alter table
                return parseAlterTable();
            }
            case "insert" -> { //insert into
                return parseInsertInto();
            }
            case "update" -> {
                return parseUpdate();
            }
            case "delete" -> { //delete from
                return parseDeleteFrom();
            }
            case "join" -> {
                return parseJoin();
            }
        }
        return "[OK]";
    }

    public String parseJoin() throws IOException {
        JoinCommand join = new JoinCommand();
        index++;
        tableFile = tokens.get(index)+".tab";
        tableName = tokens.get(index);
        index++;
        if(!Objects.equals(tokens.get(index).toLowerCase(), "and")){
            return "[ERROR]";
        }
        index++;
        String tableFile2 = tokens.get(index)+".tab";
        File file = new File(currentDirectory, tableFile);
        File file2 = new File(currentDirectory, tableFile2);
        if(!file.exists() || !file2.exists()){
            return "[ERROR] Table does not exist";
        }
        ReadInFile readInFile = new ReadInFile(file);
        ReadInFile readInFile2 = new ReadInFile(file2);
        table = new DBTable(readInFile.getAttributeList(), readInFile.getAttributeValues());
        DBTable table2 = new DBTable(readInFile2.getAttributeList(), readInFile2.getAttributeValues());
        index++;
        if(!Objects.equals(tokens.get(index).toLowerCase(), "on")){
            return "[ERROR]";
        }
        index++;
        String attributeName = tokens.get(index);
        index++;
        if(!Objects.equals(tokens.get(index).toLowerCase(), "and")){
            return "[ERROR]";
        }
        index++;
        String attributeName2 = tokens.get(index);
        return join.join(index, table, table2, attributeName, attributeName2, writeToFile);
    }

    public String parseDeleteFrom() throws IOException {
        index++;
        if(!Objects.equals(tokens.get(index).toLowerCase(), "from")){
            return "[ERROR]";
        }
        index++;
        tableFile = tokens.get(index)+".tab";
        tableName = tokens.get(index);
        File file = new File(currentDirectory, tableFile);
        ReadInFile readInFile = new ReadInFile(file);
        table = new DBTable(readInFile.getAttributeList(), readInFile.getAttributeValues());
        if(!file.exists()){
            return "[ERROR] Table does not exist";
        }
        if(attributeList==null){
            attributeList = readInFile.getAttributeList();
        }
        index++;
        if(!Objects.equals(tokens.get(index).toLowerCase(), "where")){
            return "[ERROR]";
        }
        index++; //name
        DBTable filteredTable;
        if(Objects.equals(tokens.get(index), "(")) { //multiple conditions
            filteredTable = multipleConditions(tokens, table);
        }
        else{ //single condition
            index--;
            filteredTable = conditions(tokens, index, table);
        }
        if(filteredTable.getAttributeValues().isEmpty()){
            return "[ERROR] Item does not exist";
        }
        ArrayList<HashMap<String, String>> rows = table.getAttributeValues();//original
        ArrayList<HashMap<String, String>> filteredRecords = filteredTable.getAttributeValues();
        HashSet<HashMap<String, String>> hs = new HashSet<>(filteredRecords);
        ArrayList<HashMap<String, String>> updatedRows = new ArrayList<>();

        for(HashMap<String, String> map: rows){
            if(!hs.contains(map)){
                updatedRows.add(map);
            }
        }
        table.setAttributeValuesWithoutID(updatedRows);
        writeToFile.writeAttribListToFile(Parser.getDBName(),tableName,table);
        return "[OK] Item(s) deleted";
    }

    public String parseUpdate() throws IOException {
        index++;
        tableFile = tokens.get(index)+".tab";
        tableName = tokens.get(index);
        File file = new File(currentDirectory, tableFile);
        ReadInFile readInFile = new ReadInFile(file);
        if(!file.exists()){
            return "[ERROR] Table does not exist";
        }
        if(attributeList==null){
            attributeList = readInFile.getAttributeList();
        }
        table = new DBTable(readInFile.getAttributeList(), readInFile.getAttributeValues());
        index++;
        if(!Objects.equals(tokens.get(index).toLowerCase(), "set")){
            return "[ERROR] Invalid query";
        }
        index++;
        HashMap<String, String> nameValuePair = new HashMap<>();
        while (!(Objects.equals(tokens.get(index).toLowerCase(), "where"))) {
            String attributeName = tokens.get(index);
            attributeValues = new ArrayList<>();
            if (!attributeName.matches("[A-Za-z0-9]+")) { //attribute value
                return "[ERROR]";
            }
            index++;
            if(!Objects.equals(tokens.get(index), "=")){
                return "[ERROR] Missing '='";
            }
            index++;
            isValue(tokens,index,attributeValues);
            if(!attributeValues.isEmpty()) {
                int pos = attributeValues.size() - 1;
                nameValuePair.put(attributeName, attributeValues.get(pos));
            }
        }
        index++;
        return "[OK] Data updated";
    }

    public String parseSelect() throws IOException { //SELECT
        SelectCommand select = new SelectCommand();
        index++; //e.g. select * from marks;
        if(Objects.equals(tokens.get(index), "*")){
            index++;
            if(!Objects.equals(tokens.get(index).toLowerCase(), "from")){
                return "[ERROR]";
            }
        }
        else{
            attributeList = new ArrayList<>();
            if(!select.populateAttributeList(index, tokens, attributeList)){
                return "[ERROR]: Invalid attribute names";
            }
            index = select.getIndex();
        }
        index++;
        tableFile = tokens.get(index)+".tab";
        tableName = tokens.get(index);
        File file = new File(currentDirectory, tableFile);
        ReadInFile readInFile = new ReadInFile(file);
        if(!file.exists()){
            return "[ERROR] Table does not exist";
        }
        if(attributeList==null){
            attributeList = readInFile.getAttributeList();
        }
        table = new DBTable(readInFile.getAttributeList(), readInFile.getAttributeValues());
        index++; //WHERE
        if(Objects.equals(tokens.get(index).toLowerCase(), ";")){
            String str = writeToFile.displayTableToClient(table, attributeList);
            return "[OK]\n"+str;
        }
        if(!Objects.equals(tokens.get(index).toLowerCase(), "where")){
            return "[ERROR]: Invalid query";
        }
        index++;
        if(Objects.equals(tokens.get(index), "(")){
            DBTable filteredTable = multipleConditions(tokens,table);
            String str = writeToFile.displayTableToClient(filteredTable, attributeList);
            return "[OK]\n"+str;
        }
        else {
            index--;
            DBTable filteredTable = conditions(tokens, index, table);
            String str = writeToFile.displayTableToClient(filteredTable, attributeList);
            return "[OK]\n"+str;
        }
    }

    public DBTable multipleConditions(ArrayList<String> tokens, DBTable currentTable){
        Stack filteredTables = new Stack(10);
        Stack operator = new Stack(10);
        DBTable filteredTable = null;

        for(int i=0; i< tokens.size(); i++){
            if(Objects.equals(tokens.get(i), "(") && !Objects.equals(tokens.get(i+1), "(")){
                filteredTable = conditions(tokens, i, currentTable);
                filteredTables.push(filteredTable);
            }
            if(Objects.equals(tokens.get(i).toLowerCase(), "and") ||
                    Objects.equals(tokens.get(i).toLowerCase(), "or")) {
                operator.pushOp(tokens.get(i).toLowerCase());
            }
            if(Objects.equals(tokens.get(i), ")")&& (Objects.equals(tokens.get(i+1), ")") ||
                    Objects.equals(tokens.get(i+1), ";"))) {
                String op = operator.popOp();
                DBTable temp = filteredTables.pop();
                DBTable temp2 = filteredTables.pop();
                ArrayList<HashMap<String, String>> rows1 = temp.getAttributeValues();
                ArrayList<HashMap<String, String>> rows2 = temp2.getAttributeValues();
                ArrayList<HashMap<String, String>> filteredRecords = new ArrayList<>();
                HashSet<HashMap<String, String>> hs2 = new HashSet<>(rows2);
                if(op.toLowerCase().equals("and")) {
                    for (HashMap map : rows1) {
                        if (hs2.contains(map)) {
                            filteredRecords.add(map);
                        }
                    }
                }
                else if(op.toLowerCase().equals("or")){
                    rows1.addAll(rows2);
                    HashSet<HashMap<String, String>> toClear = new HashSet<>(rows1);
                    for(HashMap map: currentTable.getAttributeValues()){
                        if(toClear.contains(map)){
                            filteredRecords.add(map);
                        }
                    }
                }
                if(Objects.equals(tokens.get(i+1), ";")) {
                    filteredTable = new DBTable(attributeList, filteredRecords);
                }
                else{
                    filteredTable = new DBTable(attributeList, filteredRecords);
                    filteredTables.push(filteredTable);
                }
            }
        }
        return filteredTable;
    }

    public DBTable conditions(ArrayList<String> tokens, int index, DBTable currentTable) {
        DBTable filteredTable = null;
        attributeValues = new ArrayList<>();
        ArrayList<HashMap<String, String>> rows = currentTable.getAttributeValues();
        ArrayList<HashMap<String, String>> filteredRecords = new ArrayList<>();
        index++;
        String attributeName = tokens.get(index);
        if(attributeName.matches("[A-Za-z0-9]+")) { //attribName, op, value
            index++;
            switch(tokens.get(index).toLowerCase()){
                case "=" -> {
                    index++;
                    if (Objects.equals(tokens.get(index), "=")) {
                        index++;
                        isValue(tokens, index, attributeValues); 
                        for (HashMap map : rows) {
                            if (map.containsKey(attributeName)) {
                                if (attributeValues.contains(map.get(attributeName))) {
                                    filteredRecords.add(map);
                                }
                            }
                        }
                        filteredTable = new DBTable(attributeList, filteredRecords);
                    }
                }
                case "!" -> {
                    index++;
                    if (Objects.equals(tokens.get(index), "=")) {
                        index++;
                        isValue(tokens, index, attributeValues); 
                        for (HashMap map : rows) {
                            if (map.containsKey(attributeName)) {
                                if (!attributeValues.contains(map.get(attributeName))) {
                                    filteredRecords.add(map);
                                }
                            }
                        }
                        filteredTable = new DBTable(attributeList, filteredRecords);
                    }
                }
                case ">" -> {
                    index++;
                    if (Objects.equals(tokens.get(index), "=")) {
                        index++;
                        isValue(tokens, index, attributeValues); 
                        int value = Integer.valueOf(attributeValues.get(0));
                        for (HashMap map : rows) {
                            if (map.containsKey(attributeName)) {
                                if (Integer.valueOf((String) map.get(attributeName)) >= value) {
                                    filteredRecords.add(map);
                                }
                            }
                        }
                        filteredTable = new DBTable(attributeList, filteredRecords);
                    }
                    else{
                        isValue(tokens, index, attributeValues); 
                        int value = Integer.valueOf(attributeValues.get(0));
                        for (HashMap map : rows) {
                            if (map.containsKey(attributeName)) {
                                if (Integer.valueOf((String) map.get(attributeName)) > value) {
                                    filteredRecords.add(map);
                                }
                            }
                        }
                        filteredTable = new DBTable(attributeList, filteredRecords);
                    }
                }
                case "<" -> {
                    index++;
                    if (Objects.equals(tokens.get(index), "=")) {
                        index++;
                        isValue(tokens, index, attributeValues); 
                        int value = Integer.valueOf(attributeValues.get(0));
                        for (HashMap map : rows) {
                            if (map.containsKey(attributeName)) {
                                if (Integer.valueOf((String) map.get(attributeName)) <= value) {
                                    filteredRecords.add(map);
                                }
                            }
                        }
                        filteredTable = new DBTable(attributeList, filteredRecords);
                    }
                    else{
                        isValue(tokens, index, attributeValues); 
                        int value = Integer.valueOf(attributeValues.get(0));
                        for (HashMap map : rows) {
                            if (map.containsKey(attributeName)) {
                                if (Integer.valueOf((String) map.get(attributeName)) < value) {
                                    filteredRecords.add(map);
                                }
                            }
                        }
                        filteredTable = new DBTable(attributeList, filteredRecords);
                    }
                }
                case "like" -> {
                    index++;
                    isValue(tokens, index, attributeValues);
                    for (HashMap map : rows) {
                        if (map.containsKey(attributeName)) {
                            String str = (String) map.get(attributeName);
                            String value = attributeValues.get(0);
                            if (str.contains(value)) {
                                filteredRecords.add(map);
                            }
                        }
                    }
                    filteredTable = new DBTable(attributeList, filteredRecords);
                }
            }
        }
        this.index = index;
        return filteredTable;
    }

    public boolean isValue(ArrayList<String> tokens, int index, ArrayList<String> attributeValues){
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
        else if(tokens.get(index).matches("^[+-]?\\d+[.]?\\d?+")){
            attributeValues.add(tokens.get(index));
            index++;
            if(Objects.equals(tokens.get(index), ",")){
                index++;
            }
        }
        else if(Objects.equals(tokens.get(index).toUpperCase(), "TRUE")||
                Objects.equals(tokens.get(index).toUpperCase(), "FALSE")||
                Objects.equals(tokens.get(index).toUpperCase(), "NULL")){
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
        InsertCommand insert = new InsertCommand();
        index++;
        if(!Objects.equals(tokens.get(index).toLowerCase(), "into")){
            return "[ERROR]: Invalid query: try INSERT INTO";
        }
        index++;
        tableFile = tokens.get(index)+".tab";
        tableName = tokens.get(index);
        File file = new File(currentDirectory, tableFile);
        if(!file.exists()){
            return "[ERROR]: Table does not exist";
        }
        index++;
        if(!Objects.equals(tokens.get(index).toLowerCase(), "values")){
            return "[ERROR]: Invalid query: try INSERT INTO <Table Name> VALUES (<Value List>);";
        }
        index++;

        if(Objects.equals(tokens.get(index), "(")) {
            attributeValues = new ArrayList<>();
            index++;
            while (!(Objects.equals(tokens.get(index), ")"))) {
                if(!isValue(tokens, index, attributeValues)){
                    return "[ERROR]: Invalid attribute name(s) | Invalid query";
                }
            }
            if (attributeValues.isEmpty()) { //TODO: if more col than existed, error
                return "[ERROR]: Missing attribute values";
            }
            return insert.insert(file, writeToFile, tableName, attributeValues);
        }
        return "[OK]";
    }



    public String stringLiteral(ArrayList<String> tokens, int index) {
        String sl = tokens.get(index);
        while(sl.charAt(sl.length()-1)!='\''
                && index<tokens.size()-1){
            index++;
            sl = sl.concat(tokens.get(index));
        }
        this.index = index;
        sl = sl.substring(1, sl.length()-1);
        return sl;
    }

    public String parseAlterTable() throws IOException {
        AlterTableCommand alter = new AlterTableCommand();
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
            return alter.add(attributeName, tableName, writeToFile, table);
        }
        else if(Objects.equals(tokens.get(index).toLowerCase(), "drop")){
            index++;
            String attributeName = tokens.get(index);
            return alter.drop(attributeName, tableName, writeToFile, table);
        }
        else{
            return "[ERROR] Invalid query";
        }
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
        DropCommand dropTable = new DropCommand();
        tableFile = tokens.get(index)+".tab";
        return dropTable.dropTable(tableFile);
    }

    public String parseDropDatabase(){
        DropCommand dropDatabase = new DropCommand();
        String path = Paths.get(".").toAbsolutePath()+File.separator+tokens.get(index);
        return dropDatabase.dropDatabase(path);
    }

    public String parseUse(){ //USE DBName;
        UseCommand use = new UseCommand();
        index++;
        if(tokens.get(index).matches("[A-Za-z0-9]+")){ //dbname
            DBName = tokens.get(index);
        }
        else{
            return "Invalid database name";
        }
        return use.useCommand();
    }

    public static String getDBName() {
        return DBName;
    }

    public static void setCurrentDirectory(File currentDirectory) {
        Parser.currentDirectory = currentDirectory;
    }

    public static File getCurrentDirectory() {
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
            return "[ERROR]: Invalid command";
        }
    }

    public String parseCreateDatabase() { //CREATE
        CreateCommand createDB = new CreateCommand();
        index++; //dbname;
        if(tokens.get(index).matches("[A-Za-z0-9]+")){
            DBName = tokens.get(index);
            index++;
        }
        else {
            return "[ERROR]: Invalid / missing database name";
        }
        return createDB.createDatabase();
    }

    public String parseCreateTable() throws IOException { //CREATE
        CreateCommand createTable = new CreateCommand();
        index++;
        if (tokens.get(index).matches("[A-Za-z0-9]+")) {
            this.tableFile = tokens.get(index) + ".tab";
            this.tableName = tokens.get(index);
            index++;
        }
        else{
            return "[ERROR]: Invalid table name";
        }
        if(index >= tokens.size()){
            return "[ERROR]: Missing ;";
        }
        else if(Objects.equals(tokens.get(index), "(")) {
            index++;
            attributeList = new ArrayList<>();
            if(!createTable.populateAttributeList(index, tokens, attributeList)){
                return "[ERROR]: Invalid attribute names";
            }
            index = createTable.getIndex();
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
            return createTable.createTable(tableFile, tableName);
        }
        return "[ERROR] Invalid query";
    }
}