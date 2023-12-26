CREATE TABLE IF NOT EXISTS examples (
	id serial4 NOT NULL,
	"text" varchar(255) NOT NULL,
	CONSTRAINT examples_pkey PRIMARY KEY (id),
	CONSTRAINT examples_text_key UNIQUE (text)
);