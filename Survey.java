package com.mkyong;


public class Survey {

    //Each Survey object will have the following basic properties
    private String landing_id;
    private String token;
    private String landed_at;
    private String response_id;
    private String submitted_at;
    private Metadata metadata;

    //Note that the landing_id and token values are the same

    //Each Survey object will also have an answers array
    private Answer[] answers;

    private Hidden hidden;

    public Survey() {

    }

    public Survey(String landing_id, String token, String landed_at,
                  String submitted_at, Metadata metadata, Answer[] answers,
                  Hidden hidden, String response_id) {
        this.landing_id = landing_id;
        this.token = token;
        this.landed_at = landed_at;
        this.submitted_at = submitted_at;
        this.metadata = metadata;
        this.answers = answers;
        this.hidden = hidden;
        this.response_id = response_id;
    }

    public String getResponse_id() {
        return response_id;
    }

    public void setResponse_id(String response_id) {
        this.response_id = response_id;
    }

    public Answer[] getAnswers() {
        return answers;
    }

    public Hidden getHidden() {
        return hidden;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public String getLanded_at() {
        return landed_at;
    }

    public String getLanding_id() {
        return landing_id;
    }

    public String getSubmitted_at() {
        return submitted_at;
    }

    public String getToken() {
        return token;
    }

    public void setAnswers(Answer[] answers) {
        this.answers = answers;
    }

    public void setHidden(Hidden hidden) {
        this.hidden = hidden;
    }

    public void setLanded_at(String landed_at) {
        this.landed_at = landed_at;
    }

    public void setLanding_id(String landing_id) {
        this.landing_id = landing_id;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public void setSubmitted_at(String submitted_at) {
        this.submitted_at = submitted_at;
    }

    public void setToken(String token) {
        this.token = token;
    }

}