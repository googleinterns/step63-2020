// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    JsonObject person = new JsonObject();
    Boolean status;
    UserService userService = UserServiceFactory.getUserService();

    //limit emails to just @google.com. logout them out and redirect to homepage, 
    //make a string and check

    // If user is not logged in, show a login form (could also redirect to a login page
    if (!userService.isUserLoggedIn()) {
      String loginUrl = userService.createLoginURL("/nickname.html");
      status = false;
      person.addProperty("status", status);
      person.addProperty("url", loginUrl);

      String statusJson = new Gson().toJson(person);
      response.getWriter().println(statusJson);

      System.out.println(statusJson);

      return;
    }

    String email = userService.getCurrentUser().getEmail();
    String end = email.substring(email.length() - 11);
    String logoutUrl = userService.createLogoutURL("/");
    System.out.println(end);

    if(!end.equals("@google.com")){
      response.sendRedirect(logoutUrl);
      return;
    }

    // If user has not set a nickname, redirect to nickname page
    String nickname = getUserNickname(userService.getCurrentUser().getUserId());
    if (nickname == null) {
      response.sendRedirect("/nickname.html");
      return;
    }

    // User is logged in and has a nickname, so the request can proceed   
    // String logoutUrl = userService.createLogoutURL("/");
    status = true;
    
    person.addProperty("url", logoutUrl);
    person.addProperty("status", status);
    person.addProperty("name", nickname);

    response.setContentType("application/json;");
    String statusJson = new Gson().toJson(person);
    response.getWriter().println(statusJson);

    System.out.println(statusJson);
  }

  /** Returns the nickname of the user with id, or null if the user has not set a nickname. */
  private String getUserNickname(String id) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query =
        new Query("UserInfo")
            .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    if (entity == null) {
      return null;
    }
    String nickname = (String) entity.getProperty("nickname");
    return nickname;
  }
}

