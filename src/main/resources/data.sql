
DROP TABLE OPERATION IF EXISTS;
DROP TABLE ACCOUNT IF EXISTS;
DROP TABLE USER IF EXISTS;

CREATE TABLE USER(
   id long AUTO_INCREMENT PRIMARY KEY,
   first_name varchar(200),
   last_name varchar(200),
   birth_date DATE
);
INSERT INTO USER(first_name, last_name, birth_date ) values (
'abir', 'njeh', '1993-05-28');

CREATE TABLE ACCOUNT(
   id long AUTO_INCREMENT PRIMARY KEY,
   user_id int ,
   balance double ,
   rib long,
   iban varchar(255),
   overdraft double,
   FOREIGN KEY(user_id) REFERENCES USER(id)
);
insert into account(user_id, balance, rib, iban, overdraft) values
(1, 5000, 12255888855, 'FR58885555', 500);

CREATE TABLE OPERATION(
  id long AUTO_INCREMENT PRIMARY KEY,
  type varchar(200) ,
  account_id long,
  date DATETIME ,
  amount double,
  balance double,
  description varchar(1000),
  FOREIGN KEY(account_id) REFERENCES ACCOUNT(id)
);