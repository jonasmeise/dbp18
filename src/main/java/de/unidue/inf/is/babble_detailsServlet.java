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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException {
    	
    	Connection myConnection = null;
		DBUtil myDB = null;
		
    	try {
 			myConnection = myDB.getConnection("babble");	//"SELECT text, created,creator, count(rebabble.babble) AS rebabbles FROM babble JOIN likesbabble ON babble.id=likesbabble.babble JOIN rebabble ON babble.id=rebabble.babble WHERE babble.id = ? ");
 			PreparedStatement myPrepStatement = myConnection.prepareStatement("SELECT text, created,creator, count(babble) AS rebabbles FROM babble , rebabble WHERE id = ? and id=babble");
 			myPrepStatement.setString(1, "3");	//übergebene ID des Babbles aus dem HMTL link=? als beispiel haben wir 3 übergeben.
 			ResultSet resultSet = myPrepStatement.executeQuery();
 			
 			StringBuffer outRebabbles = new StringBuffer();
 			StringBuffer outCreator = new StringBuffer();
 			StringBuffer outCreated = new StringBuffer();
 			StringBuffer outText = new StringBuffer();

 		while (resultSet.next()){	
 				String tempCreator = resultSet.getString("creator");
 				outCreator.append(tempCreator);
 				String tempCreated = resultSet.getString("created");
 				outCreated.append(tempCreated);
 				String tempText = resultSet.getString("text");
 				outText.append(tempText);
 				String tempRebabbles = resultSet.getString("rebabbles");
 				outText.append(tempRebabbles);

 				
 		}
 		
 			request.setAttribute("creator",outCreator.toString());
 			request.setAttribute("text",outText.toString());
 			request.setAttribute("created",outCreated.toString());
 			request.setAttribute("rebabbles",outRebabbles.toString());

 			
 			
 			
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
}
