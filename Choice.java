package com.mkyong;


public class Choice {

    private String label;

    @Override
    public String toString() {
        return " Label: " + this.getLabel();
    }

    public Choice() {

    }

    public Choice(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


}