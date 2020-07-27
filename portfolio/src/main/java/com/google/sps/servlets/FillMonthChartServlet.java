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
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.users.User;

@WebServlet("/fillMonth")
public class FillMonthChartServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    ArrayList<String> allProperties = new ArrayList<>();

    UserService userService = UserServiceFactory.getUserService();
    if(userService.isUserLoggedIn()){
        User user = userService.getCurrentUser();
        String userProperty = "";
        String userNickname = user.getNickname();
        Query query = new Query("input").addSort("input", SortDirection.DESCENDING);
        query.setFilter(new FilterPredicate("User", FilterOperator.EQUAL, userNickname));
        PreparedQuery results = datastore.prepare(query);
        int limit =0;
        if(results.countEntities() < 62){
            limit = results.countEntities();
        }
        else{
            limit = 62;
        }

        //Writes properties specific to the current user to /fill-charts to be fetched
        for(Entity entity:results.asIterable(FetchOptions.Builder.withLimit(limit))){
            try{
                Key oldKey = entity.getKey();
                Entity oldEntity = datastore.get(oldKey);
                String data = (String)oldEntity.getProperty("input");
                allProperties.add(data);
        }
            catch(EntityNotFoundException e){}
        }
    }
        response.getWriter().println(allProperties);
    
  } 
}