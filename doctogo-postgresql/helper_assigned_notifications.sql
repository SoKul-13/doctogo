-- Table: public.helper_assigned_notifications

-- DROP TABLE IF EXISTS public.helper_assigned_notifications;

CREATE TABLE IF NOT EXISTS public.helper_assigned_notifications
(
    helpers_id integer NOT NULL,
    assigned_notifications_id integer NOT NULL,
    CONSTRAINT helper_assigned_notifications_pkey PRIMARY KEY (helpers_id, assigned_notifications_id),
    CONSTRAINT fkpuwhbf1n86mfnbbs4cxaty1a0 FOREIGN KEY (helpers_id)
        REFERENCES public.dusers (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fktd5rrdg36cwxhlg5wsbev5cfw FOREIGN KEY (assigned_notifications_id)
        REFERENCES public.notification (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.helper_assigned_notifications
    OWNER to postgres;