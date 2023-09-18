CREATE TABLE room_type(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(2048) NOT NULL,
  number_of_available_rooms INT NOT NULL,
  hotel_id BIGINT NOT NULL,

  CONSTRAINT fk_room_type_hotel FOREIGN KEY (hotel_id) REFERENCES hotel (id)
);
