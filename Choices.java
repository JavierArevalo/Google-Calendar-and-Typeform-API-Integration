package com.mkyong;

public class Choices {

    private String[] labels;

    @Override
    public String toString() {
        String current = "Labels: [";
        for (int i = 0; i < labels.length - 1; i++) {
            current += labels[i] + ", ";
        }
        current += labels[labels.length -1];
        current += "]";
        return current;
    }

    public Choices() {

    }

    public Choices(String[] labels) {
        this.labels = labels;
    }

    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }

}