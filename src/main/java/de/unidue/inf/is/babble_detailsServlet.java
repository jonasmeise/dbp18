package de.unidue.inf.is;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.utils.DBUtil;

public final class babble_detailsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String initialUserID = "FooBar";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException {
    	
    	Connection myConnection = null;
		DBUtil myDB = null;
		
		request.setAttribute("likes", 0);
		request.setAttribute("dislikes", 0);
		request.setAttribute("rebabbles", 0);
		
    	try {
 			myConnection = myDB.getConnection("babble");
 			
 			PreparedStatement myPrepStatement = myConnection.prepareStatement("SELECT text, created, creator FROM babble WHERE id = ?");
 			myPrepStatement.setString(1, request.getParameter("babbleIDLink"));	
 			ResultSet resultSet = myPrepStatement.executeQuery();
 			
 			PreparedStatement likeStatement = myConnection.prepareStatement("SELECT babble, count(type) as likes FROM LikesBabble WHERE type='like' AND babble = ? GROUP BY babble");
 			likeStatement.setString(1,  request.getParameter("babbleIDLink"));
 			ResultSet likeSet = likeStatement.executeQuery();
 			
 			PreparedStatement dislikeStatement = myConnection.prepareStatement("SELECT babble, count(type) as dislikes FROM LikesBabble WHERE type='dislike' AND babble = ? GROUP BY babble");
 			dislikeStatement.setString(1,request.getParameter("babbleIDLink"));
 			ResultSet dislikeSet = dislikeStatement.executeQuery();
 			
 			PreparedStatement rebabbleStatement = myConnection.prepareStatement("SELECT babble, count(babble) AS rebabbles FROM Rebabble WHERE babble = ? GROUP BY babble");
 			rebabbleStatement.setString(1,  request.getParameter("babbleIDLink"));
 			ResultSet rebabbleSet = rebabbleStatement.executeQuery();
 			
 		while (resultSet.next()){	
 			request.setAttribute("text",resultSet.getString("text").toString());
 			request.setAttribute("created",resultSet.getString("created").toString());
 			request.setAttribute("creator",resultSet.getString("creator").toString());
 		}
 		
 		while (likeSet.next()) {
 			request.setAttribute("likes", likeSet.getString("likes").toString());
	 		
 		}
 		while (dislikeSet.next()) {
 			request.setAttribute("dislikes", dislikeSet.getString("dislikes").toString());
 		}
 		while (rebabbleSet.next()) {
 			request.setAttribute("rebabbles", rebabbleSet.getString("rebabbles").toString());
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
    	request.setAttribute("babbleid", request.getParameter("babbleIDLink"));
        request.getRequestDispatcher("babble_details.ftl").forward(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                    IOException {

    	Connection myConnection = null;
		DBUtil myDB = null;
	
		try {
			myConnection = myDB.getConnection("babble");
			PreparedStatement myPrepStatement;
			PreparedStatement myDeleteStatement;
			
		    if (request.getParameter("likeButton") != null) {
		    		PreparedStatement myUpdateStatement = myConnection.prepareStatement("UPDATE LikesBabble SET type='like' WHERE username=? AND babble=?");
		    	
		    		/*myDeleteStatement = myConnection.prepareStatement("DELETE FROM LikesBabble WHERE username=? AND babble=?");
		    		myDeleteStatement.setString(1, initialUserID);
		    		myDeleteStatement.setString(2, request.getParameter("babbleIDLink"));
		    		myDeleteStatement.executeUpdate();*/
		    		
		 			myPrepStatement = myConnection.prepareStatement("INSERT INTO LikesBabble (username, babble, type) VALUES ( ? , ? , 'like')");
		 			myPrepStatement.setString(1, initialUserID);
		 			myPrepStatement.setString(2, request.getParameter("babbleIDLink"));	//übergebene ID des Babbles aus dem HMTL link=? als beispiel haben wir 3 übergeben.
		 			myPrepStatement.executeUpdate();
		    		myUpdateStatement.executeUpdate();
		    		
				    doGet(request, response);
		    }
		    
		    
		    if (request.getParameter("dislikeButton") != null) {
		    	PreparedStatement myUpdateStatement = myConnection.prepareStatement("UPDATE LikesBabble SET type='like' WHERE username=? AND babble=?");
		    	/*
	    			myDeleteStatement = myConnection.prepareStatement("DELETE FROM LikesBabble WHERE username=? AND babble=?");
		    		myDeleteStatement.setString(1, initialUserID);
	    			myDeleteStatement.setString(2, request.getParameter("babbleIDLink"));
	    			myDeleteStatement.executeUpdate();
		    	
		 			myPrepStatement = myConnection.prepareStatement("INSERT INTO LikesBabble (username, babble, type) VALUES ( ? , ? , 'dislike')");
		 			myPrepStatement.setString(1, initialUserID);
		 			myPrepStatement.setString(2, request.getParameter("babbleIDLink"));	//übergebene ID des Babbles aus dem HMTL link=? als beispiel haben wir 3 übergeben.
		 			myPrepStatement.executeUpdate();*/	
		    		myUpdateStatement.executeUpdate();
				    doGet(request, response);
		    }
		    if (request.getParameter("rebabbleButton") != null) {
		    	
			    	myDeleteStatement = myConnection.prepareStatement("DELETE FROM Rebabble WHERE username=? AND babble=?");
		    		myDeleteStatement.setString(1, initialUserID);
	    			myDeleteStatement.setString(2, request.getParameter("babbleIDLink"));
	    			myDeleteStatement.executeUpdate();
    			
		 			myPrepStatement = myConnection.prepareStatement("INSERT INTO ReBabble (username, babble) VALUES ( ? , ? )");
		 			myPrepStatement.setString(1, initialUserID);
		 			myPrepStatement.setString(2, request.getParameter("babbleIDLink"));	//übergebene ID des Babbles aus dem HMTL link=? als beispiel haben wir 3 übergeben.
		 			myPrepStatement.executeUpdate();
				    doGet(request, response);
		    }
		    if (request.getParameter("deleteButton") != null) {
		 			myPrepStatement = myConnection.prepareStatement("DELETE FROM Babble WHERE id=? AND creator=?");
		 			myPrepStatement.setString(1, request.getParameter("babbleIDLink"));	//übergebene ID des Babbles aus dem HMTL link=? als beispiel haben wir 3 übergeben.
		 			myPrepStatement.setString(2, initialUserID);
		 			myPrepStatement.executeUpdate();	
		 			response.sendRedirect("./user_profile");
		 			
		 	}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
 		} finally {
 			try {
 				myConnection.close();
 			} catch (SQLException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
 		}
    }       
}
