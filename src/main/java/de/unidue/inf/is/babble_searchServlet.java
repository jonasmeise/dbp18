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
import de.unidue.inf.is.domain.UtilityClass;
import de.unidue.inf.is.stores.UserStore;
import de.unidue.inf.is.utils.DBUtil;

public final class babble_searchServlet extends HttpServlet {

	private UtilityClass utility= null;
    private static final long serialVersionUID = 1L;
    private String searched ="Ich";

    
    //TODO man braucht irgendeinen Fall mit der initialisierung von babblelist, wenn diese noch nicht existiert
    //d.h. wenn man die Website zB zum ersten mal startet ohne davor ein doPost aufzurufen -> Freemarker error
    

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException {
    	
    	Connection myConnection = null;
		DBUtil myDB = null;
		List<Babble> babblelist = new ArrayList<>();
		request.setAttribute("babblelist", babblelist);
		
		try {
 			myConnection = myDB.getConnection("babble");
 			PreparedStatement myPrepStatement = myConnection.prepareStatement("SELECT text,created,creator,id FROM babble WHERE text LIKE ? ORDER BY id DESC");
 			myPrepStatement.setString(1,"%"+searched+"%");
 			ResultSet resultSet = myPrepStatement.executeQuery();
 		
 			request.setAttribute("babblelist", utility.createMetaData(myConnection, resultSet));
 			
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
		
		request.getRequestDispatcher("babble_search.ftl").forward(request, response);
    	
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                    IOException {
    	
 		
    	searched = request.getParameter("search");
    	doGet(request,response);
    }
}

