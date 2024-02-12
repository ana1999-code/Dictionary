CREATE TABLE word_dictionary (
	word_id int4 NOT NULL,
	dictionary_id int8 NOT NULL,
	CONSTRAINT word_dictionary_pkey PRIMARY KEY (word_id, dictionary_id),
	CONSTRAINT dictionary_word_dictionary_fk FOREIGN KEY (dictionary_id) REFERENCES "dictionary"(id) ON DELETE CASCADE,
	CONSTRAINT word_word_dictionary_fk FOREIGN KEY (word_id) REFERENCES words(id) ON DELETE CASCADE
);