package edu.uob;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class Parser {
    private Tokenizer tokenizer = new Tokenizer();
    public ArrayList<String> tokens;
    private int index;
    private static File currentDirectory;
    private static String DBName;
    private String tableName;
    private String tableName2;
    private String tableFile;
    private String tableFile2;
    private ArrayList<String> attributeList;
    private ArrayList<String> attributeValues;
    private DBTable table;
    private DBTable table2;
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
            case "update" -> {
                System.out.println("update");
                return parseUpdate();
            }
            case "delete" -> { //delete from
                System.out.println("delete");
                return parseDeleteFrom();
            }
            case "join" -> {
                System.out.println("join");
                return parseJoin();
            }
        }
        return "[OK]";
    }

    public String parseJoin() throws IOException {
        index++;
        tableFile = tokens.get(index)+".tab";
        tableName = tokens.get(index);
        index++;
        if(!Objects.equals(tokens.get(index).toLowerCase(), "and")){
            return "[ERROR]";
        }
        index++;
        tableFile2 = tokens.get(index)+".tab";
        tableName2 = tokens.get(index);
        File file = new File(currentDirectory, tableFile);
        File file2 = new File(currentDirectory, tableFile2);
        if(!file.exists() || !file2.exists()){
            return "[ERROR] Table does not exist";
        }
        ReadInFile readInFile = new ReadInFile(file);
        ReadInFile readInFile2 = new ReadInFile(file2);
        table = new DBTable(readInFile.getAttributeList(), readInFile.getAttributeValues());
        table2 = new DBTable(readInFile2.getAttributeList(), readInFile2.getAttributeValues());
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
        ArrayList<HashMap<String, String>> rows = table.getAttributeValues();
        ArrayList<HashMap<String, String>> rows2 = table2.getAttributeValues();

        DBTable temp; //store new values
        ArrayList<HashMap<String, String>> tempRows = new ArrayList<>();
        ArrayList<String> combinedColumns = table.getAttributeList();
        combinedColumns.addAll(table2.getAttributeList());
        combinedColumns.remove("id");
        combinedColumns.remove(attributeName);
        combinedColumns.remove(attributeName2);
        int id = 1;
        for(HashMap map: rows){
            for(HashMap map2: rows2){
                if (map.get(attributeName).equals(map2.get(attributeName2))) {
                    HashMap<String, String> combinedRows = new HashMap<>();
                    combinedRows.putAll(map);
                    combinedRows.putAll(map2);
                    combinedRows.replace("id", String.valueOf(id));
                    id++;
                    tempRows.add(combinedRows);
                }
            }
        }
        temp = new DBTable(combinedColumns, tempRows);
        System.out.println(temp.getAttributeList());
        String str = writeToFile.displayTableToClient(temp,temp.getAttributeList());
        return "[OK]\n"+str;
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
            filteredTable = multipleConditions(tokens, index, table);
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

        for(HashMap map: rows){
            if(!hs.contains(map)){
                updatedRows.add(map);
            }
        } //TODO: if not exist
        table.setAttributeValuesWithoutID(updatedRows);
        writeToFile.writeAttribListToFile(DBName,tableName,table);
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
            System.out.println("attri name "+attributeName);
            attributeValues = new ArrayList<>();
            if (!attributeName.matches("[A-Za-z0-9]+")) { //attribute value
                return "[ERROR]";
            }
            index++;
            System.out.println("index "+tokens.get(index));

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

//        System.out.println("table records:");
//        System.out.println(table.getAttributeValues());
//        System.out.println("tokens:");
//        System.out.println(tokens);
        index++;
        DBTable filteredTable;
        if(Objects.equals(tokens.get(index), "(")) { //multiple conditions
            filteredTable = multipleConditions(tokens, index, table);
        }
        else{ //single condition
            index--;
            filteredTable = conditions(tokens, index, table);
        }
        ArrayList<HashMap<String, String>> rows = table.getAttributeValues();//original
        ArrayList<HashMap<String, String>> filteredRecords = filteredTable.getAttributeValues();
        HashSet<HashMap<String, String>> hs = new HashSet<>(filteredRecords);

        System.out.println("Namevalue "+nameValuePair);

        for(HashMap map: rows){
            if(hs.contains(map)){
                for(String key : nameValuePair.keySet()){
                    map.replace(key, nameValuePair.get(key));
                }
            }
        }
        writeToFile.writeAttribListToFile(DBName,tableName,table);
//        rows.clear();
//        filteredRecords.clear();
//        hs.clear();
//        System.out.println("values "+attributeValues);
//        attributeValues.clear();
//        System.out.printf("table values\n");
//        System.out.println(table.getAttributeValues());

//        System.out.println("filtered records:");
//        System.out.println(filteredTable.getAttributeValues());
//        System.out.println("Namevalue "+nameValuePair);

//        String str = writeToFile.displayTableToClient(filteredTable, attributeList);
//        return "[OK]\n"+str;
        return "[OK] Data updated";
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
//        System.out.println("file "+file);
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
            //TODO: check multiple conditions
            DBTable filteredTable = multipleConditions(tokens, index,table);
            String str = writeToFile.displayTableToClient(filteredTable, attributeList);
            return "[OK]\n"+str;
        }
        else {
            index--;
            DBTable filteredTable = conditions(tokens, index, table);
            String str = writeToFile.displayTableToClient(filteredTable, attributeList);
            return "[OK]\n"+str;
        }
