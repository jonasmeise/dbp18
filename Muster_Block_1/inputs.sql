-- Datenbanken Praktikum WS 2017/18
-- Musterlösung - Inputs for tables
-- Importieren mit: db2 -vtf inputs.sql

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
	(user, babble, type)
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
	(user, babble)
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

