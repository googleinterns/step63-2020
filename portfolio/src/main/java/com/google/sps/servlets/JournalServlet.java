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

// Servlet supports journal feature by retrieving the entry, analyzing it, and storing it in datastore
@WebServlet("/journal")
public class JournalServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Query query = new Query("Entry").addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);

    List<String> comments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {

      comments.add(String.valueOf(entity.getProperty("average-score")));

    }

    String conversion = convertToJsonUsingGson(comments);
    response.setContentType("application/json");
    response.getWriter().println(conversion);

  }
  
  //Retrieves and stores journal entry from user

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
    
    // Get the input from the form.
    String input = request.getParameter("journal-input");

    //Get time of Entry Submisison
    long inputTime = System.currentTimeMillis();

    //Find total # of chars in entry
    int entrySize = input.length();

    //Divide entry into list of strings
    //TODO: make sure punctuation is included in List
    String[] entryAsStrings = input.split("\\n|\\.|\\?|\\!");
    List<String> entryBySentence = new ArrayList<String>(Arrays.asList(entryAsStrings)); 


    List<List> sentences = new ArrayList<>();
    
    //average score on a sentence by sentence basis
    float averageScore = 0;

    //weighted average based on sentence length
    float weightedAverage = 0;

    //Iterates over list of sentences and creates sentence object 
    for (int i= 0; i < entryBySentence.size(); i++) {
        List<String> sentence = new ArrayList<>();
        sentence.add(entryBySentence.get(i));

        // calculate sentiment analysis score
        Document doc =
            Document.newBuilder().setContent(sentence.get(0)).setType(Document.Type.PLAIN_TEXT).build();
        LanguageServiceClient languageService = LanguageServiceClient.create();
        Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
        float sentScore = sentiment.getScore();
        languageService.close();

        sentence.add(String.valueOf(sentScore));
        averageScore += Float.valueOf(sentence.get(1));
        weightedAverage += Float.valueOf(sentence.get(1))*(sentence.get(0).length()/entrySize);

        sentences.add(sentence);
    }

    averageScore /= (entryBySentence.size());


    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    String sentencesInJSON = convertToJsonUsingGsonforSentences(sentences);
    // Creates entry entity in data store and adds properties
    Entity entryEntity = new Entity("Entry");
    entryEntity.setProperty("content", sentencesInJSON);
    entryEntity.setProperty("timestamp", inputTime);
    entryEntity.setProperty("average-score",averageScore);
    entryEntity.setProperty("weighted-average", weightedAverage);

    datastore.put(entryEntity);

    // Redirect back to the HTML page.
    response.sendRedirect("/journal.html");

  }

  private String convertToJsonUsingGson(List<String> messages) {
    Gson gson = new Gson();
    String json = gson.toJson(messages);
    return json;
  }

    private String convertToJsonUsingGsonforSentences(List<List> messages) {
    Gson gson = new Gson();
    String json = gson.toJson(messages);
    return json;
  }

 
}