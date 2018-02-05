package de.unidue.inf.is;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.Babble;
import de.unidue.inf.is.domain.UtilityClass;
import de.unidue.inf.is.utils.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class user_profileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String initialUserID ="FooBar";	//unser User (wir)
    private String userID="FooBar";		//startseite (beginnend mit uns) und userID der jeweiligen Seiten
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException {
    	
		UtilityClass utility= new UtilityClass();
    	
    	Connection myConnection = null;
		DBUtil myDB = null;
		List<Babble> babblelist = new ArrayList<>();
		List<Babble> babblelist2 = new ArrayList<>();
		List<Babble> babblelist3 = new ArrayList<>();
		
		//SQL Abfrage für die Persönlichen Daten nach derzeitiger userID
    	
		if(!userID.equals(initialUserID)) {
			request.setAttribute("inputType", "hidden");
		}
		else
		{
			request.setAttribute("inputType", "submit");
		}
		
    	try {
			myConnection = myDB.getConnection("babble");
			PreparedStatement myPrepStatement = myConnection.prepareStatement("SELECT foto,username,name,status FROM BabbleUser WHERE username = ?");
			myPrepStatement.setString(1, userID);
			ResultSet resultSet = myPrepStatement.executeQuery();
			
			request.setAttribute("foto", "");
			request.setAttribute("username", "");
			request.setAttribute("name", "");
			request.setAttribute("status", "");

		while (resultSet.next()){
				
				String speicher = resultSet.getString("foto");
				if(speicher!="" && speicher!=null) {
					request.setAttribute("foto", resultSet.getString(speicher));
				}

				request.setAttribute("username", resultSet.getString("username"));
				request.setAttribute("name", resultSet.getString("name"));
				request.setAttribute("status", resultSet.getString("status"));
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
    	
    	
    	//SQL-Abfrage für follow
    	
    	 if (userID.equals("FooBar")) {
		    	request.setAttribute("followType","hidden");
    	 } else {
		    	request.setAttribute("followType","submit");
		 }
    	 
    	request.setAttribute("follows", "Follow");
    	 
    	 
    	try {
    	myConnection = myDB.getConnection("babble");
 		PreparedStatement myStatement =  myConnection.prepareStatement("SELECT follower, followee FROM follows WHERE follower=? AND followee = ?");
			myStatement.setString(1, initialUserID);
			myStatement.setString(2, userID);
			ResultSet resultSet = myStatement.executeQuery();
			while(resultSet.next()){
			request.setAttribute("follows", "Dont Follow anymore");
			}
    	 }catch (SQLException e) {
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
    	
    	//SQL-Abfrage für block
    	
   	 if (userID.equals("FooBar")) {
		    	request.setAttribute("blockType","hidden");
   	 } else {
		    	request.setAttribute("blockType","submit");
		 }
   	 
   	 request.setAttribute("blocks", "Block");
   	 
   	try {
   	myConnection = myDB.getConnection("babble");
		PreparedStatement myStatement =  myConnection.prepareStatement("SELECT blocker, blockee FROM blocks WHERE blocker = ? AND blockee = ? ");
			myStatement.setString(1, initialUserID);
			myStatement.setString(2, userID);
			ResultSet resultSet = myStatement.executeQuery();
			while(resultSet.next()){
			request.setAttribute("blocks", "Dont Block anymore");
			}
   	 }catch (SQLException e) {
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
   	 
    	 
    	 //SQL-Abfrage für Babbles
   	
   	//Check if you are blocked, funktioniert noch nicht, SQL ist richtig?
   	
   	request.setAttribute("blockedReason", "");
	request.setAttribute("blockedStatus", "display:inline");
	request.setAttribute("reasons", "");
   		try {
			myConnection = myDB.getConnection("babble");
			PreparedStatement myPrepStatement = myConnection.prepareStatement("SELECT blocker, blockee, reason FROM blocks where blocker = ? AND blockee = ?");
			myPrepStatement.setString(1, userID);
			myPrepStatement.setString(2, initialUserID);
			ResultSet resultSet = myPrepStatement.executeQuery();
			
		while (resultSet.next()){	
			request.setAttribute("blockedStatus", "display:none");
			request.setAttribute("blockedReason", "You are blocked. Reason:");
			String tempReason = resultSet.getString("reason");
			if(tempReason != null){
			request.setAttribute("reasons", resultSet.getString("reason"));
			}
			
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
   		
   		//Babbles
    	 try {	
    
    		 String oldString ="SELECT b.text,b.created,b.creator,b.id FROM babble b WHERE b.creator = ?";
    		 
    		myConnection = myDB.getConnection("babble");	
 			PreparedStatement myBabbleStatement = myConnection.prepareStatement(oldString);
 			myBabbleStatement.setString(1, userID);
 			
 			ResultSet resultSet = myBabbleStatement.executeQuery();
 			
 			myConnection = myDB.getConnection("babble");	
 			PreparedStatement myLikedStatement = myConnection.prepareStatement("SELECT b.text,b.created,b.creator,b.id FROM babble b, likesBabble lb WHERE lb.type='like' AND  b.id=lb.babble AND username = ? ORDER BY b.created DESC");
 			myLikedStatement.setString(1, userID);
 			ResultSet likedResultSet = myLikedStatement.executeQuery();
 			//3
 			myConnection = myDB.getConnection("babble");	//SELECT b.text,b.created,b.creator,b.id,lb.babble, count(lb.babble) AS likes FROM babble b, LikesBabble lb WHERE b.id = lb.babble AND lb.type = 'like' AND b.creator = ? GROUP BY  b.text,b.created,b.creator,b.id,lb.babble ORDER BY b.id DESC
 			PreparedStatement myRebabbleStatement = myConnection.prepareStatement("SELECT b.text,b.created,b.creator,b.id FROM babble b, rebabble rb WHERE rb.babble=b.id AND rb.username = ? ORDER BY b.created DESC");
 			myRebabbleStatement.setString(1, userID);
 			ResultSet rebabbleResultSet = myRebabbleStatement.executeQuery();
 			
 			
 			
 			request.setAttribute("babblelist", utility.createMetaData(myConnection, resultSet));
 			request.setAttribute("babblelist2", utility.createMetaData(myConnection, likedResultSet));
 			request.setAttribute("babblelist3", utility.createMetaData(myConnection, rebabbleResultSet));
 			
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
    	  
        request.getRequestDispatcher("user_profile.ftl").forward(request, response);
    	 
       
      
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                    IOException {

    	Connection myConnection = null;
		DBUtil myDB = null;
    	
    if (request.getParameter("profileLink") != null) {
       userID = request.getParameter("profileLink");
       doGet(request, response);
       
    }else if (request.getParameter("MyPage") != null) {
       userID = request.getParameter("MyPage");
       doGet(request, response);
       
       //followButton mit switch etc. funktioniert, statt farben mit text
    }else if (request.getParameter("follow") != null){
    	try{
    		myConnection = myDB.getConnection("babble");
    	if(request.getParameter("follow").toString().equals("Follow")){
    		PreparedStatement myInsertStatement =  myConnection.prepareStatement("INSERT INTO follows (follower, followee) VALUES (?,?)");
			myInsertStatement.setString(1, initialUserID);
			myInsertStatement.setString(2, userID);
			myInsertStatement.executeUpdate();
			request.setAttribute("follows", "Dont Follow anymore");
    	}else if(request.getParameter("follow").toString().equals("Dont Follow anymore")){
			PreparedStatement myDeleteStatement = myConnection.prepareStatement("DELETE FROM follows WHERE follower = ? AND followee = ?");
			myDeleteStatement.setString(1, initialUserID);
			myDeleteStatement.setString(2, userID);
			myDeleteStatement.executeUpdate();
			request.setAttribute("follows", "Follow");
    }
    	}catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}finally {
		try {
			myConnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	    doGet(request, response);
	    
    }else if (request.getParameter("block") != null){
    	try{
    		myConnection = myDB.getConnection("babble");
    	if(request.getParameter("block").toString().equals("Block")){
    		PreparedStatement myInsertStatement =  myConnection.prepareStatement("INSERT INTO blocks (blocker, blockee) VALUES (?,?)");
			myInsertStatement.setString(1, initialUserID);
			myInsertStatement.setString(2, userID);
			myInsertStatement.executeUpdate();
			request.setAttribute("blocks", "Dont Block anymore");
    	}else if(request.getParameter("block").toString().equals("Dont Block anymore")){
			PreparedStatement myDeleteStatement = myConnection.prepareStatement("DELETE FROM blocks WHERE blocker = ? AND blockee = ?");
			myDeleteStatement.setString(1, initialUserID);
			myDeleteStatement.setString(2, userID);
			myDeleteStatement.executeUpdate();
			request.setAttribute("blocks", "Block");
    }
    	}catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}finally {
		try {
			myConnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	    doGet(request, response);
    }  
    }
    
    
   }


