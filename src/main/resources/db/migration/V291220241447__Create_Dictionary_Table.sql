CREATE TABLE "dictionary" (
	id bigserial NOT NULL,
	"name" varchar(255) NOT NULL UNIQUE,
	url varchar(255) NULL,
	CONSTRAINT dictionary_pkey PRIMARY KEY (id)
);