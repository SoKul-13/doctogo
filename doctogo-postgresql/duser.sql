-- Table: public.dusers

-- DROP TABLE IF EXISTS public.dusers;

CREATE TABLE IF NOT EXISTS public.dusers
(
    id integer NOT NULL,
    created_date date,
    dispatcher_flag boolean NOT NULL,
    email character varying(255) COLLATE pg_catalog."default",
    first_name character varying(255) COLLATE pg_catalog."default",
    helper boolean NOT NULL,
    last_name character varying(255) COLLATE pg_catalog."default",
    latitude double precision,
    longitude double precision,
    password character varying(255) COLLATE pg_catalog."default",
    phone character varying(255) COLLATE pg_catalog."default",
    updated_date date,
    user_name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT dusers_pkey PRIMARY KEY (id),
    CONSTRAINT ukm4tbqkc6tmvpvda78sjxfvigk UNIQUE (email)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.dusers
    OWNER to postgres;