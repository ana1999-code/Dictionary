ALTER TABLE IF EXISTS user_favorites
DROP CONSTRAINT word_user_favorites_fk;

ALTER TABLE IF EXISTS user_favorites
DROP CONSTRAINT user_user_favorites_fk;

ALTER TABLE IF EXISTS user_favorites
ADD CONSTRAINT word_user_favorites_fk FOREIGN KEY (word_id) REFERENCES words(id) ON DELETE CASCADE;

ALTER TABLE IF EXISTS user_favorites
ADD CONSTRAINT user_user_favorites_fk FOREIGN KEY (user_id) REFERENCES user_info(id) ON DELETE CASCADE;