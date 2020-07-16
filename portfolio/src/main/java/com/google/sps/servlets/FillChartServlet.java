import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.users.User;

@WebServlet("/fill-charts")
public class FillChartServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query("input");
    PreparedQuery results = datastore.prepare(query);


    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    String userProperty = "";
    String userEmail = user.getEmail();

    //Writes properties specific to the current user to /fill-charts to be fetched
    ArrayList<String> allProperties = new ArrayList<>();
    for(Entity entity:results.asIterable()){
        userProperty = (String)entity.getProperty("User");

        if(userService.isUserLoggedIn() && userProperty.equals(userEmail)){
        allProperties.add((String)entity.getProperty("input"));
        }
    }
    response.getWriter().println(allProperties);
  }
}