package edu.uob.DBCommands;

import edu.uob.AttributeList;
import edu.uob.Parser;

import java.util.ArrayList;

public class SelectCommand extends Parser {
    AttributeList al = new AttributeList();

    public SelectCommand() {
        super("");
    }

    public boolean populateAttributeList(int index, ArrayList<String> tokens,
                                         ArrayList<String> attributeList) {
        return al.populateAttributeList(index, tokens, attributeList);
    }

    public int getIndex() {
        return al.getIndex();
    }

}
