use greenProject;
select * from devices;
create database greenProject;

devicesCREATE TABLE devices (
    divCode char (8) PRIMARY KEY,
    ipAddress varchar (15),
    description varchar (200),
    lastConection datetime,
    status varchar(11),
  	CONSTRAINT Check_Status CHECK  (status= 'Online' || status= 'Offline')
);


create table readingData(
    readNum int PRIMARY KEY AUTO_INCREMENT,
    dataTime datetime,
    weight double,
    R int,
    G int, 
    B int,
    temperature double,
    humidity int,
    divCode char (8),
    CONSTRAINT FK_device FOREIGN KEY (divCode) REFERENCES devices (divCode),
    CONSTRAINT CK_Limit CHECK( weight > 0  and R > 0 and G > 0 and B > 0
    and temperature > 0 and humidity > 0)
    
);

