CREATE TABLE city(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  country_id BIGINT NOT NULL,

  CONSTRAINT fk_city_country FOREIGN KEY (country_id) REFERENCES country (id)
);
