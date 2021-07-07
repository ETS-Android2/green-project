drop database if exists greenProject;
create database greenProject;
use greenProject;

drop table if exists fruits;
CREATE TABLE fruits (
    fruitCode VARCHAR(3) PRIMARY KEY,
    fruitName VARCHAR(15),
    description VARCHAR(250)
);

drop table if exists devices;
CREATE TABLE devices (
    devCode CHAR(8) PRIMARY KEY,
    ipAddress VARCHAR(15),
    description VARCHAR(200),
    lastConection DATETIME,
    status VARCHAR(11),
    CONSTRAINT Check_Status CHECK (status = 'Online' || status = 'Offline')
);

drop table if exists readings;
CREATE TABLE readings (
    readNum INT PRIMARY KEY AUTO_INCREMENT,
    dataTime DATETIME,
    weight DOUBLE,
    R INT,
    G INT,
    B INT,
    fk_device CHAR(8),
    fk_fruit VARCHAR(3),
    CONSTRAINT FK_fruit FOREIGN KEY (fk_fruit)
        REFERENCES fruits (fruitCode),
    CONSTRAINT FK_device FOREIGN KEY (fk_device)
        REFERENCES devices (devCode),
    CONSTRAINT CK_Colors CHECK (R BETWEEN 0 AND 255
        AND B BETWEEN 0 AND 255
        AND G BETWEEN 0 AND 255)
);

drop table if exists humidity_temperature_readings;
create table humidity_temperature_readings(
	readNum INT PRIMARY KEY AUTO_INCREMENT,
    dataTime DATETIME,
	temperature DOUBLE,
    humidity INT,
	fk_device CHAR(8),
	CONSTRAINT FK_device FOREIGN KEY (fk_device)
        REFERENCES devices (devCode),
	CONSTRAINT CK_TempHumLimit CHECK ( temperature > 0 
    AND humidity > 0)
);

## The table fruit_requirements has to contain the requirements of
## the selection made. With a trigger, we need to check that 
## the readings are from the correct fruit.
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

