insert into role(id, role_name, description) VALUES ('5e8e86ba-dcc0-11ec-9d64-0242ac120002','ROLE_EMPLOYEE','Роль рабочего'),
                                                    ('78400e63-770f-4f13-ac6c-aabb2d25629c','ROLE_EMPLOYER','Роль работодателя'),
                                                    ('b57ce502-a550-48d9-ae7f-00be9ec751cc','ROLE_ADMIN','Роль админа');
insert into "user"(id, employee_id, employer_id, login, token_ttl,enable, password, role_id)
values ('b57ce502-a550-48d9-ae7f-00be9ec751cc',null,'7afcccda-dcf7-11ec-9d64-0242ac120002','employer',1300,true,'$2a$12$il2muA83Jprz6d93dUc95eq1XfUAvaCMvmyin/RmqV02R9Px7MKCC','78400e63-770f-4f13-ac6c-aabb2d25629c'),
       ('5e89cc4c-dcfc-11ec-9d64-0242ac120002','82290bf8-dcf8-11ec-9d64-0242ac120002','7afcccda-dcf7-11ec-9d64-0242ac120002','test',1300,true,'$2a$12$il2muA83Jprz6d93dUc95eq1XfUAvaCMvmyin/RmqV02R9Px7MKCC','5e8e86ba-dcc0-11ec-9d64-0242ac120002');