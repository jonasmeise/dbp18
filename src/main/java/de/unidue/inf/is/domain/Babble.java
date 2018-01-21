package de.unidue.inf.is.domain;

public class Babble {
	
	private String creator="";
	private String text="";
	private String created="";
	private int likes=0;
	private int dislikes=0;
	private int rebabbles=0;
	private int id=0;
	
public Babble(String creator, String text, String created, int likes, int dislikes, int rebabbles){
	this.setCreator(creator);
	this.setText(text);
	this.setCreated(created);
	this.setLikes(likes);
	this.setDislikes(dislikes);
	this.setRebabbles(rebabbles);
	id++;
}

public int getRebabbles() {
	return rebabbles;
}

public void setRebabbles(int rebabbles) {
	this.rebabbles = rebabbles;
}

public int getDislikes() {
	return dislikes;
}

public void setDislikes(int dislikes) {
	this.dislikes = dislikes;
}

public int getLikes() {
	return likes;
}

public void setLikes(int likes) {
	this.likes = likes;
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

public int getId() {
	return id;
}


}
