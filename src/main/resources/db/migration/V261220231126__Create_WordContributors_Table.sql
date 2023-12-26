CREATE TABLE IF NOT EXISTS word_contributors (
	user_id int4 NOT NULL,
	word_id int4 NOT NULL,
	CONSTRAINT word_contributors_pkey PRIMARY KEY (user_id, word_id),
	CONSTRAINT word_word_contributors_fk FOREIGN KEY (word_id) REFERENCES words(id),
	CONSTRAINT user_word_contributors_fk FOREIGN KEY (user_id) REFERENCES users(id)
);