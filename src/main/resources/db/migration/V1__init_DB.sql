drop sequence if exists chat_seq;
create sequence chat_seq start with 1 increment by 1;
drop sequence if exists message_seq;
create sequence message_seq start with 1 increment by 1;
drop sequence if exists user_seq;
create sequence user_seq start with 1 increment by 1;

DROP TABLE IF exists chats CASCADE;
create table chats (id bigint not null, admin_id bigint,
                    message_id bigint, primary key (id));
DROP TABLE IF exists messages CASCADE;
create table messages (id bigint not null, created timestamp(6),
                       text varchar(255), chat_id bigint, sender_id bigint, primary key (id));
DROP TABLE IF exists users CASCADE;
create table users (id bigint not null, login varchar(255),
                    mail varchar(255), password varchar(255), role varchar(255), primary key (id));
DROP TABLE IF exists users_chats CASCADE;
create table users_chats (user_id bigint not null, chat_id bigint not null);

alter table if exists chats add constraint FKgnpfufpa3g6wpjcp5wqdvjlsw foreign key (admin_id) references users;
alter table if exists chats add constraint FK2jwfcfeefgfb6xi9xjni1myy5 foreign key (message_id) references messages;
alter table if exists messages add constraint FK64w44ngcpqp99ptcb9werdfmb foreign key (chat_id) references chats;
alter table if exists messages add constraint FK4ui4nnwntodh6wjvck53dbk9m foreign key (sender_id) references users;
alter table if exists users_chats add constraint FKpnxkruh2u71cnyc8y91s6ydpf foreign key (chat_id) references chats;
alter table if exists users_chats add constraint FKm9idubc8h2nd586vuvands3ti foreign key (user_id) references users;