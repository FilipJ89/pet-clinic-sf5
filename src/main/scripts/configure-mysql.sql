#For docker image keep below line
#docker run --name mysqldb -p 3306:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -d mysql

#Connect to mysql root user
CREATE DATABASE sfg_dev;
CREATE DATABASE sfg_prod;

#Create accounts
CREATE USER 'dev_user'@'localhost' IDENTIFIED BY 'password';
CREATE USER 'prod_user'@'localhost' IDENTIFIED BY 'password';
#for docker also include:
#CREATE USER 'dev_user'@'%' IDENTIFIED BY 'password';
#CREATE USER 'prod_user'@'%' IDENTIFIED BY 'password';

#Database accesses
GRANT SELECT ON sfg_dev.* to 'dev_user'@'localhost';
GRANT INSERT ON sfg_dev.* to 'dev_user'@'localhost';
GRANT DELETE ON sfg_dev.* to 'dev_user'@'localhost';
GRANT UPDATE ON sfg_dev.* to 'dev_user'@'localhost';
GRANT SELECT ON sfg_prod.* to 'prod_user'@'localhost';
GRANT INSERT ON sfg_prod.* to 'prod_user'@'localhost';
GRANT DELETE ON sfg_prod.* to 'prod_user'@'localhost';
GRANT UPDATE ON sfg_prod.* to 'prod_user'@'localhost';
#for docker also include:
#GRANT SELECT ON sfg_dev.* to 'dev_user'@'%';
#GRANT INSERT ON sfg_dev.* to 'dev_user'@'%';
#GRANT DELETE ON sfg_dev.* to 'dev_user'@'%';
#GRANT UPDATE ON sfg_dev.* to 'dev_user'@'%';
#GRANT SELECT ON sfg_prod.* to 'prod_user'@'%';
#GRANT INSERT ON sfg_prod.* to 'prod_user'@'%';
#GRANT DELETE ON sfg_prod.* to 'prod_user'@'%';
#GRANT UPDATE ON sfg_prod.* to 'prod_user'@'%';