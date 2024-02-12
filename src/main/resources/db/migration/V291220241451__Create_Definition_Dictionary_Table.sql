CREATE TABLE definition_dictionary (
	definition_id int4 NOT NULL,
	dictionary_id int8 NOT NULL,
	CONSTRAINT definition_dictionary_pkey PRIMARY KEY (definition_id, dictionary_id),
	CONSTRAINT definition_definition_dictionary_fk FOREIGN KEY (definition_id) REFERENCES definitions(id) ON DELETE CASCADE,
	CONSTRAINT dictionary_definition_dictionary_fk FOREIGN KEY (dictionary_id) REFERENCES "dictionary"(id) ON DELETE CASCADE
);