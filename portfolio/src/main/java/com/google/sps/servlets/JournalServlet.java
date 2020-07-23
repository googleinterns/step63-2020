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

import com.google.appengine.api.users.*;
import com.google.appengine.api.users.UserService;
import com.google.cloud.language.v1.AnalyzeEntitiesRequest;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.AnalyzeEntitySentimentRequest;
import com.google.cloud.language.v1.AnalyzeEntitySentimentResponse;
import com.google.cloud.language.v1.AnalyzeSentimentResponse;
import com.google.cloud.language.v1.AnalyzeSyntaxRequest;
import com.google.cloud.language.v1.AnalyzeSyntaxResponse;
import com.google.cloud.language.v1.ClassificationCategory;
import com.google.cloud.language.v1.ClassifyTextRequest;
import com.google.cloud.language.v1.ClassifyTextResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.EntityMention;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.cloud.language.v1.Token;
import java.util.List;
import java.util.Map;

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

    Query sentenceQuery = new Query("Sentence").addSort("time", SortDirection.DESCENDING);
    PreparedQuery sentenceResults = datastore.prepare(sentenceQuery);

    long submissionTime;

    
    if (sentenceResults.countEntities() != 0) {

    submissionTime = Long.valueOf(String.valueOf(sentenceResults.asIterable().iterator().next().getProperty("time")));
    } else {
    submissionTime = 0;
    }
    

    List<String> comments = new ArrayList<>();
    List<String> test = new ArrayList<>();
    String currentEmail = "";

    comments.add("NEW ENTRY");
    UserService service =  UserServiceFactory.getUserService();
        User user = service.getCurrentUser();
        if (service.isUserLoggedIn()) {
            currentEmail = user.getEmail(); 
        } else {
            currentEmail = "no email found";
        }
    

    for (Entity entity : sentenceResults.asIterable()) {
        
        if (submissionTime != Long.valueOf(String.valueOf(entity.getProperty("time")))){
            submissionTime = Long.valueOf(String.valueOf(entity.getProperty("time")));
            comments.add("NEW ENTRY");
        }
        
        if (String.valueOf(entity.getProperty("content")) != "null" | String.valueOf(entity.getProperty("sentiment-score")) != "null"){
        comments.add(String.valueOf(entity.getProperty("content")));
        comments.add(String.valueOf(entity.getProperty("sentiment-score")));
        }

        if (String.valueOf(entity.getProperty("average-score")) != "null") {
            test.add("The average score was :"+String.valueOf(entity.getProperty("average-score")));
            test.add(String.valueOf(entity.getProperty("average-score")));
        }
    


        if (String.valueOf(entity.getProperty("subjects")) != "null"){
            String [] subject = String.valueOf(entity.getProperty("subjects")).split(",");
            List<String> subjectSentence = new ArrayList<String>(Arrays.asList(subject));

            if (subjectSentence.size()==2){
                test.add("It seems like "+subjectSentence.get(0)+" was the most important thing in your last entry." );
            } else {
                test.add("It seems like "+subjectSentence.get(0)+" was the most important thing in your last entry. "+subjectSentence.get(3)+"  as well.");
            }

            test.add("Care to talk about that?");

    }

    }

    String conversion = convertToJsonUsingGsonforLists(test);
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
    
    //average score on a sentence by sentence basis
    float averageScore = 0;

    //weighted average based on sentence length
    float weightedAverage = 0;

    float charsIgnored = 0;

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    //Creates new Sentence Entity

    //Iterates over list of sentences and creates sentence object

    String email = "";

    UserService service =  UserServiceFactory.getUserService();
        User user = service.getCurrentUser();
        if (service.isUserLoggedIn()) {
            email = user.getEmail(); 
        } else {
            email = "no email found";
        }


    for (int i= 0; i < entryBySentence.size(); i++) {

        if (entryBySentence.get(i) != "/r") {

        Entity sentenceEntity = new Entity("Sentence");

        // Adds sentence string
        sentenceEntity.setProperty("content",entryBySentence.get(i));

        // calculate sentiment analysis score
        Document doc =
            Document.newBuilder().setContent(entryBySentence.get(i)).setType(Document.Type.PLAIN_TEXT).build();
        LanguageServiceClient languageService = LanguageServiceClient.create();
        Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
        float sentScore = sentiment.getScore();
        languageService.close();

        //Adds sentiment score for particular sentence
        sentenceEntity.setProperty("sentiment-score",sentScore);

        //Adds time
        sentenceEntity.setProperty("time",inputTime);

        //Adds email
        sentenceEntity.setProperty("email",email);

        //Stores sentence
        datastore.put(sentenceEntity);
        
        averageScore += sentScore ;
        weightedAverage += sentScore*((entryBySentence.get(i).length())/entrySize);
        } else {
            charsIgnored += 1;
        }
    }

    averageScore /= ((entryBySentence.size()-charsIgnored));

    Entity scoreEntity = new Entity("Sentence");

    scoreEntity.setProperty("average-score", averageScore);
    scoreEntity.setProperty("time", inputTime);
    scoreEntity.setProperty("email", email);

    datastore.put(scoreEntity);

    List<String> entityNameSalianceAndType = new ArrayList<>();

    try (LanguageServiceClient language = LanguageServiceClient.create()) {
        Document entityDoc = Document.newBuilder().setContent(input).setType(Type.PLAIN_TEXT).build();
        AnalyzeEntitiesRequest entityRequest =
            AnalyzeEntitiesRequest.newBuilder()
                .setDocument(entityDoc)
                .setEncodingType(EncodingType.UTF16)
                .build();

        AnalyzeEntitiesResponse EntityResponse = language.analyzeEntities(entityRequest);

        // Print the response
        for (com.google.cloud.language.v1.Entity entity : EntityResponse.getEntitiesList()) {
            entityNameSalianceAndType.add(entity.getName());
            entityNameSalianceAndType.add(String.valueOf(entity.getSalience()));
            //entityAnalysis.add("Metadata: ");
            /**for (Map.Entry<String, String> entry : entity.getMetadataMap().entrySet()) {
            entityKeyAndValue.add(entry.getKey()+" : "+entry.getValue());
            }*/
            for (EntityMention mention : entity.getMentionsList()) {
            //entityAnalysis.add("Begin offset: %d\n"+mention.getText().getBeginOffset());
            //entityContent.add("Content: %s\n"+mention.getText().getContent());
            entityNameSalianceAndType.add(String.valueOf(mention.getType()));
            }
        }
    }
    
    //subjectEntity
    Entity sentenceEntity = new Entity("Sentence");

    List mostImportant = new ArrayList();

    if (entityNameSalianceAndType.size()== 3 | entityNameSalianceAndType.size()== 4) {
        mostImportant.add(entityNameSalianceAndType.get(0));
        mostImportant.add(entityNameSalianceAndType.get(1));

    } else if (entityNameSalianceAndType.size() <3) {
        mostImportant.add("nothing");
        mostImportant.add("0.");
    }
    
    else{
        mostImportant.add(entityNameSalianceAndType.get(0));
        mostImportant.add(entityNameSalianceAndType.get(1));
        mostImportant.add(entityNameSalianceAndType.get(2));
        mostImportant.add(entityNameSalianceAndType.get(3));
    }
    String entityNamesInJSON = convertToJsonUsingGsonforLists(mostImportant);
    sentenceEntity.setProperty("subjects", entityNamesInJSON);
    sentenceEntity.setProperty("time", inputTime);
    sentenceEntity.setProperty("email",email);

    datastore.put(sentenceEntity);

 
    // Redirect back to the HTML page.
    response.sendRedirect("/journal.html");

  }

    private String convertToJsonUsingGsonforLists(List<String> messages) {
    Gson gson = new Gson();
    String json = gson.toJson(messages);
    return json;
  }

 
}