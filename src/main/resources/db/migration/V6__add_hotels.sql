CREATE TABLE hotel(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(2048) NOT NULL,
  address_id BIGINT NOT NULL,

  CONSTRAINT fk_hotel_address FOREIGN KEY (address_id) REFERENCES address (id)
);
