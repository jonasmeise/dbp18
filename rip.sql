DROP TABLE BabbleUser;
DROP TABLE Babble;
DROP TABLE BabbleMessage;
DROP TABLE Follows;
DROP TABLE LikesBabble;
DROP TABLE Blocks;
DROP TABLE ReBabble;


CREATE TABLE BabbleUser (
  	username VARCHAR(20) NOT NULL,
  	name VARCHAR(50) NOT NULL,
  	status VARCHAR(280),
  	foto VARCHAR(100),
  	PRIMARY KEY (username)
);

CREATE TABLE Babble (
  	id SMALLINT NOT NULL GENERATED ALWAYS AS IDENTITY,
  	text VARCHAR(280) NOT NULL,
  	created timestamp DEFAULT CURRENT TIMESTAMP, 
  	creator VARCHAR(20) NOT NULL,
  	PRIMARY KEY (id),
  	FOREIGN KEY (creator) REFERENCES BabbleUser(username) ON DELETE CASCADE
);

CREATE TABLE BabbleMessage (
  	id SMALLINT NOT NULL GENERATED ALWAYS AS IDENTITY,
  	text VARCHAR(280),
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
 	username VARCHAR(20) NOT NULL, 
	babble SMALLINT NOT NULL, 
	type CHAR(7) CHECK (type IN ('like','dislike')),
	created timestamp DEFAULT CURRENT TIMESTAMP, 
 	PRIMARY KEY (username, babble),
	FOREIGN KEY (username) REFERENCES BabbleUser(username) ON DELETE CASCADE,
	FOREIGN KEY (babble) REFERENCES Babble(id) ON DELETE CASCADE
);

CREATE TABLE ReBabble (
 	username VARCHAR(20) NOT NULL, 
	babble SMALLINT NOT NULL, 
	created timestamp DEFAULT CURRENT TIMESTAMP, 
 	PRIMARY KEY (username, babble),
	FOREIGN KEY (username) REFERENCES BabbleUser(username) ON DELETE CASCADE,
	FOREIGN KEY (babble) REFERENCES Babble(id) ON DELETE CASCADE
);

INSERT INTO BabbleUser
	(username, name, status) 
	values
	('dbuser','IK','Who laughs last has the highest ping.'),
	('FooBar', 'FooBar', 'If you can’t laugh at yourself, call me… I’ll do it.'),
	('braveman', 'Dave Brave', 'Never give up when you still have something to give. Nothing is really over until the moment you stop trying.'),
	('student_1', 'Student', 'When I’m supposed to be studying, even staring at a wall becomes fun.'),
	('noregret', 'John Wick ', 'Life is too short to worry about stupid things. Have fun. Fall in love. Regret nothing, and don’t let people bring you down.'),
	('wisee', 'Fred Wiseman', 'Speak only when you feel your words are better than silence!')
;

INSERT INTO Babble
	(text, creator)
	values
	('Dies ist mein erster Babble! Endlich kann ich meine Meinung über dieses tolle Datenbanken Praktikum loswerden!! :-)', 'dbuser'),
	('Ich bin so aufgeregt! Bald kann ich meine Datenbanken-Skills demonstrieren !! :-)', 'student_1'),
	('Ich mag Kaffee', 'FooBar')
;

INSERT INTO LikesBabble 
	(username, babble, type)
	values
	('FooBar', 1, 'like'),
	('braveman', 1, 'like'),
	('student_1', 1, 'like'),
	('noregret', 1, 'like'),
	('wisee', 1, 'like'),
	('dbuser', 2, 'like'),
	('FooBar', 3, 'like'),
	('dbuser', 3, 'like'),
	('braveman', 3, 'dislike'),
	('student_1', 3, 'like'),
	('noregret', 3, 'dislike'),
	('wisee', 3, 'like')
;

INSERT INTO ReBabble
	(username, babble)
	values
	('dbuser', 3),
	('student_1', 3)
;

INSERT INTO Follows
	(follower, followee)
	values
	('student_1', 'dbuser'),
	('dbuser', 'student_1'),
	('dbuser', 'FooBar'),
	('student_1', 'FooBar')	
;

INSERT INTO Blocks
	(blocker, blockee)
	values
	('dbuser', 'wisee')
;

INSERT INTO BabbleMessage
	(sender, recipient, text)
	values
	('dbuser', 'student_1', 'Hallo Student. Wie geht es dir? Hast du dich gut vorbereitet?'),
	('student_1', 'dbuser', 'Guten Tag! Aber sicher doch! Datenbanken ist mein liebster Kurs. Ich habe ein sehr gutes Gefühl mit dem Praktikum'),
	('dbuser', 'student_1', 'Schön dies zu hören. Dann lassen wir uns mal bei der Abnahme des Praktikums positiv überraschen')
;

CREATE TRIGGER END_Friendship
AFTER INSERT ON blocks
REFERENCING NEW AS neu
FOR EACH ROW MODE DB2SQL
DELETE FROM Follows f
        WHERE (neu.blocker = f.follower AND neu.blockee = f.followee)
	OR (neu.blockee= f.follower AND neu.blocker = f.followee);
