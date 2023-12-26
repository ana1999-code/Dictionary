CREATE TABLE IF NOT EXISTS word_definition (
	definition_id int4 NOT NULL,
	word_id int4 NOT NULL,
	CONSTRAINT word_definition_pkey PRIMARY KEY (definition_id, word_id),
	CONSTRAINT word_fk FOREIGN KEY (word_id) REFERENCES words(id),
	CONSTRAINT definition_word_definition_fk FOREIGN KEY (definition_id) REFERENCES definitions(id)
);