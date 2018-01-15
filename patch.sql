-- db2 connect to babble
DROP TABLE ReBabble;
DROP TABLE LikesBabble;

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
