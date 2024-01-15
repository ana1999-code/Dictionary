ALTER TABLE IF EXISTS "comments"
DROP CONSTRAINT user_comments_fk;

ALTER TABLE IF EXISTS "comments"
DROP CONSTRAINT word_comments_fk;

ALTER TABLE IF EXISTS "comments"
ADD CONSTRAINT user_comments_fk FOREIGN KEY (commenter_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE IF EXISTS "comments"
ADD CONSTRAINT word_comments_fk FOREIGN KEY (word_id) REFERENCES words(id) ON DELETE CASCADE;