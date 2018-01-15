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
    	
    	DBUtil myDB = null;
    	try {
			myConnection = myDB.getExternalConnection("babble");
			PreparedStatement myPrepStatement = myDB.prepareSQL(myConnection, "SELECT ? FROM ?");
			myPrepStatement.setString(1, "username");
			myPrepStatement.setString(2, "BabbleUser");
			resultSet = myPrepStatement.executeQuery();
			
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
    	
    	
    	
    	request.setAttribute("username", resultSet);
        request.getRequestDispatcher("user_profile.ftl").forward(request, response);
        
        
    }
}