//        return "[OK]\n"+str;
    }

    public DBTable multipleConditions(ArrayList<String> tokens, int index, DBTable currentTable){
        // ((cond1) AND (cond2)) AND ((cond3) OR (cond4))
        System.out.println("called method");
        Stack filteredTables = new Stack(10);
        Stack operator = new Stack(10);
        DBTable filteredtable = null;

        for(int i=0; i< tokens.size(); i++){
            if(Objects.equals(tokens.get(i), "(") && !Objects.equals(tokens.get(i+1), "(")){
                System.out.println("fetch and push");
                System.out.println(tokens.get(i + 1));
                filteredtable = conditions(tokens, i, currentTable);
                System.out.println(filteredtable.getAttributeValues());
                filteredTables.push(filteredtable);
            }
//            if(Objects.equals(tokens.get(i), ")")){
////            if(Objects.equals(tokens.get(i), ")") && !Objects.equals(tokens.get(i+1), ")")){
//                filteredTables.push(filteredtable);
//            }
            if(Objects.equals(tokens.get(i).toLowerCase(), "and") ||
                    Objects.equals(tokens.get(i).toLowerCase(), "or")) {
                operator.pushOp(tokens.get(i).toLowerCase());
            }
            if(Objects.equals(tokens.get(i), ")")&& (Objects.equals(tokens.get(i+1), ")") ||
                    Objects.equals(tokens.get(i+1), ";"))) {
                //TODO nested conditions
//                System.out.println("pop and pop");
//                System.out.println(tokens.get(i + 1));
                String op = operator.popOp();
                DBTable temp = filteredTables.pop();
                DBTable temp2 = filteredTables.pop();
//                System.out.println("temp records");
//                System.out.println(temp.getAttributeValues());
//                System.out.println("temp 2 records");
//                System.out.println(temp2.getAttributeValues());
                ArrayList<HashMap<String, String>> rows1 = temp.getAttributeValues();
                ArrayList<HashMap<String, String>> rows2 = temp2.getAttributeValues();
                ArrayList<HashMap<String, String>> filteredRecords = new ArrayList<>();
//                HashSet<HashMap<String, String>> hs1 = new HashSet<>(rows1);
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
                    filteredtable = new DBTable(attributeList, filteredRecords);
                }
                else{
                    filteredtable = new DBTable(attributeList, filteredRecords);
                    filteredTables.push(filteredtable);
                }
            }
        }
        return filteredtable;
    }

    public DBTable conditions(ArrayList<String> tokens, int index, DBTable currentTable) {
//        Stack filteredTables = new Stack(10);
//        Stack operator = new Stack(10);
        DBTable filteredtable = null;
        attributeValues = new ArrayList<>();
        index++;
        String attributeName = tokens.get(index);
        if(attributeName.matches("[A-Za-z0-9]+")) { //attribName, op, value
            index++;
            switch(tokens.get(index).toLowerCase()){
                case "=" -> {
                    index++;
                    if (Objects.equals(tokens.get(index), "=")) {
                        index++;
                        isValue(tokens, index, attributeValues); //TODO: if not
                        ArrayList<HashMap<String, String>> rows = currentTable.getAttributeValues();
                        ArrayList<HashMap<String, String>> filteredRecords = new ArrayList<>();
                        for (HashMap map : rows) {
                            if (map.containsKey(attributeName)) {
                                if (attributeValues.contains(map.get(attributeName))) {
                                    filteredRecords.add(map);
                                }
                            }
                        }
                        filteredtable = new DBTable(attributeList, filteredRecords);
                    }
                }
                case "!" -> {
                    index++;
                    if (Objects.equals(tokens.get(index), "=")) {
                        index++;
                        isValue(tokens, index, attributeValues); //TODO: if not
                        ArrayList<HashMap<String, String>> rows = currentTable.getAttributeValues();
                        ArrayList<HashMap<String, String>> filteredRecords = new ArrayList<>();
                        for (HashMap map : rows) {
                            if (map.containsKey(attributeName)) {
                                if (!attributeValues.contains(map.get(attributeName))) {
                                    filteredRecords.add(map);
                                }
                            }
                        }
                        filteredtable = new DBTable(attributeList, filteredRecords);
                    }
                }
                case ">" -> {
                    index++;
                    if (Objects.equals(tokens.get(index), "=")) {
                        index++;
                        isValue(tokens, index, attributeValues); //TODO: if not
                        int value = Integer.valueOf(attributeValues.get(0));
                        ArrayList<HashMap<String, String>> rows = currentTable.getAttributeValues();
                        ArrayList<HashMap<String, String>> filteredRecords = new ArrayList<>();
                        for (HashMap map : rows) {
                            if (map.containsKey(attributeName)) {
                                if (Integer.valueOf((String) map.get(attributeName)) >= value) {
                                    filteredRecords.add(map);
                                }
                            }
                        }
                        filteredtable = new DBTable(attributeList, filteredRecords);
                    }
                    else{
                        isValue(tokens, index, attributeValues); //TODO: if not
                        int value = Integer.valueOf(attributeValues.get(0));
                        ArrayList<HashMap<String, String>> rows = currentTable.getAttributeValues();
                        ArrayList<HashMap<String, String>> filteredRecords = new ArrayList<>();
                        for (HashMap map : rows) {
                            if (map.containsKey(attributeName)) {
                                if (Integer.valueOf((String) map.get(attributeName)) > value) {
                                    filteredRecords.add(map);
                                }
                            }
                        }
                        filteredtable = new DBTable(attributeList, filteredRecords);
                    }
                }
                case "<" -> {
                    index++;
                    if (Objects.equals(tokens.get(index), "=")) {
                        index++;
                        isValue(tokens, index, attributeValues); //TODO: if not
                        int value = Integer.valueOf(attributeValues.get(0));
                        ArrayList<HashMap<String, String>> rows = currentTable.getAttributeValues();
                        ArrayList<HashMap<String, String>> filteredRecords = new ArrayList<>();
                        for (HashMap map : rows) {
                            if (map.containsKey(attributeName)) {
                                if (Integer.valueOf((String) map.get(attributeName)) <= value) {
                                    filteredRecords.add(map);
                                }
                            }
                        }
                        filteredtable = new DBTable(attributeList, filteredRecords);
                    }
                    else{
                        isValue(tokens, index, attributeValues); //TODO: if not
                        int value = Integer.valueOf(attributeValues.get(0));
                        ArrayList<HashMap<String, String>> rows = currentTable.getAttributeValues();
                        ArrayList<HashMap<String, String>> filteredRecords = new ArrayList<>();
                        for (HashMap map : rows) {
                            if (map.containsKey(attributeName)) {
                                if (Integer.valueOf((String) map.get(attributeName)) < value) {
                                    filteredRecords.add(map);
                                }
                            }
                        }
                        filteredtable = new DBTable(attributeList, filteredRecords);
                    }
                }
                case "like" -> {
                    index++;
                    isValue(tokens, index, attributeValues); //TODO: if not
                    ArrayList<HashMap<String, String>> rows = currentTable.getAttributeValues();
                    ArrayList<HashMap<String, String>> filteredRecords = new ArrayList<>();
                    for (HashMap map : rows) {
                        if (map.containsKey(attributeName)) {
                            String str = (String) map.get(attributeName);
                            String value = attributeValues.get(0);
                            if (str.contains(value)) {
                                filteredRecords.add(map);
                            }
                        }
                    }
                    filteredtable = new DBTable(attributeList, filteredRecords);
                }


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
        else if(tokens.get(index).matches("^[+-]?\\d+[.]?{1}\\d?+")){
            System.out.println("isvalue "+tokens.get(index));
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
//            System.out.println(table.getAttributeValues());
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
//        System.out.println(sl);
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
