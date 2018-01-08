-- Datenbanken Praktikum WS 2017/18
-- Musterlösung - Create Table-Statements
-- Importieren mit: db2 -vtf create_tables.sql
-- db2 create database babble
-- db2 connect to babble

CREATE TABLE BabbleUser (
  	username VARCHAR(20) NOT NULL,
  	name VARCHAR(50) NOT NULL,
  	status CLOB(1M),
  	foto VARCHAR(100),
  	PRIMARY KEY (username)
);

CREATE TABLE Babble (
  	id SMALLINT NOT NULL GENERATED ALWAYS AS IDENTITY,
  	text CLOB(1M) NOT NULL,
  	created timestamp DEFAULT CURRENT TIMESTAMP, 
  	creator VARCHAR(20) NOT NULL,
  	PRIMARY KEY (id),
  	FOREIGN KEY (creator) REFERENCES BabbleUser(username) ON DELETE CASCADE
);

CREATE TABLE BabbleMessage (
  	id SMALLINT NOT NULL GENERATED ALWAYS AS IDENTITY,
  	text CLOB(1M),
  	created timestamp DEFAULT CURRENT TIMESTAMP, 
	sender VARCHAR(20) NOT NULL,
	recipient VARCHAR(20) NOT NULL,
  	FOREIGN KEY (sender) REFERENCES BabbleUser(username) ON DELETE CASCADE,
	FOREIGN KEY (recipient) REFERENCES BabbleUser(username) ON DELETE CASCADE,
  	PRIMARY KEY (id)
);


CREATE TABLE Follows (
	follower VARCHAR(20) NOT NULL, 
	followee VARCHAR(20) NOT NULL, 
	PRIMARY KEY (follower, followee),
	FOREIGN KEY (follower) REFERENCES BabbleUser(username) ON DELETE CASCADE,
	FOREIGN KEY (followee) REFERENCES BabbleUser(username) ON DELETE CASCADE 
);

CREATE TABLE Blocks (
 	blocker VARCHAR(20) NOT NULL, 
	blockee VARCHAR(20) NOT NULL, 
 	reason CLOB(1M),
 	PRIMARY KEY (blocker, blockee),
	FOREIGN KEY (blocker) REFERENCES BabbleUser(username) ON DELETE CASCADE,
	FOREIGN KEY (blockee) REFERENCES BabbleUser(username) ON DELETE CASCADE
);


CREATE TABLE LikesBabble (
 	user VARCHAR(20) NOT NULL, 
	babble SMALLINT NOT NULL, 
	type CHAR(7) CHECK (type IN ('like','dislike')),
	created timestamp DEFAULT CURRENT TIMESTAMP, 
 	PRIMARY KEY (user, babble),
	FOREIGN KEY (user) REFERENCES BabbleUser(username) ON DELETE CASCADE,
	FOREIGN KEY (babble) REFERENCES Babble(id) ON DELETE CASCADE
);

CREATE TABLE ReBabble (
 	user VARCHAR(20) NOT NULL, 
	babble SMALLINT NOT NULL, 
	created timestamp DEFAULT CURRENT TIMESTAMP, 
 	PRIMARY KEY (user, babble),
	FOREIGN KEY (user) REFERENCES BabbleUser(username) ON DELETE CASCADE,
	FOREIGN KEY (babble) REFERENCES Babble(id) ON DELETE CASCADE
);

CREATE TRIGGER END_Friendship
AFTER INSERT ON blocks
REFERENCING NEW AS neu
FOR EACH ROW MODE DB2SQL
DELETE FROM Follows f
        WHERE (neu.blocker = f.follower AND neu.blockee = f.followee)
	OR (neu.blockee= f.follower AND neu.blocker = f.followee);

