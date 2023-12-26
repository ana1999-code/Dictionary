CREATE TABLE IF NOT EXISTS users (
	id serial4 NOT NULL,
	registered_at date NULL,
	user_info_id int4 NULL,
	email varchar(255) NOT NULL,
	first_name varchar(255) NOT NULL,
	last_name varchar(255) NOT NULL,
	"password" varchar(255) NOT NULL,
	"role" varchar(255) NOT NULL,
	profile_image bytea NULL,
	CONSTRAINT users_pkey PRIMARY KEY (id),
	CONSTRAINT users_role_check CHECK (((role)::text = ANY ((ARRAY[
	    'ADMIN'::character varying,
	    'EDITOR'::character varying,
	    'TEACHER'::character varying,
	    'LEARNER'::character varying])::text[]))),
	CONSTRAINT users_user_info_id_key UNIQUE (user_info_id),
	CONSTRAINT info_users_fk FOREIGN KEY (user_info_id) REFERENCES user_info(id)
);