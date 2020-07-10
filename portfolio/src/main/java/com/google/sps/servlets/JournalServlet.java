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

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Entity;
import com.google.cloud.language.v1.Sentiment;
import com.google.cloud.language.v1.Document;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Arrays;
import java.io.IOException;
import com.google.sps.Sentence;
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.Query;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.appengine.api.datastore.Query.SortDirection;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/journal")
public class JournalServlet extends HttpServlet {
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
    System.out.println("The journal input is "+request.getParameter("journal-input"));
    // Get the input from the form.
    String input = request.getParameter("journal-input");
    long inputTime = System.currentTimeMillis();
    int entrySize = input.length();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    String[] entryAsStrings = input.split(";|\\.|\\?|\\!");
    System.out.println("AS STRINGS "+entryAsStrings);
    List<String> entryBySentence = new ArrayList<String>(Arrays.asList(entryAsStrings)); 
    System.out.println("AS STRINGS IN LIST "+entryBySentence);

    Collection sentences = new HashSet();
    
    int i = 0;
    int avg = 0;
    int wtAvg = 0;

    System.out.println(entryBySentence.getClass().getName());
    System.out.println(entryBySentence.get(i));

    while (i<entryBySentence.size()) {
        Sentence sentence = new Sentence(entryBySentence.get(i));
        System.out.println(sentence.text());
        System.out.println(sentence.score());
        sentences.add(sentence);
        i++;
        avg += sentence.score();
        wtAvg += sentence.score()*(sentence.text().length()/entrySize);
    }

    System.out.println("THE SENTENCES "+sentences);
    System.out.println("WT AVG  "+wtAvg);
     

    Entity entryEntity = new Entity("Entry");
    entryEntity.setProperty("content", sentences);
    entryEntity.setProperty("timestamp", inputTime);
    entryEntity.setProperty("average-score",avg/(entryBySentence.size()));

    datastore.put(entryEntity);

    Query query = new Query("Entry").addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);


    // Redirect back to the HTML page.

    response.sendRedirect("/journal.html");

  }

    private String convertToJsonUsingGson(List<String> messages) {
    Gson gson = new Gson();
    String json = gson.toJson(messages);
    return json;
  }

 
}