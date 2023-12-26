CREATE TABLE IF NOT EXISTS user_achievements (
	achievement_id int4 NOT NULL,
	user_id int4 NOT NULL,
	CONSTRAINT user_achievements_pkey PRIMARY KEY (achievement_id, user_id),
	CONSTRAINT achievement_fk FOREIGN KEY (achievement_id) REFERENCES achievements(id),
	CONSTRAINT user_achievements_fk FOREIGN KEY (user_id) REFERENCES user_info(id)
);