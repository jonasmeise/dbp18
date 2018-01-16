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
		DBUtil myDB = null;
		
		
		
		//SQL Abfrage für die Persönlichen Daten nach derzeitiger userID
    	
    	try {
			myConnection = myDB.getConnection("babble");
			PreparedStatement myPrepStatement = myConnection.prepareStatement("SELECT foto,username,name,status FROM BabbleUser WHERE username = ?");
			myPrepStatement.setString(1, userID);
			ResultSet resultSet = myPrepStatement.executeQuery();
			
			StringBuffer outFoto = new StringBuffer();
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
				String tempFoto = resultSet.getString("foto");
				outFoto.append(tempFoto);
		}

			request.setAttribute("foto", outFoto.toString() );
			request.setAttribute("username", outUserName.toString());
			request.setAttribute("name", outName.toString());
			request.setAttribute("status", outStatus.toString());
			
			
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
    	//klappt noch nicht ganz? kA wieso tho, bei follow sollte es auch klappen und ist genauso
    	 
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
			if(resultSet==null){
				request.setAttribute("block","You are not blocked!");
		    	request.setAttribute("reason","you are cool");
			}else{
				request.setAttribute("block","You are blocked");
		    	request.setAttribute("reason", outReason.toString());
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
		    	request.setAttribute("follow","You cant follow yourself");
		    } else {
		    	try {
					myConnection = myDB.getConnection("babble");
					PreparedStatement myPrepStatement = myConnection.prepareStatement("SELECT follower, followee FROM follows WHERE follower = ? and followee = ?");
					myPrepStatement.setString(1, initialUserID);
					myPrepStatement.setString(2, userID);
					ResultSet resultSet = myPrepStatement.executeQuery();
					if(resultSet==null){
						request.setAttribute("follow","You dont follow this dude");
					}else{
						request.setAttribute("follow","You follow this dude!");
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
    	 
    	 //SQL-Abfrage für Babbles
    	 
    	 try {
 			myConnection = myDB.getConnection("babble");
 			PreparedStatement myPrepStatement = myConnection.prepareStatement("SELECT text,created,creator FROM babble WHERE creator = ? ORDER BY id DESC");
 			myPrepStatement.setString(1, userID);
 			ResultSet resultSet = myPrepStatement.executeQuery();
 			
 			
 			StringBuffer outCreator = new StringBuffer();
 			StringBuffer outCreated = new StringBuffer();
 			StringBuffer outText = new StringBuffer();
 	
 		while (resultSet.next()){	//lösung für nur 1 babble, ResultSet muss man irgendwie splitten und alle like/retweet tabellen joinen einfach wenn mehrere babbles kommen , ein problem wird nur eventuell auch das in der gui als ganz viele verschiedene babbels auszugeben, im moment nur mit 1 wie gesagt
 				String tempCreator = resultSet.getString("creator");
 				outCreator.append(tempCreator);
 				String tempCreated = resultSet.getString("created");
 				outCreated.append(tempCreated);
 				String tempText = resultSet.getString("text");
 				outText.append(tempText);
 
 				
 		}
 		
 			request.setAttribute("creator",outCreator.toString());
 			request.setAttribute("text",outText.toString());
 			request.setAttribute("created",outCreated.toString());
 			
 			
 			
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
    	 
    	 
    	 

			
			request.setAttribute("userID", userID);
			
			
			
			
			
			    
			    
			   
			    
        request.getRequestDispatcher("user_profile.ftl").forward(request, response);
       
      
    }
    
}


