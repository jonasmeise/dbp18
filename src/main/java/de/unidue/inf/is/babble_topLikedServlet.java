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
import de.unidue.inf.is.utils.DBUtil;

public final class babble_topLikedServlet extends HttpServlet  {
	
	 private static final String initialUserID ="FooBar";

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection myConnection = null;
		DBUtil myDB = null;
		List<Babble> babblelist = new ArrayList<>();
		
		try {	
   		String top5String ="SELECT b.text,b.created,b.creator,b.id, count(lb.babble) AS likes FROM babble b INNER JOIN likesbabble lb ON lb.babble=b.id WHERE lb.type ='like' GROUP BY  b.id,b.text,b.created,b.creator ORDER BY likes DESC FETCH FIRST 5 ROWS ONLY  ";
   		myConnection = myDB.getConnection("babble");	
			PreparedStatement myBabbleStatement = myConnection.prepareStatement(top5String);
			ResultSet resultSet = myBabbleStatement.executeQuery();
			
	
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
			    
        request.getRequestDispatcher("babble_topLiked.ftl").forward(request, response);
		
	}
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                    IOException {
    	
    }
}
