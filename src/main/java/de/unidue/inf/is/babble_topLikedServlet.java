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
 			myConnection = myDB.getConnection("babble");	//SELECT b.text,b.created,b.creator,b.id, count(lb.babble) AS likes FROM babble b JOIN LikesBabble lb ON b.id = lb.babble  WHERE lb.type = 'likes' AND b.creator = ? GROUP BY  b.text,b.created,b.creator,b.id ORDER BY b.id DESC
 			PreparedStatement myPrepStatement = myConnection.prepareStatement("SELECT text,created,creator,id FROM babble WHERE creator = ? ORDER BY id DESC");
 			myPrepStatement.setString(1, initialUserID);
 			ResultSet resultSet = myPrepStatement.executeQuery();
 			
 	
 		while (resultSet.next()){					//resultSet.getString("likes").toString()
 				babblelist.add(new Babble(resultSet.getString("creator").toString(),resultSet.getString("text").toString(),resultSet.getString("created").toString(),"","","",resultSet.getString("id"))); //ID klappt nicht zu übergeben
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
