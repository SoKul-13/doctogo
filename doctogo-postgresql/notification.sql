-- Table: public.notification

-- DROP TABLE IF EXISTS public.notification;

CREATE TABLE IF NOT EXISTS public.notification
(
    id integer NOT NULL,
    blood_pressure double precision NOT NULL,
    created_date date,
    heart_beat integer,
    latitude double precision,
    longitude double precision,
    notification_type integer,
    oxygen_saturation double precision NOT NULL,
    status integer,
    updated_date date,
    dispatcher_id integer,
    user_id integer NOT NULL,
    CONSTRAINT notification_pkey PRIMARY KEY (id),
    CONSTRAINT fk6qkc8byouu9cnbp7lenyphg88 FOREIGN KEY (dispatcher_id)
        REFERENCES public.dusers (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fktbhumwx0y2sl4r8jpw3q7or6u FOREIGN KEY (user_id)
        REFERENCES public.dusers (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.notification
    OWNER to postgres;