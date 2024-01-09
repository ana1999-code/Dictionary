ALTER TABLE IF EXISTS word_synonyms
DROP CONSTRAINT word_word_synonyms_fk;

ALTER TABLE IF EXISTS word_synonyms
DROP CONSTRAINT synonym_word_synonym_fk;

ALTER TABLE IF EXISTS word_antonyms
DROP CONSTRAINT antonym_word_antonyms_fk;

ALTER TABLE IF EXISTS word_antonyms
DROP CONSTRAINT word_word_antonyms_fk;

ALTER TABLE IF EXISTS word_synonyms
ADD CONSTRAINT word_word_synonyms_fk FOREIGN KEY(word_id) REFERENCES words(id) ON DELETE CASCADE;

ALTER TABLE IF EXISTS word_synonyms
ADD CONSTRAINT synonym_word_synonym_fk FOREIGN KEY(synonym_id) REFERENCES words(id) ON DELETE CASCADE;

ALTER TABLE IF EXISTS word_antonyms
ADD CONSTRAINT word_word_antonyms_fk FOREIGN KEY(word_id) REFERENCES words(id) ON DELETE CASCADE;

ALTER TABLE IF EXISTS word_antonyms
ADD CONSTRAINT antonym_word_antonyms_fk FOREIGN KEY(antonym_id) REFERENCES words(id) ON DELETE CASCADE;

