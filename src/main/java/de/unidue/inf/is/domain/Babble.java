package de.unidue.inf.is.domain;

public class Babble {
	
	private String creator="";
	private String text="";
	private String created="";
	private String likes="";
	private String dislikes= "";
	private String rebabbles="";
	private String id="";
	
public Babble(String creator, String text, String created, String likes, String dislikes, String rebabbles,String id){
	this.setCreator(creator);
	this.setText(text);
	this.setCreated(created);
	this.setLikes(likes);
	this.setDislikes(dislikes);
	this.setRebabbles(rebabbles);
	this.setId(id);
}


public String getText() {
	return text;
}

public void setText(String text) {
	this.text = text;
}

public String getCreator() {
	return creator;
}

public void setCreator(String creator) {
	this.creator = creator;
}

public String getCreated() {
	return created;
}

public void setCreated(String created) {
	this.created = created;
}

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}


public String getLikes() {
	return likes;
}


public void setLikes(String likes) {
	this.likes = likes;
}


public String getDislikes() {
	return dislikes;
}


public void setDislikes(String dislikes) {
	this.dislikes = dislikes;
}


public String getRebabbles() {
	return rebabbles;
}


public void setRebabbles(String rebabbles) {
	this.rebabbles = rebabbles;
}

}
