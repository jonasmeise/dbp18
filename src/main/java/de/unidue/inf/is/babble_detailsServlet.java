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

import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.stores.UserStore;
import de.unidue.inf.is.utils.DBUtil;

public final class babble_detailsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String initialUserID = "FooBar";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException {
    	
    	Connection myConnection = null;
		DBUtil myDB = null;
		
		
    	try {
 			myConnection = myDB.getConnection("babble");	//"SELECT text, created,creator, count(rebabble.babble) AS rebabbles FROM babble JOIN likesbabble ON babble.id=likesbabble.babble JOIN rebabble ON babble.id=rebabble.babble WHERE babble.id = ? ");
 			PreparedStatement myPrepStatement = myConnection.prepareStatement("SELECT text, created, creator FROM babble WHERE id = ?");
 			myPrepStatement.setString(1, request.getParameter("babbleIDLink"));	//übergebene ID des Babbles aus dem HMTL link=? als beispiel haben wir 3 übergeben.
 			ResultSet resultSet = myPrepStatement.executeQuery();
 			
 		while (resultSet.next()){	
 			request.setAttribute("text",resultSet.getString("text").toString());
 			request.setAttribute("created",resultSet.getString("created").toString());
 			request.setAttribute("creator",resultSet.getString("creator").toString());
		
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
			
		    if (request.getParameter("likeButton") != null) {
		 			myPrepStatement = myConnection.prepareStatement("INSERT INTO LikesBabble (username, babble, type) VALUES ( ? , ? , 'like')");
		 			myPrepStatement.setString(1, initialUserID);
		 			myPrepStatement.setString(2, request.getParameter("babbleIDLink"));	//übergebene ID des Babbles aus dem HMTL link=? als beispiel haben wir 3 übergeben.
		 			myPrepStatement.executeUpdate();	
		    }
		    if (request.getParameter("dislikeButton") != null) {
		 			myPrepStatement = myConnection.prepareStatement("INSERT INTO LikesBabble (username, babble, type) VALUES ( ? , ? , 'dislike')");
		 			myPrepStatement.setString(1, initialUserID);
		 			myPrepStatement.setString(2, request.getParameter("babbleIDLink"));	//übergebene ID des Babbles aus dem HMTL link=? als beispiel haben wir 3 übergeben.
		 			myPrepStatement.executeUpdate();
		    if (request.getParameter("rebabbleButton") != null) {
		 			myPrepStatement = myConnection.prepareStatement("INSERT INTO ReBabble (username, babble) VALUES ( ? , ? )");
		 			myPrepStatement.setString(1, initialUserID);
		 			myPrepStatement.setString(2, request.getParameter("babbleIDLink"));	//übergebene ID des Babbles aus dem HMTL link=? als beispiel haben wir 3 übergeben.
		 			myPrepStatement.executeUpdate();
		    }
		    if (request.getParameter("deleteButton") != null) {
		 			myPrepStatement = myConnection.prepareStatement("DELETE Babble (id, creator) VALUES ( ? , ?)");
		 			myPrepStatement.setString(1, request.getParameter("babbleIDLink"));	//übergebene ID des Babbles aus dem HMTL link=? als beispiel haben wir 3 übergeben.
		 			myPrepStatement.setString(2, initialUserID);
		 			myPrepStatement.executeUpdate();
		
		     }
		   }   
		    
		    doGet(request, response);
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
