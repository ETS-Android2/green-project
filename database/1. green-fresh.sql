## Its better to run this script first.
## This is the main body of the database,
## in  this section we can appreciate the 
## basic relations.

drop database if exists greenProject;
create database greenProject;
use greenProject;

drop table if exists fruits;
CREATE TABLE fruits (
    fruitCode CHAR(3) PRIMARY KEY,
    fruitName VARCHAR(15) not null,
    description VARCHAR(250),
    urlImage varchar(250)
);

drop table if exists productionLines;
CREATE TABLE productionLines (
    prCode CHAR(8) PRIMARY KEY,
    ipAddress VARCHAR(15) not null,
    description VARCHAR(200),
    lastConnection DATETIME default current_timestamp not null,
    status VARCHAR(11) not null,
    CONSTRAINT Check_Status CHECK (status = 'Online' || status = 'Offline')
);

drop table if exists readings;
CREATE TABLE readings (
    readNum INT PRIMARY KEY AUTO_INCREMENT,
    date_time DATETIME default current_timestamp not null,
    weight DOUBLE not null,
    R INT not null,
    G INT not null,
    B INT not null,
    fk_productionLine CHAR(8) not null,
    fk_fruit VARCHAR(3) not null,
    CONSTRAINT FK_fruit FOREIGN KEY (fk_fruit)
        REFERENCES fruits (fruitCode),
    CONSTRAINT FK_productionLine FOREIGN KEY (fk_productionLine)
        REFERENCES productionLines (prCode),
    CONSTRAINT CK_Colors CHECK (R BETWEEN 0 AND 255
        AND B BETWEEN 0 AND 255
        AND G BETWEEN 0 AND 255)
);

drop table if exists enviromentVariables;
CREATE TABLE enviromentVariables (
    readNum INT PRIMARY KEY AUTO_INCREMENT,
    date_time DATETIME default current_timestamp not null,
    temperature DOUBLE not null,
    humidity INT not null,
    fk_productionLine CHAR(8) not null,
    CONSTRAINT FK_productionLineEnviroment FOREIGN KEY (fk_productionLine)
        REFERENCES productionLines (prCode),
    CONSTRAINT CK_enviromentLimit CHECK (temperature > 0 AND humidity > 0)
);

## The table fruit_requirements has to contain the requirements of
## the selection made. With a trigger, we need to check that 
## the readings are from the correct fruit.


## This table is really tricky,
## we can leave it as it is right now
## or we can use a margin error (This will applied later on)
## The average and the variance, those are the ranges
drop table if exists fruit_requirements;
CREATE TABLE fruit_requirements (
    fk_fruitCode VARCHAR(3) PRIMARY KEY,
    
    r_avg float null,
    g_avg float null,
    b_avg float null,
    
	r_var float null,
    g_var float null,
    b_var float null,
    
     CONSTRAINT CK_avg_var CHECK (r_var BETWEEN 0 AND 255
        AND g_var BETWEEN 0 AND 255
        AND b_var BETWEEN 0 AND 255
        AND r_avg BETWEEN 0 AND 255
        AND g_avg BETWEEN 0 AND 255
        AND b_avg BETWEEN 0 AND 255),
     
    CONSTRAINT FK_fruit_requirement FOREIGN KEY (fk_fruitCode)
        REFERENCES fruits (fruitCode)
);

## This table stores the results regardin to a day.

drop table if exists fruit_results;
CREATE TABLE fruit_results (
    fk_fruit VARCHAR(3) not null,
    fk_productionLine CHAR(8) not null,
    day_date DATE default (current_date) not null,
    acceptedFruits INT not null,
    rejectedFruits INT not null,
    constraint primary key(fk_fruit, day_date),
    CONSTRAINT FK_fruitResult FOREIGN KEY (fk_fruit)
        REFERENCES fruits (fruitCode),
    CONSTRAINT FK_productionLine_results FOREIGN KEY (fk_productionLine)
        REFERENCES productionLines (prCode),
    CONSTRAINT CK_ CHECK (acceptedFruits >= 0
        AND rejectedFruits >= 0)
);

## This table stores the realtion between the production line
## and the fruit that is scanned.

drop table if exists fruit_productionLine;
create table fruit_productionLine (
	fk_fruit VARCHAR(3),
    fk_productionLine CHAR(8),
    last_change datetime default current_timestamp not null,
    CONSTRAINT primary key (fk_fruit, fk_productionLine),
    CONSTRAINT FK_fruit_relation FOREIGN KEY (fk_fruit)
        REFERENCES fruits (fruitCode),
    CONSTRAINT FK_productionLine_relation FOREIGN KEY (fk_productionLine)
        REFERENCES productionLines (prCode)
);