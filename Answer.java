package com.mkyong;

public class Answer {

    /**
     * Java class that models each individual answer in answers array and its appropiate attributes:
     */

    //A Field object: each field object has an id, question type, and optional ref
    private Field field;

    //A String representing the answer type: type of data accepted as answer
    private String type;

    //Next line lists the value of the type, followed by the respondent's answer
    private String text;

    //Choice consists of the model of the single answer the respondent selected
    private Choice choice;

    private Choices choices;

    //Default constructors
    public Answer() {

    }

    @Override
    public String toString() {
        String current = "Field: ";
        current += this.getField().toString();
        current += " type: " + this.getType();
        current += " text: " + this.getText();
        if (this.getChoice() == null) {
            current += " choices: " + this.getChoices().toString();
        } else {
            current += " choice: " + this.getChoice().toString();
        }
        return current;
    }

    /**
     * Possible constructros (depeending on information provided) for the model.
     */
    public Answer(Field field, String type, String text, Choice choice) {
        this.field = field;
        this.type = type;
        this.text = text;
        this.choice = choice;
    }

    public Answer(Field field, String type, String text, Choices choices) {
        this.field = field;
        this.type = type;
        this.text = text;
        this.choices = choices;
    }

    /**
     * Basic setters and getters for each attribute of the model.
     */
    public Choice getChoice() {
        return choice;
    }

    public Choices getChoices() {
        return choices;
    }

    public void setChoices(Choices choices) {
        this.choices = choices;
    }

    public void setChoice(Choice choice) {
        this.choice = choice;
    }

    public String getText() {
        return text;
    }

    public Field getField() {
        return field;
    }

    public String getType() {
        return type;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setType(String type) {
        this.type = type;
    }

}