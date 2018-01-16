package de.unidue.inf.is;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.stores.UserStore;
import de.unidue.inf.is.utils.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class user_profileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String initialUserID ="FooBar";	//unser User (wir)
    private String userID="FooBar";		//startseite (beginnend mit uns) und userID der jeweiligen Seiten
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException {
    	
    	Connection myConnection = null;
		String dbUserName = "";
		String dbName = "";		
		String dbStatus = "";
		String dbReason ="";
		Boolean blocked;
		DBUtil myDB = null;
		
		
		//SQL Abfrage für die Persönlichen Daten nach derzeitiger userID
    	
    	try {
			myConnection = myDB.getConnection("babble");
			PreparedStatement myPrepStatement = myConnection.prepareStatement("SELECT username,name,status FROM BabbleUser WHERE username = ?");
			myPrepStatement.setString(1, userID);
			ResultSet resultSet = myPrepStatement.executeQuery();
			
			
			StringBuffer outUserName = new StringBuffer();
			StringBuffer outName = new StringBuffer();
			StringBuffer outStatus = new StringBuffer();
		while (resultSet.next()){
				String tempUserName = resultSet.getString("username");
				outUserName.append(tempUserName);
				String tempName = resultSet.getString("name");
				outName.append(tempName);
				String tempStatus = resultSet.getString("status");
				outStatus.append(tempStatus);
		}
			 
		
			dbUserName = outUserName.toString();
			dbName= outName.toString();
			dbStatus= outStatus.toString();
			
			
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
    	
    	//SQL-Abfrage für blocked //TODO die $reason und $block teile außerhalb der try/catch?
    	
    	if (userID.equals("FooBar")) {
	    	request.setAttribute("block","You are not blocked");
	    	request.setAttribute("reason", "this is your page idiot");
	    } else {
    	try {
			myConnection = myDB.getConnection("babble");
			PreparedStatement myPrepStatement = myConnection.prepareStatement("SELECT reason FROM blocks WHERE blocker = ? and blockee = ?");
			myPrepStatement.setString(1, userID);
			myPrepStatement.setString(2, initialUserID);
			ResultSet resultSet = myPrepStatement.executeQuery();
			StringBuffer outReason = new StringBuffer();
			while (resultSet.next()){
					String tempReason = resultSet.getString("reason");
					outReason.append(tempReason);
			}
			dbReason=outReason.toString();
			if(resultSet==null){
				request.setAttribute("block","");
		    	request.setAttribute("reason","");
			}else{
				request.setAttribute("block","You are blocked");
		    	request.setAttribute("reason", dbReason);
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
		    	request.setAttribute("follow","You cant follow yourself");
		    } else {
		    	try {
					myConnection = myDB.getConnection("babble");
					PreparedStatement myPrepStatement = myConnection.prepareStatement("SELECT  FROM follows WHERE follower = ? and followee = ?");
					myPrepStatement.setString(1, initialUserID);
					myPrepStatement.setString(2, userID);
					ResultSet resultSet = myPrepStatement.executeQuery();
					if(resultSet==null){
						request.setAttribute("follow","You dont follow this dude");
					}else{
						request.setAttribute("block","You follow this dude!");
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

			request.setAttribute("profilepic", "http://gify.com Keepo");
			request.setAttribute("username", dbUserName);
			request.setAttribute("name", dbName);
			request.setAttribute("status", dbStatus);
			request.setAttribute("userID", userID);
			
			
			
			    
			    
			   
			    
        request.getRequestDispatcher("user_profile.ftl").forward(request, response);
       
      
    }
    
}
}
