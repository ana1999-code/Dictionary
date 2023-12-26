CREATE TABLE IF NOT EXISTS user_favorites (
	user_id int4 NOT NULL,
	word_id int4 NOT NULL,
	CONSTRAINT user_favorites_pkey PRIMARY KEY (user_id, word_id),
	CONSTRAINT word_user_favorites_fk FOREIGN KEY (word_id) REFERENCES words(id),
	CONSTRAINT user_user_favorites_fk FOREIGN KEY (user_id) REFERENCES user_info(id)
);