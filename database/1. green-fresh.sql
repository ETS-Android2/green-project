## It's better to run this script first.
## This is the main body of the database,
## in  this section we can appreciate the 
## basic relations.

drop database if exists greenProject;
create database greenProject;
use greenProject;

drop table if exists fruits;
CREATE TABLE fruits (
    fruitCode VARCHAR(3) PRIMARY KEY,
    fruitName VARCHAR(15),
    description VARCHAR(250)
);

drop table if exists productionLines;
CREATE TABLE productionLines (
    prCode CHAR(8) PRIMARY KEY,
    ipAddress VARCHAR(15),
    description VARCHAR(200),
    lastConnection DATETIME,
    status VARCHAR(11),
    CONSTRAINT Check_Status CHECK (status = 'Online' || status = 'Offline')
);

drop table if exists readings;
CREATE TABLE readings (
    readNum INT PRIMARY KEY AUTO_INCREMENT,
    date_time DATETIME,
    weight DOUBLE,
    R INT,
    G INT,
    B INT,
    fk_productionLine CHAR(8),
    fk_fruit VARCHAR(3),
    CONSTRAINT FK_fruit FOREIGN KEY (fk_fruit)
        REFERENCES fruits (fruitCode),
    CONSTRAINT FK_productionLine FOREIGN KEY (fk_productionLine)
        REFERENCES productionLines (prCode),
    CONSTRAINT CK_Colors CHECK (R BETWEEN 0 AND 255
        AND B BETWEEN 0 AND 255
        AND G BETWEEN 0 AND 255)
);

drop table if exists eviromentVariables;
CREATE TABLE eviromentVariables (
    readNum INT PRIMARY KEY AUTO_INCREMENT,
    date_time DATETIME,
    temperature DOUBLE,
    humidity INT,
    fk_productionLine CHAR(8),
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
drop table if exists fruit_requirements;
CREATE TABLE fruit_requirements (
    fk_fruitCode VARCHAR(3) PRIMARY KEY,
    fk_minimum_requirements INT,
    fk_maximum_requirements INT,
    CONSTRAINT FK_fruit_requirement FOREIGN KEY (fk_fruitCode)
        REFERENCES fruits (fruitCode),
    CONSTRAINT FK_minReading FOREIGN KEY (fk_minimum_requirements)
        REFERENCES readings (readNum),
    CONSTRAINT FK_maxReading FOREIGN KEY (fk_maximum_requirements)
        REFERENCES readings (readNum)
);

## This table stores the results regardin to a day.

drop table if exists fruit_results;
CREATE TABLE fruit_results (
    numResult INT PRIMARY KEY,
    fk_fruit VARCHAR(3),
    fk_productionLine CHAR(8),
    day_date DATE,
    acceptedFruits INT,
    rejectedFruits INT,
    CONSTRAINT FK_fruitResult FOREIGN KEY (fk_fruit)
        REFERENCES fruits (fruitCode),
    CONSTRAINT FK_productionLine_results FOREIGN KEY (fk_productionLine)
        REFERENCES productionLines (prCode),
    CONSTRAINT CK_ CHECK (acceptedFruits > 0
        AND rejectedFruits > 0),
    CONSTRAINT UQ_date_unique_result UNIQUE (day_date , fk_productionLine)
);
