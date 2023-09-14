CREATE TABLE address(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  house_number VARCHAR(255) NOT NULL,
  postal_code VARCHAR(255) NOT NULL,
  street_id BIGINT NOT NULL,

  CONSTRAINT fk_address_street FOREIGN KEY (street_id) REFERENCES street (id)
);
