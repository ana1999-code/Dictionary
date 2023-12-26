CREATE TABLE IF NOT EXISTS definitions (
	id serial4 NOT NULL,
	"text" varchar(255) NOT NULL,
	CONSTRAINT definitions_pkey PRIMARY KEY (id),
	CONSTRAINT definitions_text_key UNIQUE (text)
);