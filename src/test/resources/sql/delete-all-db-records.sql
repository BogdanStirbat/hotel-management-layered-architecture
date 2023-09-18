SET foreign_key_checks = 0;

DELETE FROM user;
DELETE FROM country;
DELETE FROM city;
DELETE FROM street;
DELETE FROM address;
DELETE FROM hotel;
DELETE FROM room_type;
DELETE FROM image;

SET foreign_key_checks = 1;