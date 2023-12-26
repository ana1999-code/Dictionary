CREATE TABLE IF NOT EXISTS "comments" (
	commented_at date NULL,
	commenter_id int4 NULL,
	id serial4 NOT NULL,
	word_id int4 NULL,
	"text" varchar(255) NOT NULL,
	CONSTRAINT comments_commenter_id_key UNIQUE (commenter_id),
	CONSTRAINT comments_pkey PRIMARY KEY (id),
	CONSTRAINT user_comments_fk FOREIGN KEY (commenter_id) REFERENCES users(id),
	CONSTRAINT word_comments_fk FOREIGN KEY (word_id) REFERENCES words(id)
);