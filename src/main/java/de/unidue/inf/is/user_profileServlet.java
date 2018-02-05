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
    		 //Der hier geht, müssen noch irgendwie sortiert werden, created ist aber echt nur created und nicht liked
    		 String SQLString = "SELECT text,created,creator,id FROM ((SELECT b.text,b.created,b.creator,b.id FROM babble b WHERE b.creator = ? ) UNION ALL (SELECT b.text,b.created,b.creator, b.id FROM babble b INNER JOIN likesBabble lb ON b.id=lb.babble WHERE username = ? ) UNION ALL (SELECT b.text,b.created,b.creator,b.id FROM babble b INNER JOIN rebabble rb ON rb.babble=b.id WHERE rb.username = ?)) ORDER BY created DESC";
    		 //Der geht auch aber ohne likes und rebabbles
    		 String oldString ="SELECT b.text,b.created,b.creator,b.id FROM babble b WHERE b.creator = ?";
    		 //kA wieso das wieder zu lang ist
    		String testString ="SELECT b.text,b.created,b.creator,b.id, count(lb.babble) AS likes, count(rb.babble) AS rebabbles, count(lb2.babble) AS dislikes FROM babble b INNER JOIN likesBabble lb ON lb.babble=b.id INNER JOIN rebabble rb ON rb.babble=b.id INNER JOIN likesBabble lb2 ON lb2.babble=b.id WHERE lb2.type='dislike' AND lb.type = 'like' AND creator= 'FooBar' GROUP BY b.id, b.text,b.created,b.creator";
 			//String likesString ="SELECT b.id,count(lb.babble) AS likes FROM babble b INNER JOIN likesbabble lb ON b.id=lb.babble WHERE lb.type='like'  GROUP BY b.id ";
 			//Stringd
    		myConnection = myDB.getConnection("babble");	
 			PreparedStatement myBabbleStatement = myConnection.prepareStatement(SQLString);
 			//PreparedStatement myLikesStatement = myConnection.prepareStatement(likesString);
 			myBabbleStatement.setString(1, userID);
 			myBabbleStatement.setString(2, userID);
 			myBabbleStatement.setString(3, userID);
 			ResultSet resultSet = myBabbleStatement.executeQuery();
 			
 			/*myConnection = myDB.getConnection("babble");	//SELECT b.text,b.created,b.creator,b.id,lb.babble, count(lb.babble) AS likes FROM babble b, LikesBabble lb WHERE b.id = lb.babble AND lb.type = 'like' AND b.creator = ? GROUP BY  b.text,b.created,b.creator,b.id,lb.babble ORDER BY b.id DESC
 			PreparedStatement myLikedStatement = myConnection.prepareStatement("SELECT b.text,b.created,b.creator,b.id FROM babble b, likesBabble lb WHERE b.id=lb.babble AND username = ? ORDER BY b.created DESC");
 			myLikedStatement.setString(1, userID);
 			ResultSet likedResultSet = myLikedStatement.executeQuery();
 			
 			myConnection = myDB.getConnection("babble");	//SELECT b.text,b.created,b.creator,b.id,lb.babble, count(lb.babble) AS likes FROM babble b, LikesBabble lb WHERE b.id = lb.babble AND lb.type = 'like' AND b.creator = ? GROUP BY  b.text,b.created,b.creator,b.id,lb.babble ORDER BY b.id DESC
 			PreparedStatement myRebabbleStatement = myConnection.prepareStatement("SELECT b.text,b.created,b.creator,b.id FROM babble b, rebabble rb WHERE rb.babble=b.id AND rb.username = ? ORDER BY b.created DESC");
 			myRebabbleStatement.setString(1, userID);
 			ResultSet rebabbleResultSet = myRebabbleStatement.executeQuery();
 			*/
 			
 	
 		while (resultSet.next()){
 		
 			String likesString ="SELECT b.id,count(lb.babble) AS likes FROM babble b INNER JOIN likesbabble lb ON b.id=lb.babble WHERE lb.type='like' AND b.id=? GROUP BY b.id ";
 			PreparedStatement myLikesStatement = myConnection.prepareStatement(likesString);
 			String likeCount = null;
 			String dislikesString ="SELECT b.id,count(lb.babble) AS dislikes FROM babble b INNER JOIN likesbabble lb ON b.id=lb.babble WHERE lb.type='dislike' AND b.id=? GROUP BY b.id ";
 			PreparedStatement myDislikesStatement = myConnection.prepareStatement(dislikesString);
 			String dislikeCount = null;
 			String rebabbleString ="SELECT rb.babble ,count(rb.babble) AS rebabbles FROM rebabbles rb WHERE rb.babble=? GROUP BY rb.babble ";
 			PreparedStatement myRebabbleStatement = myConnection.prepareStatement(rebabbleString);
 			String rebabbleCount = null;
 			
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
 				dislikeCount = likesResultSet.getString("dislikes");
 			}
 			while(rebabblesResultSet.next()){
 				rebabbleCount = likesResultSet.getString("rebabbles");
 			}
 			babblelist.add(new Babble(resultSet.getString("creator").toString(),resultSet.getString("text").toString(),resultSet.getString("created").toString(),likeCount,dislikeCount,rebabbleCount,resultSet.getString("id")));
				request.setAttribute("babblelist", babblelist); 
 		}
 		
 		/*//TODO muss man alles in wieder eine große SQL packen mit UNION oder so 
 		while (rebabbleResultSet.next()){				
 			babblelist.add(new Babble(rebabbleResultSet.getString("creator").toString(),rebabbleResultSet.getString("text").toString(),rebabbleResultSet.getString("created").toString(),"","","",rebabbleResultSet.getString("id"))); //ID klappt nicht zu übergeben
 			request.setAttribute("babblelist", babblelist); 
 		}
 		
 		while (likedResultSet.next()){					
				babblelist.add(new Babble(likedResultSet.getString("creator").toString(),likedResultSet.getString("text").toString(),likedResultSet.getString("created").toString(),"","","",likedResultSet.getString("id"))); //ID klappt nicht zu übergeben
				request.setAttribute("babblelist", babblelist); 
		}*/
 		
 		
 		
 		
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


