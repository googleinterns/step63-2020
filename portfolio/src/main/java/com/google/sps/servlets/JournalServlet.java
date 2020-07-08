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
  
  /***
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Query query = new Query("Entry").addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);

    int maxComments = 10;

    try {
    String getMax = request.getParameter("max");
    maxComments = Integer.parseInt(getMax);
    } 
    catch (NumberFormatException e) {

    }

    List<String> comments = new ArrayList<>();
    for (Entity entity : results.asList()) {

      String comment = (String) entity.getProperty("content");
      String commentWithScore = comment+"  "+String.valueOf(entity.getProperty("score"));

      comments.add(commentWithScore);

    }

    String conversion = convertToJsonUsingGson(comments);
    response.setContentType("application/json");
    response.getWriter().println(conversion);

  }
  **/
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{

    // Get the input from the form.
    String input = request.getParameter("journal-input");
    long inputTime = System.currentTimeMillis();
    int entrySize = input.length();

   /***
    Document doc =
        Document.newBuilder().setContent(input).setType(Document.Type.PLAIN_TEXT).build();
    LanguageServiceClient languageService = LanguageServiceClient.create();
    Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    float score = sentiment.getScore();
    languageService.close();
   **/

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    ArrayList<String> entryBySentence = input.split(;|\\.|\\?|!);

    Collection sentences = new HashSet();
    
    int i = 0;
    int avg = 0;
    int wtAvg = 0;

    while (i<entryBySentence.size()) {
        Sentence sentence = Sentence(entryBySentence[i]);
        sentences.add(sentence);
        i++;
        avg += sentence.score();
        wtAvg += sentence.score()*(sentence.text.length()/entrySize)
    }

     

    Entity entryEntity = new Entity("Entry");
    entryEntity.setProperty("content", sentences);
    entryEntity.setProperty("timestamp", inputTime);
    entryEntity.setProperty("average-score",avg/(entryBySentence.size()));

    datastore.put(entryEntity);

    // Redirect back to the HTML page.

    response.sendRedirect("/index.html");
  }

    private String convertToJsonUsingGson(List<String> messages) {
    Gson gson = new Gson();
    String json = gson.toJson(messages);
    return json;
  }

 
}