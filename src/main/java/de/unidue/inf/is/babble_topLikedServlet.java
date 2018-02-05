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
import de.unidue.inf.is.domain.UtilityClass;
import de.unidue.inf.is.utils.DBUtil;

public final class babble_topLikedServlet extends HttpServlet  {
	
	 private static final String initialUserID ="FooBar";

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection myConnection = null;
		DBUtil myDB = null; //ab
		List<Babble> babblelist = new ArrayList<>();
		
		UtilityClass utility= new UtilityClass();
		
		try {	
   		String top5String ="SELECT b.text,b.created,b.creator,b.id, count(lb.babble) AS likes FROM babble b INNER JOIN likesbabble lb ON lb.babble=b.id WHERE lb.type ='like' GROUP BY  b.id,b.text,b.created,b.creator ORDER BY likes DESC FETCH FIRST 5 ROWS ONLY  ";
   		myConnection = myDB.getConnection("babble");	
			PreparedStatement myBabbleStatement = myConnection.prepareStatement(top5String);
			ResultSet resultSet = myBabbleStatement.executeQuery();
			
			request.setAttribute("babblelist", utility.createMetaData(myConnection, resultSet));
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
