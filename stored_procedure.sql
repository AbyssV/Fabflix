use moviedb;

DELIMITER $$

DROP PROCEDURE IF EXISTS add_movie$$

CREATE PROCEDURE add_movie(IN title_input VARCHAR(100), IN year_input INTEGER, IN director_input VARCHAR(100),
IN name_input VARCHAR(100), IN birthYear_input INTEGER,
IN genre_name_input VARCHAR(32),
OUT result VARCHAR(1000))

BEGIN

declare count_movie int;
declare count_star int;
declare count_genre int;
declare new_movie_id varchar(10);
declare movie_id varchar(10);
declare new_star_id varchar(10);

set count_movie = (select count(*) from movies where movies.title = title_input and movies.year=year_input and movies.director=director_input);
set count_star = (select count(*) from stars where stars.name = name_input);
set count_genre = (select count(*) from genres where genres.name = genre_name_input);

	IF count_movie = 0 THEN -- movie not in database
		-- SET result="Movie NOT IN DATABASE,";
        set new_movie_id = (select concat('tt', (select (SELECT CAST((SELECT SUBSTRING_INDEX((select max(id) from movies),'t',-1)) AS UNSIGNED)) +1)));
		INSERT INTO movies (id, title, year, director) VALUES (new_movie_id, title_input, year_input, director_input);
        SET result="Sucessfully add new movie.";
        
        IF (count_star = 0) THEN
            -- SET result=concat(result," Star Not IN DATABASE  ");
            set new_star_id = (select concat('nm', (select (SELECT CAST((SELECT SUBSTRING_INDEX((select max(id) from stars),'m',-1)) AS UNSIGNED)) +1)));
            INSERT INTO stars (id, name, birthYear) VALUES (new_star_id, name_input, birthYear_input);
			SET result=concat(result," NEW STAR ADDED,  ");
			-- PROCEED TO build relation between the new movie and the NEW star!! 

            INSERT INTO stars_in_movies(starId, movieId) values(new_star_id, new_movie_id);
			SET result=concat(result," new STAR and new movie relation added!!  ");

           
		ELSE
			-- SET result=concat(result," Star ALREADY IN DATABASE, Proceed to build relation between the new movie and the existing star!!  ");

			INSERT INTO stars_in_movies(starId, movieId) values((select id from stars where name = name_input), new_movie_id);
			SET result=concat(result," existing STAR and new movie relation added!!");

		
        END IF;
        
        IF (count_genre = 0) THEN
        
			-- SELECT CONCAT(result, "Genre Not IN DATABASE, PROCEED TO ADD A NEW Genre!!!!") as result;
            INSERT INTO genres (name) VALUES (genre_name_input);
            SET result=concat(result," NEW GENRE ADDED");
            -- , PROCEED TO build relation between the new movie and the NEW GENRE!!
            
            insert into genres_in_movies(genreId, movieId) values((select id from genres where name = genre_name_input), new_movie_id);
			SET result=concat(result," new GENRE and new movie relation added!!");

           
		ELSE
			-- SELECT CONCAT(result, "Genre ALREADY IN DATABASE, Proceed to build relation between the new movie and the existing genre!!") as result;
            insert into genres_in_movies(genreId, movieId) values((select id from genres where name = genre_name_input), new_movie_id);
			SET result=concat(result," existing GENRE and new movie relation added!!");

           
		END IF;
        
        -- set result= 1;
	-- /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     ELSE
	   -- set result = 0;
       -- SELECT CONCAT("MOVIE ALREADY IN DATABASE, EXITING PROCEDURE!!") as result;
        SET result="MOVIE ALREADY IN DATABASE";
	END IF;
END$$


DELIMITER ;
-- call add_movie('sb3', 2018, 'wjkh', 'yyr', 1996, 'action','');