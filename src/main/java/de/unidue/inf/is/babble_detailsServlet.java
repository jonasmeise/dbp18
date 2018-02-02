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
    private String likeValue = "Like";
	private String dislikeValue = "Dislike";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException {
    	
    	Connection myConnection = null;
		DBUtil myDB = null;
		
		likeValue="Like";
		dislikeValue="Dislike";
		
		request.setAttribute("likes", 0);
		request.setAttribute("dislikes", 0);
		request.setAttribute("rebabbles", 0);
		request.setAttribute("rebabbleValue", "Rebabble");
		request.setAttribute("deleteType", "submit");
		request.setAttribute("likeValue", likeValue);
		request.setAttribute("dislikeValue", dislikeValue);
		
    	try {
 			myConnection = myDB.getConnection("babble");
 			
 			//check if already rebabbled
 	
 		 		PreparedStatement myStatement =  myConnection.prepareStatement("SELECT babble FROM Rebabble WHERE babble = ? AND username = ? ");
 				myStatement.setString(1, request.getParameter("babbleIDLink"));
 		 		myStatement.setString(2, initialUserID);
 				ResultSet rebabbleValueSet = myStatement.executeQuery();
 				while(rebabbleValueSet.next()){
 				request.setAttribute("rebabbleValue", "Dont Rebabble");
 				}
 				
 				//check if already liked
 				
 				PreparedStatement myLikedStatement =  myConnection.prepareStatement("SELECT babble FROM LikesBabble WHERE type='like' AND babble = ? AND username = ?");
 				myLikedStatement.setString(1, request.getParameter("babbleIDLink"));
 		 		myLikedStatement.setString(2, initialUserID);
 				ResultSet likedSet = myLikedStatement.executeQuery();
 				while(likedSet.next()){
 					likeValue = "Dont Like";
 					request.setAttribute("likeValue", likeValue);
 				}
 				
 				//check if already disliked
 				
 				PreparedStatement myDislikedStatement =  myConnection.prepareStatement("SELECT babble FROM LikesBabble WHERE type='dislike' AND babble = ? AND username = ?");
 				myDislikedStatement.setString(1, request.getParameter("babbleIDLink"));
 		 		myDislikedStatement.setString(2, initialUserID);
 				ResultSet dislikedSet = myDislikedStatement.executeQuery();
 				while(dislikedSet.next()){
 					dislikeValue = "Dont Dislike";
 					request.setAttribute("dislikeValue", dislikeValue);
 				}
 				
 			//check who is creator for deleteButton
 				
 				PreparedStatement creatorStatement =  myConnection.prepareStatement("SELECT creator FROM Babble WHERE id = ?");
 				creatorStatement.setString(1, request.getParameter("babbleIDLink"));
 				ResultSet creatorSet = creatorStatement.executeQuery();
 				while(creatorSet.next()){
 					if(!creatorSet.getString("creator").equals(initialUserID)){
 						request.setAttribute("deleteType", "hidden");
 					}
 				}
 			
 				//checks for count of likes,rebabbles and dislikes
 				
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
			
			//likeButton
			
		    if (request.getParameter("likeButton") != null) {
		    	
		    	if(request.getParameter("likeButton").toString().equals("Like")){
		    		if(dislikeValue.equals("Dont Dislike")){
		    			PreparedStatement myUpdateStatement = myConnection.prepareStatement("UPDATE LikesBabble SET type='like' WHERE username=? AND babble=?");
			    		myUpdateStatement.setString(1, initialUserID);
			    		myUpdateStatement.setString(2, request.getParameter("babbleIDLink"));
			    		myUpdateStatement.executeUpdate();
			    		doGet(request, response);
		    		}else{
		    		PreparedStatement myPrepStatement = myConnection.prepareStatement("INSERT INTO LikesBabble (username, babble, type) VALUES ( ? , ? , 'like')");
		 			myPrepStatement.setString(1, initialUserID);
		 			myPrepStatement.setString(2, request.getParameter("babbleIDLink"));	//übergebene ID des Babbles aus dem HMTL link=? als beispiel haben wir 3 übergeben.
		 			myPrepStatement.executeUpdate();
		 			doGet(request, response);
		    		}
		    	}else if(request.getParameter("likeButton").toString().equals("Dont Like")){
		    		
		    		PreparedStatement myDeleteStatement = myConnection.prepareStatement("DELETE FROM LikesBabble WHERE username=? AND babble=?");
		    		myDeleteStatement.setString(1, initialUserID);
		    		myDeleteStatement.setString(2, request.getParameter("babbleIDLink"));
		    		myDeleteStatement.executeUpdate();
		    		doGet(request, response);
		    	}
		 			
		    		
				    
		    }
		    
		    //DislikeButton
		    
		    
if (request.getParameter("dislikeButton") != null) {
		    	
		    	if(request.getParameter("dislikeButton").toString().equals("Dislike")){
		    		if(likeValue.equals("Dont Like")){
		    			PreparedStatement myUpdateStatement = myConnection.prepareStatement("UPDATE LikesBabble SET type='dislike' WHERE username=? AND babble=?");
			    		myUpdateStatement.setString(1, initialUserID);
			    		myUpdateStatement.setString(2, request.getParameter("babbleIDLink"));
			    		myUpdateStatement.executeUpdate();
			    		doGet(request, response);
		    		}else{
		    		PreparedStatement myPrepStatement = myConnection.prepareStatement("INSERT INTO LikesBabble (username, babble, type) VALUES ( ? , ? , 'dislike')");
		 			myPrepStatement.setString(1, initialUserID);
		 			myPrepStatement.setString(2, request.getParameter("babbleIDLink"));	//übergebene ID des Babbles aus dem HMTL link=? als beispiel haben wir 3 übergeben.
		 			myPrepStatement.executeUpdate();
		 			doGet(request, response);
		    		}
		    	}else if(request.getParameter("dislikeButton").toString().equals("Dont Dislike")){
		    		PreparedStatement myDeleteStatement = myConnection.prepareStatement("DELETE FROM LikesBabble WHERE username=? AND babble=?");
		    		myDeleteStatement.setString(1, initialUserID);
		    		myDeleteStatement.setString(2, request.getParameter("babbleIDLink"));
		    		myDeleteStatement.executeUpdate();
		    		doGet(request, response);
		    	}
}
		    	
		    
		    //rebabbleButton
		    
		    if (request.getParameter("rebabbleButton") != null) {
		    	
		    	if(request.getParameter("rebabbleButton").toString().equals("Rebabble")){
		    		PreparedStatement myPrepStatement = myConnection.prepareStatement("INSERT INTO ReBabble (username, babble) VALUES ( ? , ? )");
		 			myPrepStatement.setString(1, initialUserID);
		 			myPrepStatement.setString(2, request.getParameter("babbleIDLink"));	//übergebene ID des Babbles aus dem HMTL link=? als beispiel haben wir 3 übergeben.
		 			myPrepStatement.executeUpdate();
				    doGet(request, response);
		    	}else if(request.getParameter("rebabbleButton").toString().equals("Dont Rebabble")){
		    		PreparedStatement myDeleteStatement = myConnection.prepareStatement("DELETE FROM Rebabble WHERE username=? AND babble=?");
		    		myDeleteStatement.setString(1, initialUserID);
	    			myDeleteStatement.setString(2, request.getParameter("babbleIDLink"));
	    			myDeleteStatement.executeUpdate();
	    			doGet(request, response);
		    	}
		 			
		    }
		    
		    //DeleteButton
		    
		    if (request.getParameter("deleteButton") != null) {
		    		PreparedStatement myPrepStatement = myConnection.prepareStatement("DELETE FROM Babble WHERE id=? AND creator=?");
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
		
		//Back to userpage Button
		
		if (request.getParameter("back") != null) {
			
		}
    }       
}
