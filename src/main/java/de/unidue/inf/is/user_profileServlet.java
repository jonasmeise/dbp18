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
		    	request.setAttribute("follow","You cant follow yourself");
		    } else {
		    	try {
					myConnection = myDB.getConnection("babble");
					PreparedStatement myPrepStatement = myConnection.prepareStatement("SELECT follower, followee FROM follows WHERE follower = ? and followee = ?");
					myPrepStatement.setString(1, initialUserID);
					myPrepStatement.setString(2, userID);
					ResultSet resultSet = myPrepStatement.executeQuery();
					if(resultSet==null){ //TODO das hier ist der fehler wahrscheinlich
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
 			PreparedStatement myPrepStatement = myConnection.prepareStatement("SELECT text,created,creator,id FROM babble WHERE creator = ? ORDER BY id DESC");
 			myPrepStatement.setString(1, userID);
 			ResultSet resultSet = myPrepStatement.executeQuery();
 			
 	
 		while (resultSet.next()){	//lösung für nur 1 babble, ResultSet muss man irgendwie splitten und alle like/retweet tabellen joinen einfach wenn mehrere babbles kommen , ein problem wird nur eventuell auch das in der gui als ganz viele verschiedene babbels auszugeben, im moment nur mit 1 wie gesagt
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
    
    if (request.getParameter("profileLink") != null) {
       userID = request.getParameter("profileLink");
       doGet(request, response);
    }else if (request.getParameter("MyPage") != null) {
       userID = request.getParameter("MyPage");
       doGet(request, response);
    }else if (request.getParameter("babbleIDLink") != null){
    	
    }else  if (request.getParameter("follow") != null){
    	
    	//TODO SQL für Followbutton reintun und dann wenn nicht insert into follow
    	doGet(request, response);
    }else if (request.getParameter("block") != null){
    	//TODO SQL für Blockbutton reintun und dann wenn nicht insert into blocks
    	doGet(request, response);
    }   
        request.getRequestDispatcher("user_profile.ftl").forward(request, response);
    }
    
   }


