
package com.google.sps;

import java.util.List;
import com.google.sps.servlets.JournalServlet;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.ArrayList;

import org.mockito.Mockito;

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
public final class JournalServletTest extends HttpServlet {
    JournalServlet JournalServlet = new JournalServlet();
  /*
  @Test
  public void testEmail() {

    Assert.assertEquals("chiamaka@google.com", JournalServlet.setEmail());
  }
  */
  

  //Can't test this either, only have "end user credentials"

/**
  @Test
  public void submissionAndProcessingTimeDifferent(){
      Entity sentenceEntity = JournalServlet.createSentenceEntity(System.currentTimeMillis(),"string");
      String clientTime = String.valueOf(sentenceEntity.getProperty("time"));
      String serverTime = String.valueOf(sentenceEntity.getProperty("server-time"));

      Boolean expected = false;
      Boolean actual = clientTime.equals(serverTime);
    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!! expected: "+expected+" actual: "+actual);
     Assert.assertEquals(expected,actual);

    
  }
  */

  //Can't test, only have "end user credentials"

  @Test
  public void correctSentimentReturn(){
      float expected = 0;

    //Assert.assertEquals(expected, JournalServlet.getSentimentScore("testing"));
  }
  @Test
  public void everyEntityHasSalienceScore() {
    
    List<String> test = new ArrayList();
    test.add("string");
    test.add("0.0");
    test.add("string");
    test.add("string");

    List<String> expected = new ArrayList();
    expected.add("string");

    System.out.println(String.valueOf(expected.getClass().getName()));
    //System.out.println(String.valueOf(JournalServlet.getSubjects(test)));


    //Assert.assertEquals(expected, JournalServlet.getSubjects(test));
  }

  @Test
  public void catchUnequalAttributes() {
    
    List<String> test = new ArrayList();
    test.add("string");
    test.add("0.0");
    test.add("string");

    List<String> expected = new ArrayList();
    expected.add("Unequal amount of subject, type, and salience");

    //Assert.assertEquals(expected, JournalServlet.getSubjects(test));
  }
  
  /**
  @Test
  public void filterIfCommon() {
    
    List test = Mockito.mock(List.class);
    test.add("string");
    test.add("0.3");
    test.add("COMMON");
    test.add("Chiamaka");
    test.add("0.7");
    test.add("PROPER");

    List expected = Mockito.mock(List.class);
    expected.add("Chiamaka");
    test.add("0.7");
    test.add("PROPER");
    
    Assert.assertEquals(expected, JournalServlet.getSubjects(test));
  }
  */



  
}