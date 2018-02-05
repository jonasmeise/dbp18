package de.unidue.inf.is.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UtilityClass {



public ArrayList<Babble> createMetaData(Connection myConnection, ResultSet resultSet) throws SQLException{
	ArrayList<Babble> output = new ArrayList<Babble>();
	 while (resultSet.next()){
			String likesString ="SELECT b.id,count(lb.babble) AS likes FROM babble b INNER JOIN likesbabble lb ON b.id=lb.babble WHERE lb.type='like' AND b.id=? GROUP BY b.id ";
			PreparedStatement myLikesStatement = myConnection.prepareStatement(likesString);
			String likeCount = "0";
			String dislikesString ="SELECT b.id,count(lb.babble) AS dislikes FROM babble b INNER JOIN likesbabble lb ON b.id=lb.babble WHERE lb.type='dislike' AND b.id=? GROUP BY b.id ";
			PreparedStatement myDislikesStatement = myConnection.prepareStatement(dislikesString);
			String dislikeCount = "0";
			String rebabbleString ="SELECT rb.babble ,count(rb.babble) AS rebabbles FROM rebabble rb WHERE rb.babble=? GROUP BY rb.babble ";
			PreparedStatement myRebabbleStatement = myConnection.prepareStatement(rebabbleString);
			String rebabbleCount = "0";
			
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
				dislikeCount = dislikesResultSet.getString("dislikes");
			}
			
			while(rebabblesResultSet.next()){
				rebabbleCount = rebabblesResultSet.getString("rebabbles");
			}
			
			output.add(new Babble(resultSet.getString("creator").toString(),resultSet.getString("text").toString(),resultSet.getString("created").toString(),likeCount,dislikeCount,rebabbleCount,resultSet.getString("id")));
		}
		
		return output;
}
}
