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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonObject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/nickname")
public class NickNameServlet extends HttpServlet {

  //adds the nickname to the datastore
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/nickname.html");
      return;
    }

    String nickname = request.getParameter("nickname");
    String id = userService.getCurrentUser().getUserId();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity entity = new Entity("UserInfo", id);
    entity.setProperty("id", id);
    entity.setProperty("nickname", nickname);
    // The put() function automatically inserts new data or updates existing data based on ID
    datastore.put(entity);

    response.sendRedirect("/index.html");
  }

  // /**
  //  * Returns the nickname of the user with id, or empty String if the user has not set a nickname.
  //  */
  // private String getUserNickname(String id) {
  //   DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  //   Query query =
  //       new Query("UserInfo")
  //           .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
  //   PreparedQuery results = datastore.prepare(query);
  //   Entity entity = results.asSingleEntity();
  //   if (entity == null) {
  //     return "";
  //   }
  //   String nickname = (String) entity.getProperty("nickname");
  //   return nickname;
  // }
}
