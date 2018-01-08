package de.unidue.inf.is.domain;

public final class User {

    private String firstname;
    private String lastname;
    private String status;
    private String username; 
    private String profilepic; //URL als String oder was wollen die von mir?


    public User() {
    }


    public User(String firstname, String lastname,String status, String username, String profilepic) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.setStatus(status);
        this.setUsername(username);
        this.setProfilepic(profilepic);
        

    }


    public String getFirstname() {
        return firstname;
    }


    public String getLastname() {
        return lastname;
    }


	public String getProfilepic() {
		return profilepic;
	}


	public void setProfilepic(String profilepic) {
		this.profilepic = profilepic;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}

}