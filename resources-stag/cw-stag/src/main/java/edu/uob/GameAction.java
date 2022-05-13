package edu.uob;

import java.util.ArrayList;

public class GameAction {
    private ArrayList<String> subjects = new ArrayList<>();
    private ArrayList<String> consumed = new ArrayList<>();
    private ArrayList<String> produced = new ArrayList<>();
    private String narration;

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getNarration() {
        return narration;
    }

    public void removeSubject(String toRemove){
        subjects.remove(toRemove);
    }

    public void addConsumed(String consumed) {
        this.consumed.add(consumed);
    }

    public void addSubject(String subject) {
        this.subjects.add(subject);
    }

    public void addProduced(String produced) {
        this.produced.add(produced);
    }

    public ArrayList<String> getConsumed() {
        return consumed;
    }

    public ArrayList<String> getProduced() {
        return produced;
    }

    public ArrayList<String> getSubjects() {
        return subjects;
    }

}
