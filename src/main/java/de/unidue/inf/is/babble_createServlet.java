package de.unidue.inf.is;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.Babble;
import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.stores.UserStore;
import de.unidue.inf.is.utils.DBUtil;

public final class babble_createServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException {
        request.getRequestDispatcher("babble_create.ftl").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                    IOException {
    
    if (request.getParameter("createButton") != null) {
       //SQL mit insert into babble mit eingegebenen text etc.
    	
    	Connection myConnection = null;
		DBUtil myDB = null;
    	
    	try {
			myConnection = myDB.getConnection("babble");
			System.out.println(request.getParameter("textarea"));
			PreparedStatement myPrepStatement = myConnection.prepareStatement("INSERT INTO Babble (text,creator) VALUES (? , 'FooBar')");
			myPrepStatement.setString(1, request.getParameter("textarea"));
			myPrepStatement.executeUpdate();
		}
				
		 catch (SQLException e) {
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
    	response.sendRedirect("./user_profile");
       //doGet(request, response);
    }
   }
}
