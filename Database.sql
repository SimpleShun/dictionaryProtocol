CREATE DATABASE dictionary;
CREATE DATABASE french;
CREATE USER "dict"@localhost IDENTIFIED BY "123";
GRANT ALL PRIVILEGES ON dictionary.* TO "dict"@localhost;

use dictionary;
create table dictionary(
	word varchar(20) primary key,
	value varchar(1024) not null
);

insert into dictionary values("delusion"," delusion
	1. The act of deluding; deception; a misleading of the mind.
	2. The state of being deluded or misled.
	3. That which is falsely or delusively believed or propagated; false belief; error in belief.\n Syn: Delusion, Illusion.");

insert into dictionary values("intelligence"," intelligence 
     1. The act or state of knowing; the exercise of the understanding.\n
     2. The capacity to know or understand; readiness of comprehension; the intellect, as a gift or an endowment.\n
     3. Information communicated; news; notice; advice.\n
     4. Knowledge imparted or acquired, whether by study, research, or experience; general information. Specifically; (Mil.) Information about an enemy or potential enemy, his capacities, and intentions.\n ");

USE french;
create table dictionary(
	word varchar(20) primary key,
	value varchar(1024) not null
);

