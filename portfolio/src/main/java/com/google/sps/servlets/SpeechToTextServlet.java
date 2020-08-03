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

import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


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
import java.io.FileWriter;
import java.io.File;

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


@WebServlet("/speech")
public class SpeechToTextServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Query sentenceQuery = new Query("Sentence").addSort("time", SortDirection.DESCENDING);
    PreparedQuery sentenceResults = datastore.prepare(sentenceQuery);


  }
  
  //Retrieves and stores journal entry from user

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
    
    // Get the input from the form.
    String input = request.getParameter("audio-link");

        List<String> response_text = new ArrayList();
        response_text.add("Transcription");

        /** Demonstrates using the Speech API to transcribe an audio file. */
         // Instantiates a client
         try (SpeechClient speechClient = SpeechClient.create()) {
         

         try {
        FileWriter myWriter = new FileWriter("audio.wav");
        myWriter.write(input);
        myWriter.close();
        System.out.println("Successfully wrote to the file.");
        }   catch (IOException e) {
        System.out.println("An error occurred.");
        }

        // The path to the audio file to transcribe
        String fileName = "audio.wav";

        // Reads the audio file into memory
            Path path = Paths.get(fileName);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!"+fileName);
            byte[] data = Files.readAllBytes(path);
            ByteString audioBytes = ByteString.copyFrom(data);

            // Builds the sync recognize request
            RecognitionConfig config =
                RecognitionConfig.newBuilder()
                    .setEncoding(AudioEncoding.LINEAR16)
                    .setSampleRateHertz(16000)
                    .setLanguageCode("en-US")
                    .build();
            RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioBytes).build();

            // Performs speech recognition on the audio file
            RecognizeResponse recognizedResponse = speechClient.recognize(config, audio);
            List<SpeechRecognitionResult> results = recognizedResponse.getResultsList();
            
            for (SpeechRecognitionResult result : results) {
                // There can be several alternative transcripts for a given chunk of speech. Just use the
                // first (most likely) one here.
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                response_text.add("Transcription"+alternative.getTranscript());

                System.out.printf("Transcription: %s%n", alternative.getTranscript());
            }
        } 
        
        

 
    // Redirect back to the HTML page.
    //response.sendRedirect("/journal.html");
    response.setContentType("text/html;");
    response.getWriter().println(convertToJsonUsingGsonforLists(response_text));

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
}
 

