-- Create the admin user (password: admin)
INSERT INTO user(id, email, password, role)
VALUES (1, 'admin@test.com', '$2a$10$yIpIXUl6tkXlhNsVLrhxfOFL8sQChoZmxyCuxExrkge2kkdW4e6K2', 'ADMIN');