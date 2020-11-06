delete from usr_team_role;
delete from usr_role;
delete from usr;

insert into usr(id, activation_code, active, avatar_filename, email, name, password, sec_name, surname, username) values
(1, null, true, 'default.png', 'wminecraft616@gmail.com', 'Гога', '$2a$08$bgSzfgN9UVrXLMzNodznVOerzznIXTMWyD3qBAygUmg507KJ4F5aC', 'Константинович', 'Жуков', 'mock'),
(2, null, true, 'default.png', 'wminecraft616@gmail.com', 'Гоша', '$2a$08$bgSzfgN9UVrXLMzNodznVOerzznIXTMWyD3qBAygUmg507KJ4F5aC', 'Константинович', 'Жуков', 'gog'),
(3, null, true, 'default.png', 'wminecraft616@gmail.com', 'Жора', '$2a$08$bgSzfgN9UVrXLMzNodznVOerzznIXTMWyD3qBAygUmg507KJ4F5aC', 'Константинович', 'Жуков', 'mockito'),
(4, null, true, 'default.png', 'wminecraft616@gmail.com', 'Егор', '$2a$08$bgSzfgN9UVrXLMzNodznVOerzznIXTMWyD3qBAygUmg507KJ4F5aC', 'Константинович', 'Жуков', 'steam');
insert into usr_role(usr_id, roles) values
(1, 'USER'),
(2, 'USER'), (2, 'PARTICIPANT'),
(3, 'USER'), (3, 'ORGANIZER'),
(4, 'USER'), (4, 'ADMIN'), (4, 'PARTICIPANT'), (4, 'ORGANIZER');
insert into usr_team_role(usr_id, team_roles) values
(1, 'PROGRAMMER'),
(2, 'DESIGNER'), (2, 'BIOTECHNOLOGIST'),
(3, 'INTERIOR_DESIGNER'), (3, 'PROGRAMMER'),
(4, 'GAME_DESIGNER'), (4, 'MUSICIAN'), (4, 'TESTER'), (4, 'PROGRAMMER'), (4, 'BACKEND');

alter sequence hibernate_sequence restart with 10;