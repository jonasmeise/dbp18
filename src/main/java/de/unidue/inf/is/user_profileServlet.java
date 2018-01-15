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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException {
    	
    	Connection myConnection = null;
    	ResultSet resultSet = null;
		String dbUserName = "";
		String dbName = "";		
		String dbStatus = "";

    	
    	DBUtil myDB = null;
    	try {
			myConnection = myDB.getConnection("babble");
			PreparedStatement myPrepStatement = myConnection.prepareStatement("SELECT username,name,status FROM BabbleUser WHERE username = ?");
			myPrepStatement.setString(1, "FooBar");
			resultSet = myPrepStatement.executeQuery();
			
			
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
    	
			System.out.println("ResultSet ist leer:" + resultSet==null);


			request.setAttribute("profilepic", "http://gify.com Keepo");
			request.setAttribute("username", dbUserName);
			request.setAttribute("name", dbName);
			request.setAttribute("status", dbStatus);
        request.getRequestDispatcher("user_profile.ftl").forward(request, response);
       
      
    }
    
}
