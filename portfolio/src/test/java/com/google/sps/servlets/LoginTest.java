package com.google.sps;
import java.util.List;

import com.google.sps.servlets.LoginServlet;

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

  public void testDoGet() throws ServletException, IOException{
    LoginServlet servlet = new LoginServlet();
    UserService mockUser = Mockito.mock(UserService.class);
    JsonObject mockObj = Mockito.mock(JsonObject.class);
    JsonObject actual = Mockito.mock(JsonObject.class);


    if(!mockUser.isUserLoggedIn()){
      String loginUrl = mockUser.createLoginURL("/index.html");
      mockObj.addProperty("status", false);
      mockObj.addProperty("url", loginUrl);

      String json = new Gson().toJson(mockObj);
    }

    String name = "example";
    String logoutUrl = mockUser.createLogoutURL("/");

    mockObj.addProperty("status", true);
    mockObj.addProperty("url", logoutUrl);
    mockObj.addProperty("name", name);

    String json = new Gson().toJson(mockObj);
    
    assertEquals(json, actual);
  }

}
