// package com.google.sps;
// import java.util.List;
// import org.junit.Assert;
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.junit.runners.JUnit4;
// import org.mockito.Mockito;
// import javax.servlet.ServletException;
// import java.io.IOException;
// import static org.mockito.Mockito.when;
// import static org.mockito.Mockito.doReturn;
// import com.google.appengine.api.datastore.DatastoreService;
// import com.google.appengine.api.datastore.DatastoreServiceFactory;
// import com.google.appengine.api.datastore.Entity;
// import com.google.appengine.api.datastore.Query;
// import com.google.sps.servlets.NickNameServlet;
// import com.google.appengine.api.datastore.PreparedQuery;
// import com.google.appengine.api.datastore.Query;
// import com.google.appengine.api.users.UserService;
// import com.google.appengine.api.users.UserServiceFactory;
// // import com.google.appengine.repackaged.com.google.storage.onestore.v3.proto2api.OnestoreEntity.User;
// import com.google.gson.JsonObject;
// import javax.servlet.annotation.WebServlet;
// import javax.servlet.http.HttpServlet;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import org.junit.After;
// import org.junit.Before;
// import org.junit.Test;
// import junit.framework.TestCase;
// import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
// import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

// @RunWith(JUnit4.class)
// public class NickNameTest extends TestCase {

//   private final LocalServiceTestHelper helper =
//       new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

//   @Before
//   public void setUp() {
//     helper.setUp();
//   }

// //   @After
// //   public void tearDown() {
// //     helper.tearDown();
// //   }

// // Datastore mockDatastore = Mockito.mock(DatastoreService.class);
// // Entity mockEntity = Mockito.mock(Entity.class);

// // when(mockEntity.getProperty("nickname")).thenReturn("got");
// // when(mockEntity.setProperty("nickname")).thenReturn("set");

//   //adds nickname to datastore
//   @Test
//   public void successfulDoPost() throws ServletException, IOException{
//     NickNameServlet servlet = new NickNameServlet();
//     UserService mockUser = Mockito.mock(UserService.class);
//     HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
//     DatastoreService mockDatastore = Mockito.mock(DatastoreService.class);

//     if (!mockUser.isUserLoggedIn()){
//       //make sure that it redirects them to nickname.html
//     //   when(response.sendRedirect("/nickname.html")).thenReturn("/nickname.html");
//     // assertEquals(response.sendRedirect("/nickname.html"), "/nickname.html");
//       response.sendRedirect("/nickname.html");
//     }

//     String nickname = "testName";
//     String id = "123456789";

//     Entity test = new Entity("User", id);
//     test.setProperty("id", id);
//     test.setProperty("nickname", nickname);
    
//     when(mockDatastore.put(test)).thenReturn("added");
//     assertEquals(mockDatastore.put(test), "added");
    
//     //make sure it redirects them to home page
//     // when(response.sendRedirect("/index.html")).thenReturn("/index.html");
//     // assertEquals(response.sendRedirect("/index.html"), "/index.html");

//     response.sendRedirect("/index.html");
    
//   }
// }

// /*
// API object is parameter of test, break down code into logical functions
// in the already existing servlet (allows to test functions in isolation)

// make mock datastore, create a new class that acts like the datastore, but does nothing

// getting this error:
// [ERROR] /usr/local/google/home/erysd/step/portfolio/step63-2020/portfolio/src/test/java/com/google/sps/servlets/NickNameTest.java:[27,54] package com.google.appengine.tools.development.testing does not exist
// [ERROR] /usr/local/google/home/erysd/step/portfolio/step63-2020/portfolio/src/test/java/com/google/sps/servlets/NickNameTest.java:[28,54] package com.google.appengine.tools.development.testing does not exist
// */
