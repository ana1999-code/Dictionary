CREATE TABLE IF NOT EXISTS achievements (
	id serial4 NOT NULL,
	number_of_words_required int4 NOT NULL,
	"name" varchar(255) NOT NULL,
	CONSTRAINT achievements_name_key UNIQUE (name),
	CONSTRAINT achievements_number_of_words_required_key UNIQUE (number_of_words_required),
	CONSTRAINT achievements_pkey PRIMARY KEY (id)
);