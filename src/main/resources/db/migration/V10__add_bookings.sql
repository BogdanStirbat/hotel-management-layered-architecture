CREATE TABLE booking(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  room_type_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  check_in_date DATE NOT NULL,
  check_out_date DATE NOT NULL,

  CONSTRAINT fk_booking_room_type FOREIGN KEY (room_type_id) REFERENCES room_type (id),
  CONSTRAINT fk_booking_user_id FOREIGN KEY (room_type_id) REFERENCES user (id)
);
