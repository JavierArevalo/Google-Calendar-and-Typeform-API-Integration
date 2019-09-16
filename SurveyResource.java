package com.mkyong;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class SurveyResource {

    /**
     * This class will be used as an internal API to provide the useful information about the surveys completed to the
     * main java program: CalendarQuickstart.
     *
     * It will basically process the raw model received by filehelper, process it, and store the relevant information so
     * that it can immediately accessed through this resource when requested by other classes.
     *
     * The class retrieves the Response object received by the filehelper class, then stores only the surveys completed
     * array from this object, and returns the surveys array to the main program when requested.
     */
    private static Survey[] listOfSurveys;
    private static final String url = "https://api.typeform.com/forms/wJK7ar/responses";

    /**
     * This method intializes the survey resource class by through the initalize() method.
     */
    public SurveyResource() {
        SurveyResource.initialize();
    }

    /**
     * To initialize the survey resource, the class gets the Response object from the typeform API through
     * the File Helper class. After doing so, it stores the important information from this object: the survey array.
     */
    public static void initialize() {
        if (listOfSurveys == null) {
            try {
                Response current = FileHelper.getSurveysList(url);
                listOfSurveys= current.getItems();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * This method returns the surveys array which is the important information that the main java program will use.
     * @return array of Surveys completed, received through the Response Object from the FileHelper class.
     */
    public static Survey[] getListOfSurveys() {
        if (listOfSurveys == null) {
            return null;
        }
        return listOfSurveys;
    }

}