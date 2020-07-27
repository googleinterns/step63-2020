
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
public final class JournalServlet {

    public static String setEmail() {
        String email = "chiamaka@google.com"; 

        return email;
  }

  public static List<String> getSubjects(List<String> entityNameSalianceAndType){

    List mostImportant = new ArrayList();

    if ((entityNameSalianceAndType.size()%2)>0) {
        mostImportant.add("Unequal amount of subject, type, and salience");
    }
    else if (entityNameSalianceAndType.size() <3) {
        mostImportant.add("nothing");
        mostImportant.add("0.");
    }
    else {
        mostImportant.add(entityNameSalianceAndType.get(0));
        mostImportant.add(entityNameSalianceAndType.get(1));
        mostImportant.add(entityNameSalianceAndType.get(2));
        mostImportant.add(entityNameSalianceAndType.get(3));
    }

    try {
        Float.valueOf(entityNameSalianceAndType.get(1));
        Float.valueOf(entityNameSalianceAndType.get(3));
    }
    catch (Exception e) {
        List<String> error = new ArrayList();
        error.add("Unequal amount of subject, type, and salience");
        mostImportant = error;
    }
    return mostImportant;


  }


  
}