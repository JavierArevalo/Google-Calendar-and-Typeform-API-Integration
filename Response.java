package com.mkyong;

public class Response {

    int total_items;
    int page_count;
    //Each survey is an item in the items array
    Survey[] items;

    public Response() {

    }

    public Response(int total_items, int page_count, Survey[] items) {
        this.total_items = total_items;
        this.page_count = page_count;
        this.items = items;
    }

    public int getPage_count() {
        return page_count;
    }

    public int getTotal_items() {
        return total_items;
    }

    public Survey[] getItems() {
        return items;
    }

    public void setItems(Survey[] items) {
        this.items = items;
    }

    public void setPage_count(int page_count) {
        this.page_count = page_count;
    }

    public void setTotal_items(int total_items) {
        this.total_items = total_items;
    }

}