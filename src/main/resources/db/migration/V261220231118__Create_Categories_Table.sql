CREATE TABLE IF NOT EXISTS categories (
	id serial4 NOT NULL,
	"name" varchar(255) NOT NULL,
	CONSTRAINT categories_name_key UNIQUE (name),
	CONSTRAINT categories_pkey PRIMARY KEY (id)
);