CREATE TABLE review(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  booking_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  review_date DATE NOT NULL,
  title VARCHAR(255) NULL,
  body VARCHAR(1024) NULL,
  rating INT NOT NULL,

  CONSTRAINT fk_review_booking FOREIGN KEY (booking_id) REFERENCES booking (id),
  CONSTRAINT fk_review_user_id FOREIGN KEY (user_id) REFERENCES user (id)
);
