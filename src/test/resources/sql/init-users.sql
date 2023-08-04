-- Create the admin user (password: admin)
INSERT INTO user(id, email, password, role)
VALUES (1, 'admin@test.com', '$2a$10$yIpIXUl6tkXlhNsVLrhxfOFL8sQChoZmxyCuxExrkge2kkdW4e6K2', 'ADMIN');

-- Create the staff user (password: staff)
INSERT INTO user(id, email, password, role)
VALUES (1, 'admin@test.com', '$2a$10$tkhB/TaixEmRD.gzZFcFvOxkQhnGfddLXddbdltwS.EzJZnCwboJm', 'STAFF');

-- Create the client user (password: client)
INSERT INTO user(id, email, password, role)
VALUES (1, 'admin@test.com', '$2a$10$Wsv67kzmHUaszaST56Se3OEMMjf1q1WNEiv1qluuqPBOO45PYmPA2', 'CLIENT');