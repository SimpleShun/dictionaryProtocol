CREATE DATABASE dictionary;
CREATE USER "dict"@localhost IDENTIFIED BY "123";
GRANT ALL PRIVILEGES ON dictionary.* to "dict"@localhost;

use dictionary;
create table dictionary(
	word varchar(20) private key,
	value varchar(1024) not null,
);
