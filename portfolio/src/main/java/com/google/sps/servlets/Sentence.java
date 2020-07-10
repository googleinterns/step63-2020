
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

  public Sentence(String text){
    this.text = text;
    this.score = getScore(text);

  }

  public float getScore(String text){

    try {
    Document doc =
        Document.newBuilder().setContent(text).setType(Document.Type.PLAIN_TEXT).build();
    LanguageServiceClient languageService = LanguageServiceClient.create();
    Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    float sentScore = sentiment.getScore();
    languageService.close();

    return sentScore;
    } catch(IOException e) {

  return -1;
}
  
  }
  public float score() {
    return getScore(text);
  }

  public String text() {
    return text;
  }

}
