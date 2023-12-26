CREATE TABLE IF NOT EXISTS word_synonyms (
	synonym_id int4 NOT NULL,
	word_id int4 NOT NULL,
	CONSTRAINT word_synonyms_pkey PRIMARY KEY (synonym_id, word_id),
	CONSTRAINT word_word_synonyms_fk FOREIGN KEY (word_id) REFERENCES words(id),
	CONSTRAINT synonym_word_synonym_fk FOREIGN KEY (synonym_id) REFERENCES words(id)
);