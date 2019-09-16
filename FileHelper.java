package com.mkyong;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import java.lang.StringBuilder;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class FileHelper {

    /**
     * This class retrieves a JSON file from the typefrom API, and uses an Object Mapper to model the information.
     * The Response Object models the information received by a GET request through the TypeForm API.
     * The Response object will also contain an array of Surveys completed, modeled by the class Survey.
     *
     * Note that the second method Forms, is included for possible optimization purposes but not currently used in code.
     * This method would return and model the JSON file that represents a typeform survey (without answers, just the survey).
     *
     * Currently the useful information received from the Forms method (question ids and the correct order) was hard
     * coded in the initialization method of the main java CalendarQuickstart class.
     */
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String token = "8x14zqQmyUVnCmsrV27ZbH6295JLwdCLdJhuRNEszc5P";


    /**
     * This method sends a GET request to the typeform API and models the received request (surveys completed and their
     * appropiate answers) as an Response Object.
     *
     * @throws FileNotFoundException if an error occurs in the GET request to the Typeform API
     * @throws IOException if an error occurs modeling the information from the GET request.
     *
     * @return Response Object that models all the information received by the GET request. Most importantly, the
     * response object returned contains a a Survey array, which contains the answers of the surveys completed and
     * is the main information that will be processed in the main java class: CalendarQuickstart.
     */
    public static Response getSurveysList(String urlString) throws FileNotFoundException, IOException {

        URL serverUrl = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) serverUrl.openConnection();
        urlConnection.setRequestMethod("GET");
        String encoded = new sun.misc.BASE64Encoder().encode(token.getBytes());
        //Response resp = given().header("Authorization", "Bearer " + token);


        urlConnection.addRequestProperty("Authorization", "Bearer " + token);
        //BufferedReader bufReader = new BufferedReader(new InputStreamReader(new URL(urlString).openStream()));

        BufferedReader bufReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

        StringBuilder strBuilder = new StringBuilder();
        String builderLine;
        while ((builderLine = bufReader.readLine()) != null) {
            strBuilder.append(builderLine);
        }

        String data = strBuilder.toString();
        if (!data.isEmpty()) {
            System.out.println("Data is not empty. ");
            return mapper.readValue(data, new TypeReference<Response>() {

            });
        } else {
            System.out.println("Get request or object mapper did not work. ");
        }
        return null;

    }

    /**
     * This method sends a GET request to the typeform API and models the received request (only the questions of the
     * survey, and their order, not any answers received nor surveys completed) as an Forms Object.
     *
     * @throws FileNotFoundException if an error occurs in the GET request to the Typeform API
     * @throws IOException if an error occurs modeling the information from the GET request.
     *
     * @return Forms Object that models all the information received by the GET request. This object models the basics
     * of the survey such as question order, question ids, etc but DOES NOT inlcude or model any answers or completed
     * surveys, just the survey itself.
     *
     * Note: for this method to work, a java Forms model would also have to be developed similar to the one in Survey class.
     * A Forms class would need to be created and subclasses accordingly to be able to model the JSON file
     * received by this get request.
     */
    /*
    public static Forms getForm(String urlString) throws FileNotFoundException, IOException {
        URL serverUrl = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) serverUrl.openConnection();
        urlConnection.setRequestMethod("GET");
        String encoded = new sun.misc.BASE64Encoder().encode(token.getBytes());
        //Response resp = given().header("Authorization", "Bearer " + token);


        urlConnection.addRequestProperty("Authorization", "Bearer " + token);
        //BufferedReader bufReader = new BufferedReader(new InputStreamReader(new URL(urlString).openStream()));

        BufferedReader bufReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

        StringBuilder strBuilder = new StringBuilder();
        String builderLine;
        while ((builderLine = bufReader.readLine()) != null) {
            strBuilder.append(builderLine);
            System.out.println(builderLine + " ");
            System.out.println();
        }

        String data = strBuilder.toString();
        if (!data.isEmpty()) {
            System.out.println("Data from forms is not empty. ");
            return mapper.readValue(data, new TypeReference<Forms>() {

            });
        } else {
            System.out.println("Get request from forms or object mapper (forms) did not work. ");
        }
        return null;
    }
    */
}