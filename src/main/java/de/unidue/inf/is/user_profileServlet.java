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
		String out = "";
    	
    	DBUtil myDB = null;
    	try {
			myConnection = myDB.getConnection("babble");
			PreparedStatement myPrepStatement = myConnection.prepareStatement("SELECT * FROM BabbleUser WHERE username=Foobar");
			resultSet = myPrepStatement.executeQuery();
			

			StringBuffer outb = new StringBuffer();
			while (resultSet.next()) {
				String name = resultSet.getString("username");
				outb.append(name).append("<br>");
			}
			out = outb.toString();
			System.out.println(out);
			
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
    	
			System.out.println(resultSet==null);


			request.setAttribute("userprofilepic", "test");
			request.setAttribute("username", out);
        request.getRequestDispatcher("user_profile.ftl").forward(request, response);
        
        
    }
}
