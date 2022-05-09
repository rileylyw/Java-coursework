package edu.uob;

import java.util.ArrayList;

public class GameAction {
//    private ArrayList<String> triggers;
    private ArrayList<String> subjects = new ArrayList<>();
    private ArrayList<String> consumed = new ArrayList<>();
    private ArrayList<String> produced = new ArrayList<>();

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

    //    public void addTrigger(String trigger) {
//        this.triggers.add(trigger);
//    }
}
