CREATE TABLE IF NOT EXISTS word_example (
	example_id int4 NOT NULL,
	word_id int4 NOT NULL,
	CONSTRAINT word_example_pkey PRIMARY KEY (example_id, word_id),
	CONSTRAINT word_fk FOREIGN KEY (word_id) REFERENCES words(id),
	CONSTRAINT example_word_example_fk FOREIGN KEY (example_id) REFERENCES examples(id)
);