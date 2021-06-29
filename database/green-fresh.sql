drop database if exists greenProject;
create database greenProject;
use greenProject;

drop table if exists devices;
CREATE TABLE devices (
    divCode CHAR(8) PRIMARY KEY,
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
    temperature DOUBLE,
    humidity INT,
    divCode CHAR(8),
    CONSTRAINT FK_device FOREIGN KEY (divCode)
        REFERENCES devices (divCode),
    CONSTRAINT CK_Limit CHECK (weight > 0 AND temperature > 0
        AND humidity > 0),
    CONSTRAINT CK_Colors CHECK (R BETWEEN 0 AND 255
        AND B BETWEEN 0 AND 255
        AND G BETWEEN 0 AND 255)
);

