package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.users.User;
import com.google.gson.Gson;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.time.DayOfWeek;
import java.time.LocalDate; 

@WebServlet("/charts")
public class ChartServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html;");

    //Get the day of the week
    LocalDate localdate = LocalDate.now();
    DayOfWeek dotw = DayOfWeek.from(localdate);
    String currentDay = (String)dotw.name(); 
    response.getWriter().println((String)dotw.name());

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query("dayOfWeek");
    PreparedQuery results = datastore.prepare(query);

    if(results.countEntities() == 0){
        Entity dayOfWeek = new Entity("dayOfWeek");
        dayOfWeek.setProperty("dayOfWeek", currentDay);
        datastore.put(dayOfWeek);
    }

    //Gets stored date from datastore
    String storedDate ="";
    for(Entity entity:results.asIterable()){
        storedDate = (String)entity.getProperty("dayOfWeek");
    }

    //If the current day is the same as stored, continue. If not, update stored date
    if(!(currentDay.equals(storedDate))){

        //Deletes last weeks entries at the start of a new week
        if(currentDay.equals("MONDAY") && storedDate.equals("SUNDAY")){
            ArrayList<Key> pastInput = new ArrayList<>();
            Query newWeekquery = new Query("input");
            PreparedQuery newWeekresults = datastore.prepare(newWeekquery);
            Key pastKey = null;
            for(Entity entity:newWeekresults.asIterable()){
                pastKey = entity.getKey();
                pastInput.add(pastKey);
            }
            datastore.delete(pastInput);
        }

        //Changes the stored date to the current date
        Key storedDateKey = null;
        try{
            for(Entity entity:results.asIterable()){
                storedDateKey = entity.getKey();
            }
            Entity today = datastore.get(storedDateKey);
            today.setProperty("dayOfWeek", currentDay);
            datastore.delete(storedDateKey);
            datastore.put(today);
        }
        catch(EntityNotFoundException e){
            System.out.println("Entity not found");
        }

        
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query("dayOfWeek");
    PreparedQuery results = datastore.prepare(query);

    //Recieves stored date from datastore
    String storedDate ="";
    for(Entity entity:results.asIterable()){
        storedDate = (String)entity.getProperty("dayOfWeek");
    }

    Query replaceQuery = new Query("input");
    PreparedQuery replaceResults = datastore.prepare(replaceQuery);
    Gson g = new Gson();

    /*If the user enters a different value on the same day, delete the previous entry and replace
    with new.*/
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    String userProperty = "";
    String userEmail = user.getEmail();


    int entityCount = replaceResults.countEntities();
    if(entityCount!= 0){
        try{
            String whichQuestion ="";
            String propertyName = (String)request.getParameter("name");
            for(Entity entity:replaceResults.asIterable()){
                Key oldKey = entity.getKey();
                Entity oldEntity = datastore.get(oldKey);
                String data = (String)oldEntity.getProperty("input");
                String[] previousProperties = data.split(",");
                whichQuestion = previousProperties[2];
                userProperty =(String)entity.getProperty("User");

                //Does not replace if the entry is from a new day and doesn't delete the data of other users.
                if(whichQuestion.contains(propertyName) && previousProperties[0].contains(storedDate)
                    && userEmail.equals(userProperty)){

                    datastore.delete(oldKey);
                }

            }
        }
        catch(EntityNotFoundException e){}
    }

    

    //Adds day, value, and question to arraylist
    List<String> properties = new ArrayList<>();
    properties.add(storedDate);
    properties.add((String)request.getParameter("value"));
    properties.add((String)request.getParameter("name"));

    

    //Creates entity, sets the properties, adds to datastore
    Entity input = new Entity("input");
    input.setProperty("input", g.toJson(properties));
    input.setProperty("User", user.getEmail());
    datastore.put(input);

  }
}
