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
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    LocalDate localDate = LocalDate.now();
    String currentDate = localDate.toString();


    Query replaceQuery = new Query("input");
    PreparedQuery replaceResults = datastore.prepare(replaceQuery);
    Gson g = new Gson();

    /*If the user enters a different value on the same day, delete the previous entry and replace
    with new.*/
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    String userProperty = "";
    String userNickname = user.getNickname();


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
                if(whichQuestion.contains(propertyName) && previousProperties[0].contains(currentDate)
                    && userNickname.equals(userProperty)){

                    datastore.delete(oldKey);
                }

            }
        }
        catch(EntityNotFoundException e){}
    }

    

    //Adds day, value, and question to arraylist
    List<String> properties = new ArrayList<>();
    properties.add(currentDate);
    properties.add((String)request.getParameter("value"));
    properties.add((String)request.getParameter("name"));

    

    //Creates entity, sets the properties, adds to datastore
    Entity input = new Entity("input");
    input.setProperty("input", g.toJson(properties));
    input.setProperty("User", userNickname);
    datastore.put(input);

  }
}
