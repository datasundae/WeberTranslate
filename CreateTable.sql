CREATE TABLE book_sentences (
    id SERIAL PRIMARY KEY,
    page_number INTEGER,
    paragraph_number INTEGER,
    sentence_number INTEGER,
    original_language TEXT,
    translated_language TEXT,
    tags TEXT[],  -- Array for storing multiple tags
    full_text tsvector GENERATED ALWAYS AS (to_tsvector('german', original_language) || to_tsvector('english', translated_language)) STORED  -- For full-text search
);


id SERIAL PRIMARY KEY: Creates an auto-incrementing integer column named id as the primary key for the table.
page_number INTEGER: Stores the page number of the sentence as an integer.
paragraph_number INTEGER: Stores the paragraph number of the sentence as an integer.
sentence_number INTEGER: Stores the sentence number within the paragraph as an integer.
original_language TEXT: Stores the original German sentence as text.
translated_language TEXT: Stores the English translation of the sentence as text.
tags TEXT[]: An array of text strings to store multiple tags associated with the sentence.
full_text tsvector: A special column for full-text search.
GENERATED ALWAYS AS: Automatically generates the tsvector value based on the original_language and translated_language columns.
to_tsvector('german', original_language): Converts the original_language text to a tsvector using the German language configuration.
to_tsvector('english', translated_language): Converts the translated_language text to a tsvector using the English language configuration.
||: Concatenates the two tsvector values.
STORED: Stores the generated tsvector value in the table for efficient searching.
How to use it
Connect to your PostgreSQL database using a tool like psql or a database management tool.
Execute the SQL statement above. This will create the book_sentences table with the specified columns and full-text search capability.
You can then use the Java code provided earlier to insert data into this table.
Full-text search example
SQL
SELECT * FROM book_sentences WHERE full_text @@ to_tsquery('german', 'Sinn')
