package de.unidue.inf.is;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.Babble;
import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.stores.UserStore;
import de.unidue.inf.is.utils.DBUtil;

public final class babble_searchServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private String searched ="Ich";

    
    //TODO man braucht irgendeinen Fall mit der initialisierung von babblelist, wenn diese noch nicht existiert
    //d.h. wenn man die Website zB zum ersten mal startet ohne davor ein doPost aufzurufen -> Freemarker error
    

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException {
    	
    	Connection myConnection = null;
		DBUtil myDB = null;
		List<Babble> babblelist = new ArrayList<>();
		request.setAttribute("babblelist", babblelist);
		
		try {
 			myConnection = myDB.getConnection("babble");
 			PreparedStatement myPrepStatement = myConnection.prepareStatement("SELECT text,created,creator,id FROM babble WHERE text LIKE ? ORDER BY id DESC");
 			myPrepStatement.setString(1,"%"+searched+"%");
 			ResultSet resultSet = myPrepStatement.executeQuery();

 			while (resultSet.next()){
 		 		
 	 			String likesString ="SELECT b.id,count(lb.babble) AS likes FROM babble b INNER JOIN likesbabble lb ON b.id=lb.babble WHERE lb.type='like' AND b.id=? GROUP BY b.id ";
 	 			PreparedStatement myLikesStatement = myConnection.prepareStatement(likesString);
 	 			String likeCount = "0";
 	 			String dislikesString ="SELECT b.id,count(lb.babble) AS dislikes FROM babble b INNER JOIN likesbabble lb ON b.id=lb.babble WHERE lb.type='dislike' AND b.id=? GROUP BY b.id ";
 	 			PreparedStatement myDislikesStatement = myConnection.prepareStatement(dislikesString);
 	 			String dislikeCount = "0";
 	 			String rebabbleString ="SELECT rb.babble ,count(rb.babble) AS rebabbles FROM rebabble rb WHERE rb.babble=? GROUP BY rb.babble ";
 	 			PreparedStatement myRebabbleStatement = myConnection.prepareStatement(rebabbleString);
 	 			String rebabbleCount = "0";
 	 			
 	 			myLikesStatement.setString(1, resultSet.getString("id"));
 	 			myDislikesStatement.setString(1, resultSet.getString("id"));
 	 			myRebabbleStatement.setString(1, resultSet.getString("id"));
 	 			
 	 			ResultSet likesResultSet = myLikesStatement.executeQuery();
 	 			ResultSet dislikesResultSet = myDislikesStatement.executeQuery();
 	 			ResultSet rebabblesResultSet = myRebabbleStatement.executeQuery();
 	 			
 	 			while(likesResultSet.next()){
 	 				likeCount = likesResultSet.getString("likes");
 	 			}
 	 			while(dislikesResultSet.next()){
 	 				dislikeCount = dislikesResultSet.getString("dislikes");
 	 			}
 	 			while(rebabblesResultSet.next()){
 	 				rebabbleCount = rebabblesResultSet.getString("rebabbles");
 	 			}
 	 			babblelist.add(new Babble(resultSet.getString("creator").toString(),resultSet.getString("text").toString(),resultSet.getString("created").toString(),likeCount,dislikeCount,rebabbleCount,resultSet.getString("id")));
 					request.setAttribute("babblelist", babblelist); 
 	 		}
 		
 		} catch (SQLException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		} finally {
 			try {
 				myConnection.close();
 		
 			} catch (SQLException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
 		}
		
		request.getRequestDispatcher("babble_search.ftl").forward(request, response);
    	
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                    IOException {
    	
 		
    	searched = request.getParameter("search");
    	doGet(request,response);
    }
}

