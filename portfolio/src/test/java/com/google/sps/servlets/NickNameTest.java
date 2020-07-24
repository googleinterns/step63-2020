package com.google.sps;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
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
public class NickNameTest extends TestCase {

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

// Datastore mockDatastore = Mockito.mock(DatastoreService.class);
// Entity mockEntity = Mockito.mock(Entity.class);

// when(mockEntity.getProperty("nickname")).thenReturn("got");
// when(mockEntity.setProperty("nickname")).thenReturn("set");

  //adds nickname to datastore
  @Test
  public void successfulDoPost() {
    NickNameServlet servlet = Mockito.mock(NickNameServlet.class);
    UserService mockUser = Mockito.mock(UserServiceFactory.class);
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    Datastore mockDatastore = Mockito.mock(DatastoreService.class);

    if (!mockUser.isUserLoggedIn()){
      //make sure that it redirects them to nickname.html
      response.sendRedirect("/nickname.html");
    }

    String nickname = "testName";
    String id = "123456789";

    Entity test = new Entity("User", id);
    test.setProperty("id", id);
    test.setProperty("nickname", nickname);

    when(mockDatastore.put(test)).thenReturn(test);
    
    //make sure it redirects them to home page
    response.sendRedirect("/index.html");
    
  }
}

/*
API object is parameter of test, break down code into logical functions
in the already existing servlet (allows to test functions in isolation)

make mock datastore, create a new class that acts like the datastore, but does nothing
*/
