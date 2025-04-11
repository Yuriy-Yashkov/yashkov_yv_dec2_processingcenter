insert into user_access (user_login, user_password, full_name, user_role)
values ('admin', '{noop}123', 'Admin', 'ADMIN'),
       ('manager', '{noop}123', 'Manager''s full name', 'USER'),
       ('yriy', '$2a$10$CIQGlUroK4ECFlrnTECAVedmp/7kDBTUpzbSIPse0ouo7e1fgLA2G', 'yriy', 'USER'),
       ('yriy1', '$2a$10$uxQmERZBaJluDOenOL654OnPdbhLK8YgvALQNd14E8m8PZml.awJ.', 'yriy1', 'ADMIN');