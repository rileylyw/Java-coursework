package edu.uob.DBCommands;

import edu.uob.DBTable;
import edu.uob.Parser;
import edu.uob.WriteToFile;

import java.io.IOException;

public class AlterTableCommand extends Parser {
    public AlterTableCommand() {
        super("");
    }

    public String add(String attributeName, String tableName, WriteToFile writeToFile, DBTable table) throws IOException {
        if(attributeName.matches("[A-Za-z0-9]+")) {
            table.addColumn(attributeName);
            writeToFile.writeAttribListToFile(Parser.getDBName(), tableName, table);
        }
        else{
            return "[ERROR]: Invalid column name";
        }
        return "[OK]: Column "+attributeName+" added";
    }

    public String drop(String attributeName, String tableName, WriteToFile writeToFile, DBTable table) throws IOException {
        if(table.dropColumn(attributeName)){
            writeToFile.writeAttribListToFile(Parser.getDBName(), tableName, table);
            return "[OK]: Column "+attributeName+" dropped";
        }
        else{
            return "id column cannot be dropped | column does not exist";
        }
    }
}
