-- ?????ATM
CREATE DATABASE ATM;

-- ?????
USE ATM;

-- ?????
CREATE TABLE users(
id INT PRIMARY KEY AUTO_INCREMENT,
NAME VARCHAR(10) NOT NULL,
PASSWORD VARCHAR(6) NOT NULL,
balance DECIMAL(10,2) NOT NULL DEFAULT 0.00
);

-- ???id?10001????????? ???????????
INSERT INTO users
VALUES(10001,'**','123456',100000000.00);
