package de.unidue.inf.is;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.Babble;
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
    	
    	Connection myConnection = null;
		DBUtil myDB = null;
		List<Babble> babblelist = new ArrayList<>();
		
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
    	
    	//SQL-Abfrage für blocked 
    	
    	 
    	if (userID.equals("FooBar")) {
	    	request.setAttribute("block","You are not blocked");
	    	request.setAttribute("reason", "this is your page idiot");
	    } else {
    	try {
			myConnection = myDB.getConnection("babble");
			PreparedStatement myPrepStatement = myConnection.prepareStatement("SELECT reason FROM blocks WHERE blocker = ? AND blockee = ?");
			myPrepStatement.setString(1, userID);
			myPrepStatement.setString(2, initialUserID);
			ResultSet resultSet = myPrepStatement.executeQuery();
			
			if(resultSet==null){//TODO das hier ist der fehler wahrscheinlich
				request.setAttribute("block","You are not blocked!");
		    	request.setAttribute("reason","you are cool");
			}else{
				request.setAttribute("block","You are blocked");
		    	request.setAttribute("reason", "testreason"); //TODO funktioniert nicht wenn reason leer
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
    	 
    	 
    	 //SQL-Abfrage für Babbles
    	 
    	 try {
 			myConnection = myDB.getConnection("babble");
 			PreparedStatement myPrepStatement = myConnection.prepareStatement("SELECT text,created,creator,id FROM babble WHERE creator = ? ORDER BY id DESC");
 			myPrepStatement.setString(1, userID);
 			ResultSet resultSet = myPrepStatement.executeQuery();
 			
 	
 		while (resultSet.next()){	
 				babblelist.add(new Babble(resultSet.getString("creator").toString(),resultSet.getString("text").toString(),resultSet.getString("created").toString(),0,0,0,resultSet.getString("id"))); //ID klappt nicht zu übergeben
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
    }else if (request.getParameter("follow") != null){
    	try{
    		myConnection = myDB.getConnection("babble");
    		System.out.println(request.getParameter("follow").toString());
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
    } 
    }
    
    
   }


