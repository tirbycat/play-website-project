# --- !Ups
DELETE FROM variable;
INSERT INTO variable(name, val) VALUES('CONTENT_PATH_FS', '/srv/content');
INSERT INTO variable(name, val) VALUES('CONTENT_PATH_HTTP', '/content');


# --- !Downs