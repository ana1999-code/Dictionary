CREATE TABLE IF NOT EXISTS words (
	category_id int4 NOT NULL,
	id serial4 NOT NULL,
	added_at timestamp(6) NULL,
	"name" varchar(255) NOT NULL,
	CONSTRAINT words_name_key UNIQUE (name),
	CONSTRAINT words_pkey PRIMARY KEY (id),
	CONSTRAINT category_words_fk FOREIGN KEY (category_id) REFERENCES categories(id)
);