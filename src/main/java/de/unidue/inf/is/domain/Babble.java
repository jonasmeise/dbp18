package de.unidue.inf.is.domain;

public class Babble {
	
	private String creator="";
	private String text="";
	private String created="";
	private int likes=0;
	private int dislikes=0;
	private int rebabbles=0;
	
public Babble(String creator, String text, String created, int likes, int dislikes, int rebabbles){
	this.creator = creator;
	this.text =text;
	this.created=created;
	this.likes = likes;
	this.dislikes = dislikes;
	this.rebabbles = rebabbles;
}

}
