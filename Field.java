package com.mkyong;

public class Field {

    //The unique id for the question. Must use it to match answers to questions
    //Can be used to match questions to answers
    private String id;

    //question type such as short text, email or opinion scale, etc
    private String type;

    //Question I am asking? Does my survey have ref???
    //Readable name I specified for the field when creating the form
    //Can be used to match questions to answers
    //Not included unless specified when created the form, which is why two constructors are provided
    private String ref;


    //Constructors: default, and then one with two parameters that delegetes
    //to the one with three parameters since one attribute is optional.
    public Field() {

    }

    @Override
    public String toString() {
        String current = "Id: " + this.getId();
        current += " type: " + this.getType();
        if (this.getRef() != null) {
            current += " Ref: " + this.getRef();
        }
        return current;
    }

    public Field(String id, String type) {
        this(id, type, null);
    }

    public Field(String id, String type, String ref) {
        this.id = id;
        this.type = type;
        this.ref = ref;
    }


    //Basic setters and getters for each attribute:
    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public String getRef() {
        return this.ref;
    }


}