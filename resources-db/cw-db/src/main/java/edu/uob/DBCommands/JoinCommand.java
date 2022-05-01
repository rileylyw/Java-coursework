package edu.uob.DBCommands;

import edu.uob.DBTable;
import edu.uob.Parser;
import edu.uob.WriteToFile;

import java.util.ArrayList;
import java.util.HashMap;

public class JoinCommand extends Parser {
    private int index;

    public JoinCommand() {
        super("");
    }

    public String join(int currentIndex,
                       DBTable table, DBTable table2, String attributeName, String attributeName2,
                       WriteToFile writeToFile){
        this.index = currentIndex;
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
        for(HashMap<String, String> map: rows){
            for(HashMap<String, String> map2: rows2){
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
        String str = writeToFile.displayTableToClient(temp,temp.getAttributeList());
        return "[OK]\n"+str;
    }

}
