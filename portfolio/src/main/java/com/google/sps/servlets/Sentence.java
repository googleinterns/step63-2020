
package com.google.sps;

import java.util.Comparator;

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
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.Query;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.appengine.api.datastore.Query.SortDirection;


public final class Sentence {

  private final String text;
  private final float score;

  public Sentence(String input){
    this.text = input;
    System.out.println("The text is "+text);
    this.score = calculateScore(text);
    System.out.println("THE SCORE IS "+score);

  }

  private static float calculateScore(String inputText){
    
    //Calculates sentiment analysis score

    try {
    Document doc =
        Document.newBuilder().setContent(inputText).setType(Document.Type.PLAIN_TEXT).build();

    LanguageServiceClient languageService = LanguageServiceClient.create();
    //This is the problem line
    Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    float sentScore = sentiment.getScore();
    languageService.close();

    return sentScore;
    } catch(IOException e) {
    //If the IOException is not added, when the code is run there will be a java IOException "must be caught" error
    //also I get an error if if there's no return statement

    return -1;
  }

  }


  public String text() {
    return text;
  }

  public float score() {
  return score;
  }

}
