delete from team_competition;
delete from usr_team;
delete from usr_registered_team;
delete from team_profession;
delete from team;
delete from registered_team;

insert into team(id, logo_filename, team_name, group_chat_id, usr_id) VALUES
(1, 'teamLogo.png', 'Team A', 'e179e6eb-d67c-4e6f-1945-e752b6d11549', 1),
(2, 'teamLogo.png', 'Train B', 'e179e6eb-d67c-4e6f-1945-e752a6q11549', 2),
(3, 'teamLogo.png', 'FunCo', 'e179g9zb-d67c-4e6f-1945-e752a6q11549', 3);
insert into registered_team(id, group_chat_id, team_name) VALUES
(1, 'e179e6eb-d67c-4e6f-1945-e752b6d11549', 'Team A'),
(2, 'e179e6eb-d67c-4e6f-1945-e752a6q11549', 'Train B'),
(3, 'e179g9zb-d67c-4e6f-1945-e752a6q11549', 'FunCo');
insert into usr_team(usr_id, team_id) values
(1, 1),
(1, 2),
(3, 3),
(4, 3),
(4, 1);
insert into usr_registered_team(usr_id, team_id) values
(1, 1),
(1, 2),
(3, 3),
(4, 3),
(4, 1);
insert into team_profession(team_id, professions) VALUES
(1, 'GAME_DESIGNER'), (1, 'MUSICIAN'), (1, 'TESTER'), (1, 'PROGRAMMER'), (1, 'BACKEND'),
(2, 'PROGRAMMER'),
(3, 'GAME_DESIGNER'), (3, 'MUSICIAN'), (3, 'TESTER'), (3, 'PROGRAMMER'), (3, 'BACKEND'), (3, 'DESIGNER'), (3, 'BIOTECHNOLOGIST');
insert into team_competition(team_id, comp_id) VALUES
(1, 2),
(1, 4),
(2, 1),
(2, 3),
(2, 4);

alter sequence hibernate_sequence restart with 10;