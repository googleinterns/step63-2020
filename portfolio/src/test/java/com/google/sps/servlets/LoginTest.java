package com.google.sps;
import java.util.List;

import com.google.sps.servlets.LoginServlet;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;
import javax.servlet.ServletException;
import java.io.IOException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.sps.servlets.NickNameServlet;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
// import com.google.appengine.repackaged.com.google.storage.onestore.v3.proto2api.OnestoreEntity.User;
import com.google.gson.JsonObject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import junit.framework.TestCase;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

@RunWith(JUnit4.class)
public final class LoginTest {

  private final LocalServiceTestHelper helper =
    new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setUp() {
    helper.setUp();
  }

  @Test
  public void testDoGet() throws ServletException, IOException{
    LoginServlet servlet = new LoginServlet();
    UserService mockUser = Mockito.mock(UserService.class);
    JsonObject mockObj1 = new JsonObject();
    JsonObject mockObj2 = new JsonObject();
    String expected1 = "{\"status\":false,\"url\":\"/nickname.html\"}";
    String expected2 = "{\"url\":\"/\",\"status\":true,\"name\":\"example\"}";


    if(!mockUser.isUserLoggedIn()){
      mockObj1.addProperty("status", false);
      mockObj1.addProperty("url", "/nickname.html");

      String json1 = new Gson().toJson(mockObj1);
      Assert.assertEquals(expected1, json1);
    }
    String name = "example";
    
    mockObj2.addProperty("url", "/");
    mockObj2.addProperty("status", true);
    mockObj2.addProperty("name", name);

    String json2 = new Gson().toJson(mockObj2); 
    Assert.assertEquals(expected2, json2);
  }

  @Test
  public void testGetName() throws ServletException, IOException{
    String id = "123456789";
    String name = "example";
    LoginServlet servlet = new LoginServlet();
    DatastoreService mockDatastore = Mockito.mock(DatastoreService.class);

    Query mockQuery = new Query("Test")
      .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = mockDatastore.prepare(mockQuery);
    Entity test = results.asSingleEntity();
    if(test == null){
        System.out.println("entity is null");
        Assert.assertNull(test);
    }

    Assert.assertEquals(test.getProperty("nickname"), name);
  }
}
