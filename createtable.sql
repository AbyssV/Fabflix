DROP   DATABASE   IF   EXISTS   `moviedb`;
CREATE   DATABASE   `moviedb`;
USE   `moviedb`;

CREATE TABLE movies(
id VARCHAR(10),
title VARCHAR(100)NOT NULL,
year INTEGER NOT NULL,
director VARCHAR(100)NOT NULL,
PRIMARY KEY(ID)
);

CREATE TABLE stars(
id VARCHAR(10),
name VARCHAR(100)NOT NULL,
birthYear INTEGER,
PRIMARY KEY(ID)
);

CREATE TABLE stars_in_movie(
starId VARCHAR(10) NOT NULL,
movieId VARCHAR(10) NOT NULL,
-- do i need primary key?????
-- do i need not null????
FOREIGN KEY(starId) REFERENCES stars(id),
FOREIGN KEY(movieId) REFERENCES movies(id)
-- on delete cascade????????
);

CREATE TABLE genres(
id INTEGER AUTO_INCREMENT, -- syntax???????
name VARCHAR(32) NOT NULL,
PRIMARY KEY(ID)
);

CREATE TABLE genres_in_movies(
genreId INTEGER ,
movieId VARCHAR(10),
FOREIGN KEY(movieId) REFERENCES movies(id),
FOREIGN KEY(genreId) REFERENCES genres(id)
);


CREATE TABLE creditcards(
id VARCHAR(20),
firstName VARCHAR(50)NOT NULL,
lastName VARCHAR(50)NOT NULL,
expiration DATE,
PRIMARY KEY(id)
);


CREATE TABLE customers(
id INTEGER AUTO_INCREMENT,
firstName VARCHAR(50)NOT NULL,
lastName VARCHAR(50)NOT NULL,
ccId VARCHAR(20)NOT NULL,
address VARCHAR(200)NOT NULL,
email VARCHAR(50)NOT NULL,
password VARCHAR(20)NOT NULL,
PRIMARY KEY(id),
FOREIGN KEY(ccId) REFERENCES creditcards(id)
);

-- relationship
CREATE TABLE sales(
id INTEGER AUTO_INCREMENT,
customerId INTEGER NOT NULL,
movieId VARCHAR(10)NOT NULL,
saleDate DATE,
PRIMARY KEY(id),
FOREIGN KEY(customerId) REFERENCES customers(id),
FOREIGN KEY(movieId) REFERENCES movies(id)
);




CREATE TABLE ratings(
movieId VARCHAR(10),
rating FLOAT NOT NULL,
numVotes INTEGER NOT NULL,
FOREIGN KEY(movieId) REFERENCES movies(id)
);



