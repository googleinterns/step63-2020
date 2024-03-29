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
      
    List<String> comments = new ArrayList<>();
    
    
    if (setEmail().substring(setEmail().length()-10,setEmail().length()).equals("google.com") | setEmail().equals("chimnchuk@gmail.com") | setEmail().equals("erysd.school@gmail.com") | setEmail().equals("ericas12145@gmail.com")) {
    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Query sentenceQuery = new Query("Sentence").addSort("server-time", SortDirection.DESCENDING);
    PreparedQuery sentenceResults = datastore.prepare(sentenceQuery);

    long submissionTime;

    
    if (sentenceResults.countEntities() != 0) {

    submissionTime = Long.valueOf(String.valueOf(sentenceResults.asIterable().iterator().next().getProperty("time")));
    } else {
    submissionTime = 0;
    }
    
    List<String> test = new ArrayList<>();
    String currentEmail = setEmail();

    comments.add("NEW ENTRY");
    /**
    UserService service =  UserServiceFactory.getUserService();
        User user = service.getCurrentUser();
        if (service.isUserLoggedIn()) {
            currentEmail = user.getEmail(); 
        } else {
            currentEmail = "no email found";
        }
    **/
    comments.add(currentEmail);
    

    for (Entity entity : sentenceResults.asIterable()) {

        if ((entity.getProperty("email") != "null") && (String.valueOf(entity.getProperty("email")).equals(currentEmail))) {
        
        if (submissionTime != Long.valueOf(String.valueOf(entity.getProperty("time")))){
            submissionTime = Long.valueOf(String.valueOf(entity.getProperty("time")));
            comments.add("NEW ENTRY");
        }
        
        if (String.valueOf(entity.getProperty("content")) != "null" | String.valueOf(entity.getProperty("sentiment-score")) != "null"){
        comments.add(String.valueOf(entity.getProperty("sentiment-score")));
        comments.add(String.valueOf(entity.getProperty("content")));
        }

        if (String.valueOf(entity.getProperty("average-score")) != "null") {
            comments.add(String.valueOf(entity.getProperty("average-score")));
            comments.add("The average score was :"+String.valueOf(entity.getProperty("average-score")));
        }

        if (String.valueOf(entity.getProperty("weighted-average")) != "null") {
            comments.add(String.valueOf(entity.getProperty("weighted-average")));
            comments.add("The weighted average was :"+String.valueOf(entity.getProperty("weighted-average")));
        }
    


        if (String.valueOf(entity.getProperty("subjects")) != "null"){
            String [] subject = String.valueOf(entity.getProperty("subjects")).split(",");
            List<String> subjectSentence = new ArrayList<String>(Arrays.asList(subject));

            if (subjectSentence.size()==2){
                comments.add("It seems like "+subjectSentence.get(0)+" was the most important thing in your last entry." );
            } else {
                comments.add("It seems like "+subjectSentence.get(0)+" was the most important thing in your last entry. "+subjectSentence.get(2)+"  as well.");
            }

            comments.add("Care to talk about that?");

         }

    }
    }
    
    } else {
        comments.add("Because product is still in its developemental stage, access is limited to Corporate Google Accounts");
    }

    String conversion = convertToJsonUsingGsonforLists(comments);
    response.setContentType("application/json");
    response.getWriter().println(conversion);

  }
  
  //Retrieves and stores journal entry from user

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
   if (setEmail().substring(setEmail().length()-10,setEmail().length()).equals("google.com") | setEmail().equals("chimnchuk@gmail.com") | setEmail().equals("erysd.school@gmail.com") | setEmail().equals("ericas12145@gmail.com")){  
    
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

    //Iterates over list of sentences and creates sentence object

    String email = setEmail();
    
    List<String> wtAvg = new ArrayList<>();

    for (int i= 0; i < entryBySentence.size(); i++) {

        if (entryBySentence.get(i) != "/r") {

        datastore.put(createSentenceEntity(inputTime,entryBySentence.get(i)));
        
        averageScore += getSentimentScore(entryBySentence.get(i)) ;

        weightedAverage += getSentimentScore(entryBySentence.get(i))*((float)(entryBySentence.get(i).length())/entrySize);

        } else {
            charsIgnored += 1;
        }
    }

    averageScore /= ((entryBySentence.size()-charsIgnored));


    Entity scoreEntity = new Entity("Sentence");
    scoreEntity.setProperty("average-score", averageScore);
    scoreEntity.setProperty("time", inputTime);
    scoreEntity.setProperty("server-time",System.currentTimeMillis());
    scoreEntity.setProperty("email", email);

    datastore.put(scoreEntity);

    Entity weightedScoreEntity = new Entity("Sentence");
    weightedScoreEntity.setProperty("weighted-average", weightedAverage);
    weightedScoreEntity.setProperty("time", inputTime);
    weightedScoreEntity.setProperty("server-time",System.currentTimeMillis());
    weightedScoreEntity.setProperty("email", email);

    datastore.put(weightedScoreEntity);
    
    //subjectEntity
    Entity sentenceEntity = new Entity("Sentence");
    
    String entityNamesInJSON = convertToJsonUsingGsonforLists(getSubjects(input));
    sentenceEntity.setProperty("subjects", entityNamesInJSON);
    sentenceEntity.setProperty("time", inputTime);
    sentenceEntity.setProperty("server-time",System.currentTimeMillis());
    sentenceEntity.setProperty("email",email);
    

    datastore.put(sentenceEntity);

 
    // Redirect back to the HTML page.
    response.sendRedirect("/journal.html");

    
    } else {
    response.sendRedirect("/index.html");
    }


  }

    private String convertToJsonUsingGsonforLists(List<String> messages) {
        Gson gson = new Gson();
        String json = gson.toJson(messages);
        return json;
  }

    public String setEmail() {
        String email = "";
        UserService service =  UserServiceFactory.getUserService();
        User user = service.getCurrentUser();
        if (service.isUserLoggedIn()) {
            email = user.getEmail(); 
        } else {
            email = "no email found";
        }

        return email;
  }
  private List<String> getEntityAnalysis(String input){
    
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
            //for (Map.Entry<String, String> entry : entity.getMetadataMap().entrySet()) {
            //entityKeyAndValue.add(entry.getKey()+" : "+entry.getValue());
            //}
        for (EntityMention mention : entity.getMentionsList()) {
            //entityAnalysis.add("Begin offset: %d\n"+mention.getText().getBeginOffset());
            //entityContent.add("Content: %s\n"+mention.getText().getContent());
            entityNameSalianceAndType.add(String.valueOf(mention.getType()));
            }
        }

    } catch (IOException e) {

    }
    return entityNameSalianceAndType;
  }

  
  private List<String> getSubjects(String inputString){
    
    List<String> entityNameSalianceAndType = getEntityAnalysis(inputString);

    List<String> mostImportant = new ArrayList();

    if (entityNameSalianceAndType.size() <3) {
        mostImportant.add("nothing");
        mostImportant.add("0.");
    }
    
    else if ((entityNameSalianceAndType.size())%3 == 1) {
        mostImportant.add(entityNameSalianceAndType.get(0));
    }
    else {
        mostImportant.add(entityNameSalianceAndType.get(0));
        mostImportant.add(entityNameSalianceAndType.get(1));
        mostImportant.add(entityNameSalianceAndType.get(2));
    }

    return mostImportant;


  }

  public float getSentimentScore(String input) {
      try {
      Document doc =
            Document.newBuilder().setContent(input).setType(Document.Type.PLAIN_TEXT).build();
        LanguageServiceClient languageService = LanguageServiceClient.create();
        Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
        float sentScore = sentiment.getScore();
        languageService.close();

        return sentScore;
      } catch (IOException e){
          return 0;
      }
  }

  public Entity createSentenceEntity(long inputTime, String sentence){

        //Instantiates Sentence Entity
        Entity sentenceEntity = new Entity("Sentence");

        sentenceEntity.setProperty("content",sentence);

        //Adds sentiment score for particular sentence
        sentenceEntity.setProperty("sentiment-score",getSentimentScore(sentence));

        //Adds time
        sentenceEntity.setProperty("time",inputTime);

        //Adds processing time
        sentenceEntity.setProperty("server-time",System.currentTimeMillis());

        //Adds email
        sentenceEntity.setProperty("email",setEmail());

        return sentenceEntity;

  }
  


 
}