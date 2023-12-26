CREATE TABLE IF NOT EXISTS word_antonyms (
	antonym_id int4 NOT NULL,
	word_id int4 NOT NULL,
	CONSTRAINT word_antonyms_pkey PRIMARY KEY (antonym_id, word_id),
	CONSTRAINT antonym_word_antonyms_fk FOREIGN KEY (antonym_id) REFERENCES words(id),
	CONSTRAINT word_word_antonyms_fk FOREIGN KEY (word_id) REFERENCES words(id)
);