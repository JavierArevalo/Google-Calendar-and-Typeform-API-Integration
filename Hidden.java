package com.mkyong;

public class Hidden {

    private String name;
    private String date_today;
    private String date_tomorrow;
    private String date_thedayaftertomorrow;
    private Long dpi;

    public Hidden() {

    }

    public Hidden(String name, String date_today,
                       String date_tomorrow, String date_thedayaftertomorrow,
                       Long dpi) {
        this.date_thedayaftertomorrow = date_thedayaftertomorrow;
        this.date_today = date_today;
        this.date_tomorrow = date_tomorrow;
        this.name = name;
        this.dpi = dpi;
    }

    public Long getDpi() {
        return dpi;
    }

    public String getDate_thedayaftertomorrow() {
        return date_thedayaftertomorrow;
    }

    public String getDate_today() {
        return date_today;
    }

    public String getDate_tomorrow() {
        return date_tomorrow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate_thedayaftertomorrow(String date_thedayaftertomorrow) {
        this.date_thedayaftertomorrow = date_thedayaftertomorrow;
    }

    public void setDate_today(String date_today) {
        this.date_today = date_today;
    }

    public void setDate_tomorrow(String date_tomorrow) {
        this.date_tomorrow = date_tomorrow;
    }

    public void setDpi(Long dpi) {
        this.dpi = dpi;
    }

    @Override
    public String toString() {
        String ans = "Name : " + this.getName();
        ans += " date today: " + this.getDate_today();
        ans += " date tomorrow: " + this.getDate_tomorrow();
        ans += " date day after tomorrow: " + this.getDate_thedayaftertomorrow();
        ans += " dpi: " + this.getDpi();
        return ans;
    }

}