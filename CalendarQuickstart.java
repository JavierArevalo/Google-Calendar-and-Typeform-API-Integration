
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.calendar.model.EventDateTime;

import com.mkyong.SurveyResource;
import com.mkyong.Survey;
import com.mkyong.Answer;
import com.mkyong.Choices;
import com.mkyong.Field;
import com.mkyong.Choice;
import com.mkyong.Hidden;

import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;

import java.io.*;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.*;
import java.time.LocalDateTime;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;

public class CalendarQuickstart {

    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static HashMap<Long, String> dpiToCalendarMap;
    private static HashMap<String, Integer> questionIds;
    private static Date today;

    private static HashSet<String> surveysAlready;
    private static HashMap<Long, List<Event>> previousCal;

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);

    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = CalendarQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));


        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * Helper function that creates an google calendar event
     * @param startTime: the start time of the event created
     * @param endTime: the end time of the event created.
     * If both start time and endtime are equal to 0, then function will create an all day event.
     * @param date: string representation of the date (dd-mm-yyyy) when event will be created.
     * @param dpi: the dpi of the expert being evaluated. Will be used to add the event to the appropiate calendar.
     *
     * @throws IOException if an error with the google credentials occur.
     * @throws  GeneralSecurityException if the program does not have access to the google calendar account.
     */
    public static void createGoogleEvent(int startTime, int endTime, String date, Long dpi) throws IOException, GeneralSecurityException {

        boolean allDay = false;
        if (startTime == 0 && endTime == 0) {
            allDay = true;
        }

        //Google calendar API start up code
        //Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        Event event = new Event()
                .setSummary("Ocupado")
                .setDescription("Durante el tiempo de este evento el experto no puede trabajar. ");

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date1 = new Date();
        try {
            date1 = sdf.parse(date);
        } catch (Exception e) {
            System.out.println(e.getMessage() + " error in date parsing when creating google event. ");
        }

        SimpleDateFormat nsdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = nsdf.format(date1);
        if (allDay) {
            //if all day, then program will create 4 different events

            //Event 1 (using previously created event object):
            String dateTimeStr = dateString + "T06:00:00-07:00";
            String endDateTimeStr = dateString + "T10:00:00-07:00";

            DateTime startDateTime = new DateTime(dateTimeStr);
            EventDateTime start = new EventDateTime()
                    .setDateTime(startDateTime)
                    .setTimeZone("America/Guatemala");
            event.setStart(start);

            DateTime endDateTime = new DateTime(endDateTimeStr);
            EventDateTime end = new EventDateTime()
                    .setDateTime(endDateTime)
                    .setTimeZone("America/Guatemala");
            event.setEnd(end);

            String calendarId = "info@yavoy.co";
            String currentDestination = dpiToCalendarMap.get(dpi);

            if (existsEvent(dpi, 7, 11, date) == null) {
                event = service.events().insert(calendarId, event).execute();
                System.out.printf("Event created: %s with Id: %s\n", event.getHtmlLink(), event.getId());
                String id = event.getId();
                event = service.events().move(calendarId, id, currentDestination).execute();
                System.out.println(event.getUpdated());

            }

            //Event 2
            Event event2 = new Event()
                    .setSummary("Ocupado")
                    .setDescription("Durante el tiempo de este evento el experto no puede trabajar. ");

            String dateTimeStr2 = dateString + "T10:00:00-07:00";
            String endDateTimeStr2 = dateString + "T13:00:00-07:00";

            DateTime startDateTime2 = new DateTime(dateTimeStr2);
            EventDateTime start2 = new EventDateTime()
                    .setDateTime(startDateTime2)
                    .setTimeZone("America/Guatemala");
            event2.setStart(start2);

            DateTime endDateTime2 = new DateTime(endDateTimeStr2);
            EventDateTime end2 = new EventDateTime()
                    .setDateTime(endDateTime2)
                    .setTimeZone("America/Guatemala");
            event2.setEnd(end2);

            if (existsEvent(dpi, 11, 14, date) == null) {
                event2 = service.events().insert(calendarId, event2).execute();
                System.out.printf("Event created: %s with Id: %s\n", event2.getHtmlLink(), event2.getId());

                String id2 = event2.getId();

                event2 = service.events().move(calendarId, id2, currentDestination).execute();
                System.out.println(event2.getUpdated());
            }

            //Event 3
            Event event3 = new Event()
                    .setSummary("Ocupado")
                    .setDescription("Durante el tiempo de este evento el experto no puede trabajar. ");

            String dateTimeStr3 = dateString + "T13:00:00-07:00";
            String endDateTimeStr3 = dateString + "T16:00:00-07:00";

            DateTime startDateTime3 = new DateTime(dateTimeStr3);
            EventDateTime start3 = new EventDateTime()
                    .setDateTime(startDateTime3)
                    .setTimeZone("America/Guatemala");
            event3.setStart(start3);

            DateTime endDateTime3 = new DateTime(endDateTimeStr3);
            EventDateTime end3 = new EventDateTime()
                    .setDateTime(endDateTime3)
                    .setTimeZone("America/Guatemala");
            event3.setEnd(end3);

            if (existsEvent(dpi, 14, 17, date) == null) {
                event3 = service.events().insert(calendarId, event3).execute();
                System.out.printf("Event created: %s with Id: %s\n", event3.getHtmlLink(), event3.getId());

                String id3 = event3.getId();
                event3 = service.events().move(calendarId, id3, currentDestination).execute();
                System.out.println(event3.getUpdated());
            }

            //Event 4
            Event event4 = new Event()
                    .setSummary("Ocupado")
                    .setDescription("Durante el tiempo de este evento el experto no puede trabajar. ");

            String dateTimeStr4 = dateString + "T16:00:00-07:00";
            String endDateTimeStr4 = dateString + "T19:00:00-07:00";

            DateTime startDateTime4 = new DateTime(dateTimeStr4);
            EventDateTime start4 = new EventDateTime()
                    .setDateTime(startDateTime4)
                    .setTimeZone("America/Guatemala");
            event4.setStart(start4);

            DateTime endDateTime4 = new DateTime(endDateTimeStr4);
            EventDateTime end4 = new EventDateTime()
                    .setDateTime(endDateTime4)
                    .setTimeZone("America/Guatemala");
            event4.setEnd(end4);

            if (existsEvent(dpi, 17, 20, date) == null) {
                event4 = service.events().insert(calendarId, event4).execute();
                System.out.printf("Event created: %s with Id: %s\n", event4.getHtmlLink(), event4.getId());

                String id4 = event4.getId();
                event4 = service.events().move(calendarId, id4, currentDestination).execute();
                System.out.println(event4.getUpdated());
            }

        } else {
            String dateTimeStr;
            if (startTime == 7) {
                dateTimeStr = dateString + "T0" + (startTime - 1) + ":00:00-07:00";
            } else {
                dateTimeStr = dateString + "T" + (startTime - 1) + ":00:00-07:00";
            }

            DateTime startDateTime = new DateTime(dateTimeStr);
            EventDateTime start = new EventDateTime()
                    .setDateTime(startDateTime)
                    .setTimeZone("America/Guatemala");
            event.setStart(start);

            String dateTimeStrEnd = dateString + "T" + (endTime - 1) + ":00:00-07:00";
            DateTime endDateTime = new DateTime(dateTimeStrEnd);
            EventDateTime end = new EventDateTime()
                    .setDateTime(endDateTime)
                    .setTimeZone("America/Guatemala");
            event.setEnd(end);

            if (existsEvent(dpi, startTime, endTime, date) == null) {
                String calendarId = "info@yavoy.co";
                event = service.events().insert(calendarId, event).execute();
                System.out.printf("Event created: %s with Id: %s\n", event.getHtmlLink(), event.getId());

                String id = event.getId();
                String currentDestination = dpiToCalendarMap.get(dpi);
                event = service.events().move(calendarId, id, currentDestination).execute();
                System.out.println(event.getUpdated());
            }

        }
    }

    /**
     * Main code of the program. This is the code that gets run after entering "gradle run" in terminal.
     * This code is the logic that integrates the typeform API with the google calendar API to acheive the result
     * of creating a "busy" event each timeslot that the expert is unable to work, for each survey submitted.
     *
     * This code gets the list of surveys completed from the typeform API using helper classes.
     * Then it processes all the surveys that have not being processed yet using the helper functions within this class.
     * Processing a survey means creating a google calendar event (using the expert's specific calendar) when the
     * expert is not able to work, as expressed in his/her typeform response.
     */
    public static void main(String... args) throws IOException, GeneralSecurityException {

        initialize();
        System.out.println("Today: " + today.toString());

        SurveyResource resource = new SurveyResource();
        Survey[] listOfSurveys = resource.getListOfSurveys();
        System.out.println("Surveys completed: " + listOfSurveys.length);

        for (int i = 0; i < listOfSurveys.length; i++) {
            Survey currentSurvey = listOfSurveys[i];
            if (validSurvey(currentSurvey)) {
                String token = currentSurvey.getToken();
                if (!surveysAlready.contains(token)) {
                    Hidden hidden = currentSurvey.getHidden();
                    String date_after_tomorrow = hidden.getDate_thedayaftertomorrow();
                    if (validate2(date_after_tomorrow)) {
                        process(currentSurvey, hidden);
                    }
                    System.out.println("Token: " + token);
                    addToFile(token);
                }
                    System.out.println("Token: " + token);
                    System.out.println("HashSet size: " + surveysAlready.size());
                }

            }

        }

    /**
     * This function will be used to evaluate whether a Submitted Survey is complete or not.
     * If the survey being passed in has answers and hidden values, then it will be considered complete and processed.
     *
     * @param current: Survey being evaluated to see if it is complete or not
     * @return true if Survey passed in is complete: has answers and has hidden variables
     * @return false otherwise.
     */
    public static boolean validSurvey(Survey current) {
        if (current.getAnswers() == null) {
            return false;
        }
        if (current.getHidden() == null) {
            return false;
        }
        //Answer[] answers = current.getAnswers();
        //if (answers.getChoice() == null && answers.getChoices() == null) {
            //return false;
        //}
        return true;
    }

    /**
     * This function will also be used to evaluate more carefully whether a Submitted Survey is relevant or not.
     * The Survey will be considered relevant if it has information relevant today or afterwards.
     * To evaluate this, we will compare the appropiate date that represents the Survey with today's date.
     *
     * @param date: string representation of date that will be used to see whether a completed Survey is relevant or not.
     * @return false if date passed in is before the date of today (date when program is being ran)
     * @return true otherwise
     */
    public static boolean validate2(String date) {
        System.out.println("Today's date: " + today + " and current date being evaluated: " + date);
        if (today == null) {
            System.out.println();
            System.out.println("Initialization went wrong, today is null. ");
            System.out.println();
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date currentDate;
        try {
            currentDate = sdf.parse(date);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return false;
        }
        currentDate.setHours(21);
        if (currentDate.compareTo(today) < 0) {
            return false;
        }
        return true;
    }

    /**
     * This helper function is what allows the program to only process each Survey once, in repeated calls.
     * Each Survey has a unique identifier called a token.
     * Thus, once a survey is processed, its unique token will be added to a file that keeps record of
     * the tokens of the surveys already processed.
     * Then, when the program is run, it will only process the survey if its not already in the file.
     * And if that is the case, after processing the survey, the token will be added to the file through this function.
     *
     * This system is what prevents the program from redundantly processing surveys in repetead calls.
     *
     * @param token the unique string identifier of the survey that will be added to the file of tokens.
     */
    public static void addToFile(String token) {
        String fileName = "surveysAlreadyEvaluated.txt";

        try {
            File file = new File(fileName);
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            br.write(token);
            br.newLine();

            br.close();
            fr.close();

        }
        catch(IOException ex) {
            System.out.println("Error writing to file: " + fileName);

        }
    }

    /**
     * This helper function sorts the answers array passed in.
     * The function uses the question id of each answer to figure where the correct position of each answer.
     * The TypeForm results API returns the answers in a random order (eg: Q3, Q2, Q4, ...) so this function is used to
     * pass the answers from an unorganized array to a organized arrayList (Q1, Q2, Q3, ...) using their question id to
     * know the correct place of each answer.
     *
     * @param answers: the array of answers that will be sorted
     * @return ordered arraylist of answer objects.
     */
    public static ArrayList<Answer> sort(Answer[] answers) {
        if (questionIds == null) {
            System.out.println();
            System.out.println("Initialization went wrong, questionIds HashMap is null. ");
            System.out.println();
            return null;
        }

        Answer[] modified = new Answer[6];
        for (int i = 0; i < answers.length; i++) {
            Answer currentAnswer = answers[i];
            Field field = currentAnswer.getField();
            String id = field.getId();
            int shouldBe = questionIds.get(id) - 1;
            modified[shouldBe] = currentAnswer;
        }
        ArrayList<Answer> fin = new ArrayList<>();
        for (int i = 0; i < modified.length; i++) {
            Answer current = modified[i];
            if (current != null) {
                fin.add(current);
            }
        }
        return fin;
    }

    /**
     * This method processes the answer of the passed in survey. It also uses the passed in hidden variables
     * to determine to which calendar to add the events (using the dpi hidden variable).
     * 
     */
    public static void process(Survey current, Hidden hidden) throws IOException, GeneralSecurityException {

        Answer[] befor = current.getAnswers();
        ArrayList<Answer> answers = sort(befor);

        Long dpi = hidden.getDpi();

        //there are two types of questions: yes or no, and horarios depending on previous one
        boolean yesOrNo = true;
        int index = 0;

        //count will be of "yes or no" questions to see if I use date today, date tomorrow, or date after tomorrow for immeidate validator
        int count = 1;
        String answerDate = hidden.getDate_today();
        while (index < answers.size()) {

            if (yesOrNo) {
                if (count == 1) {
                    answerDate = hidden.getDate_today();
                } else if (count == 2) {
                    answerDate = hidden.getDate_tomorrow();
                } else if (count == 3) {
                    answerDate = hidden.getDate_thedayaftertomorrow();
                }
                count++;

                boolean validate = validate2(answerDate);
                System.out.println("Validate 2: " + validate);
                boolean yes = false;
                Answer currentAnswer = answers.get(index);
                Choice choice = currentAnswer.getChoice();
                //should not be null can check later for this edge case
                String answerItself = choice.getLabel();
                if (answerItself.equals("Si")) {
                    yes = true;
                }
                //Do following step just to check
                System.out.println("Boolean: " + yes + " answer itself: " + answerItself);

                if (validate && yes) {
                    yesOrNo = false;
                    index++;
                } else if (validate) {
                    //validate and no
                    yesOrNo = true;
                    index++;

                    //create all day event using correct date
                    createGoogleEvent(0, 0, answerDate, dpi);
                } else if (yes) {
                    //not valid and yes
                    index+=2;
                    yesOrNo = true;
                } else {
                    //not valid and no
                    index++;
                    yesOrNo = true;
                }

            } else {
                //Following code creates an array of length 4 that represents when the expert is able to work.
                //array[0] = 1 means that expert is able to work at first time slot = "Manana (7AM - 11AM)"
                //array[1] = 0 means that expert is not able to work during second time slot = "Mediodia (11AM - 2PM)"
                //and so on

                Answer currentAnswer = answers.get(index);
                Choices choices = currentAnswer.getChoices();
                //labels are all the times that the expert selected that he is available to work.
                String[] labels = choices.getLabels();
                int[] array = new int[4];
                for (int i = 0; i < labels.length; i++) {
                    String currentStr = labels[i];
                    if (currentStr.equals("Mañana (7AM - 11AM)")) {
                        array[0] = 1;
                    } else if (currentStr.equals("Mediodía (11AM - 2PM)")) {
                        array[1] = 1;
                    } else if (currentStr.equals("Tarde ( 2PM - 5PM)")) {
                        array[2] = 1;
                    } else if (currentStr.equals("Noche (5PM - 8PM)")) {
                        array[3] = 1;
                    } else {
                        System.out.println("Some of the statements are wrong, did not qualify as either. ");
                    }
                }

                //Key: only create event if expert is busy, thus if value in time array = 0.
                for (int i = 0; i < array.length; i++) {
                    int startTime;
                    int endTime;
                    if (i == 0) {
                        startTime = 7;
                        endTime = 11;
                    } else if (i == 1) {
                        startTime = 11;
                        endTime = 14;
                    } else if (i == 2) {
                        startTime = 14;
                        endTime = 17;
                    } else {
                        startTime = 17;
                        endTime = 20;
                    }

                    if (array[i] == 0) {
                        //If he is busy then just create event in the case that no busy event already exists
                        if (existsEvent(dpi, startTime, endTime, answerDate) == null) {
                            createGoogleEvent(startTime, endTime, answerDate, dpi);
                        }
                    } else {
                        //if he is free then check if there is no other event. If I do find a event, delete event
                        String otherEvent = existsEvent(dpi, startTime, endTime, answerDate);
                        System.out.println();
                        System.out.println("Other event: " + otherEvent);
                        System.out.println();
                        if (otherEvent != null) {
                            //this means that there exists another event during the specified time that I need to delete
                            String calendarId = dpiToCalendarMap.get(dpi);
                            deleteGoogleEvent(otherEvent, calendarId);
                        }
                    }
                }
                yesOrNo = true;
                index++;
            }
        }
    }

    public static void deleteGoogleEvent(String eventId, String calendarId) throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        service.events().delete(calendarId, eventId).execute();
    }


    /**
     * This helper function sorts the answers array passed in.
     * The function uses the question id of each answer to figure where the correct position of each answer.
     * The TypeForm results API returns the answers in a random order (eg: Q3, Q2, Q4, ...) so this function is used to
     * pass the answers from an unorganized array to a organized arrayList (Q1, Q2, Q3, ...) using their question id to
     * know the correct place of each answer.
     *
     * @param answers: the array of answers that will be sorted
     * @return ordered arraylist of answer objects.
     */
    public static void initialize() throws IOException, GeneralSecurityException {
        questionIds = new HashMap<>();
        questionIds.put("P3L6CjDljKpT", 1);
        questionIds.put("pjSzo6SSLnmN", 2);
        questionIds.put("CjQdUHutL7JG", 3);
        questionIds.put("OPymzrxjachx", 4);
        questionIds.put("KFgURtCD57vj", 5);
        questionIds.put("I3l3WFTsEAmp", 6);

        dpiToCalendarMap = new HashMap<>();
        dpiToCalendarMap.put(1609103660092L, "yavoy.co_r5ld7lgaf1kovlesu9bbsc5ul8@group.calendar.google.com");
        dpiToCalendarMap.put(1721204970101L, "yavoy.co_1ir1mojct4mndmmfil8mu58rgg@group.calendar.google.com");
        dpiToCalendarMap.put(2077937510402L, "yavoy.co_6rid3t1dlqgfuplbesu46a21pc@group.calendar.google.com");
        dpiToCalendarMap.put(2336457050101L, "yavoy.co_3ldphlini285a8dipf7gejgaug@group.calendar.google.com");
        dpiToCalendarMap.put(2498564760101L, "yavoy.co_1i9r1ec6egv5u01r5cd9filn4g@group.calendar.google.com");
        dpiToCalendarMap.put(2635743850917L, "yavoy.co_lgjqlbq8jrgjmitgukf04het2k@group.calendar.google.com");
        dpiToCalendarMap.put(1681569700103L, "yavoy.co_0947or9gcd0k35pifi50p8ikfs@group.calendar.google.com");
        dpiToCalendarMap.put(2676091400410L, "yavoy.co_cgoslr03t6a90k5jss3hjvsplk@group.calendar.google.com");

        ZoneId zoneId = ZoneId.of("America/Guatemala");
        LocalDateTime now = LocalDateTime.now();
        today = java.sql.Timestamp.valueOf(now);

         surveysAlready = new HashSet<>();

         previousCal = new HashMap<>();

        String fileName = "surveysAlreadyEvaluated.txt";

        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                surveysAlready.add(line);
            }

            // Always close files.
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file " + fileName);
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file" + fileName);

        }

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        for (Map.Entry<Long, String> item : dpiToCalendarMap.entrySet()) {
            Long key = item.getKey();
            String calendarId = item.getValue();
            List<Event> current = new ArrayList<Event>();
            if (previousCal.containsKey(key)) {
                current = previousCal.get(key);
            }
            String pageToken = null;
            do {
                Date upper2 = today;
                upper2.setHours(5);
                DateTime upper = new DateTime(upper2);
                LocalDateTime lower2 = now.plusDays(4);
                Date lower3 = java.sql.Timestamp.valueOf(lower2);
                DateTime lower = new DateTime(lower3);

                Events events = service.events().list(calendarId).setPageToken(pageToken)
                .setTimeMin(upper)
                .setTimeMax(lower)
                        .execute();

                List<Event> items = events.getItems();
                for (Event event: items) {
                    current.add(event);
                    System.out.println("DPI: " + key + " and event: " + event.getSummary() + " event id: " + event.getId());
                }
                pageToken = events.getNextPageToken();
            } while (pageToken != null);

            previousCal.put(key, current);

        }

        LocalDateTime now2 = LocalDateTime.now();
        today = java.sql.Timestamp.valueOf(now2);
        //This creates a hashmap<Long, List<Event>> and every expert has his or her own entry.
        //Each entry in this hashmap contains a list of all the events for each expert on the last 3 days.

    }

    //Returns event id if it exists or null if it doesnt
    public static String existsEvent(Long dpi, int startTime, int endTime, String date) {

        List<Event> list = previousCal.get(dpi);

        //This will check if there is an event in a specific expert's calendar for a specific time block
        for (Event event: list) {
            DateTime start = event.getStart().getDateTime();
            DateTime end = event.getEnd().getDateTime();

            String dateOfEvent2 = start.toString().substring(0, 10);
            String dateOfEvent = flip(dateOfEvent2);
            System.out.println(dateOfEvent);
            System.out.println("For string comparison: parameter date: " + date + ", and date of event: " + dateOfEvent);
            if (dateOfEvent.equals(date)) {
                String dateTimeStrStart = start.toString();
                int startHourOfEvent = getHour(dateTimeStrStart);
                String dateTimeStrEnd = end.toString();
                int endHourOfEvent = getHour(dateTimeStrEnd);
                System.out.println("Event: " + event.getSummary() + " start time: " + startHourOfEvent + " and end time: " + endHourOfEvent);
                if ((startHourOfEvent <= startTime) && (endHourOfEvent >= endTime)) {
                    System.out.println("Event summary: " + event.getSummary());
                    if (event.getSummary().equals("Ocupado")) {
                        return event.getId();
                    }
                }
            }
        }
        return null;
    }

    private static String flip(String date) {
        //assumming string is in yyyy-mm-dd
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1;
        try {
            date1 = sdf.parse(date);
        } catch (Exception e) {
            System.out.println("Error in parsing date: " + e.getMessage());
            return null;
        }
        String r = "";
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
        try {
            r = sdf2.format(date1);
        } catch (Exception e) {
            System.out.println("Error in parsing date: " + e.getMessage());
            return null;
        }
        return r;
    }

    private static int getHour(String dateTime) {
        if (dateTime.length() == 0) {
            return 0;
        } else if (dateTime == null) {
            return 0;
        }
        int index = 0;
        for (int i = 0; i < dateTime.length(); i++) {
            if (dateTime.charAt(i) == 'T') {
                index = i;
            }
        }
        System.out.println("Just making sure index is not 0, index: " + index);
        String hour = dateTime.substring(index + 1, index + 3);
        return Integer.parseInt(hour);
    }


}
