package edu.uob;

import java.util.ArrayList;
import java.util.Objects;

public class AttributeList extends Parser{
    private int index;

    public AttributeList() {
        super("");
    }

    public boolean populateAttributeList(int currentIndex, ArrayList<String> tokens,
                                         ArrayList<String> attributeList){
        this.index = currentIndex;
        if(Objects.equals(tokens.get(index-1), "(")) {
            while (!Objects.equals(tokens.get(index), ")")) {
                if (tokens.get(index).matches("[A-Za-z0-9]+")) {
                    attributeList.add(tokens.get(index));
                    index++;
                    if (Objects.equals(tokens.get(index), ",")) {
                        index++;
                    }
                } else {
                    if (!Objects.equals(tokens.get(index), ")")) {
                        return false;
                    }
                }
            }
        }
        else {
            while (!Objects.equals(tokens.get(index).toLowerCase(), "from")) {
                if (tokens.get(index).matches("[A-Za-z0-9]+")) {
                    attributeList.add(tokens.get(index));
                    index++;
                    if (Objects.equals(tokens.get(index), ",")) {
                        index++;
                    }
                } else {
                    if (!Objects.equals(tokens.get(index).toLowerCase(), "from")) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public int getIndex() {
        return this.index;
    }
}
