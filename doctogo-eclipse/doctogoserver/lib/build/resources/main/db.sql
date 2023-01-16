CREATE TABLE public.dusers
(
    id bigint NOT NULL,
    email varchar(256) NOT NULL,
	firstname varchar(256) NOT NULL,
	lastname varchar(256) NOT NULL,
	phone varchar(12) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE public.dusers
    OWNER to postgres;

CREATE TABLE public.notification
(
    id bigint NOT NULL,
    userid bigint NOT NULL,
	longitude decimal NOT NULL,
	latitude decimal NOT NULL,
	notification_type int NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE public.notification
    OWNER to postgres;
    
select * from dusers;

delete from dusers where id=1;
alter table dusers drop column firstname;
alter table dusers drop column "name";
alter table dusers add column user_name varchar(20) NOT NULL;

insert into dusers ("id", "first_name","last_name", "email", "phone","user_name") 
values (2, 'om','K', 'om@k.com','234234234', 'omk');
    