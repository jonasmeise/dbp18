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

public final class babble_searchServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private String searched ="";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException {
        request.getRequestDispatcher("babble_search.ftl").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                    IOException {
    
    	searched = request.getParameter("search");
    	Connection myConnection = null;
		DBUtil myDB = null;
		List<Babble> babblelist = new ArrayList<>();
		
		try {
 			myConnection = myDB.getConnection("babble");
 			PreparedStatement myPrepStatement = myConnection.prepareStatement("SELECT text,created,creator FROM babble WHERE text LIKE searched ORDER BY id DESC");
 			myPrepStatement.setString(1, searched);
 			ResultSet resultSet = myPrepStatement.executeQuery();
 			
 	
 		while (resultSet.next()){	//lösung für nur 1 babble, ResultSet muss man irgendwie splitten und alle like/retweet tabellen joinen einfach wenn mehrere babbles kommen , ein problem wird nur eventuell auch das in der gui als ganz viele verschiedene babbels auszugeben, im moment nur mit 1 wie gesagt
 				babblelist.add(new Babble(resultSet.getString("creator").toString(),resultSet.getString("text").toString(),resultSet.getString("created").toString(),0,0,0,"2")); //ID klappt nicht zu übergeben
 				babblelist.add(new Babble("dbuser","guck","fuck",0,0,0,"1"));
 				babblelist.add(new Babble("FooBar","sdgfs","sdf",43,2,0,"2"));
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
 		
     
       
        
       
		request.setAttribute("babblelist", babblelist);
        request.getRequestDispatcher("babble_search.ftl").forward(request, response);
    }
}

