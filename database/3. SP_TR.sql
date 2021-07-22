## In this script we have the main Stores procedures
## and some triggers as well.

use greenProject;

#_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_

## Triggers ______ Triggers


## 1.- A trigger used every time 
## we want to insert a new reading we gotta
## search into the relation table between the 
## fruit and the production line

drop trigger if exists TR_insert_readings;
DELIMITER //
create trigger TR_insert_readings before insert on readings
for each row 
begin 
	declare fruit char(3);
    
	select 
		fruit_productionLine.fk_fruit
	from 
		fruit_productionLine
	where 
		fruit_productionLine.fk_productionLine = new.fk_productionLine  
	into fruit;
    
	set new.fk_fruit=fruit; 
   
end //
DELIMITER ;

#_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_

## 2.- A trigger that is activated every time 
## an enviroment variable is inserted, we change 
## the last connection field in the production line table

drop trigger if exists TR_insert_enviromentVariable;
DELIMITER //
create trigger TR_insert_enviromentVariable before insert on enviromentVariables
for each row 
begin 
	update productionLines 
    set lastConnection = current_timestamp()
    where prCode = new.fk_productionLine;
   
end //
DELIMITER ;

#_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_

## Store Procedures ______ Store Procedures

## 1.- Insert a new Production Line

drop procedure if exists SP_insert_productionLine;
DELIMITER //
create procedure SP_insert_productionLine(
	in code char(8),
	in ip varchar(15),
	in _description varchar(200),
	in _status varchar(11)
)
begin 
	
    IF (EXISTS(
			select 
				*
			from 
				productionLines
			where 
				productionLines.prCode = code
		)
	) 
	THEN
	BEGIN
		update productionLines 
		set lastConnection = current_timestamp()
		where prCode = code;
	END;
    ELSE 
    BEGIN
		insert into productionLines 
		(prCode, ipAddress, description, status)
		values
		(code, ip, _description, _status);
	END;
	END IF;
    
end //
DELIMITER ;

call SP_insert_productionLine('Test', '0.0.0.0.', 'Testing the Procedure', 'Online');

#_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_

## 2.- Insert a new reading of Eviroment Variables. 

drop procedure if exists SP_insert_eviromentVariable;
DELIMITER //
create procedure SP_insert_eviromentVariable (
	in productionLine char(8),
    in temp double,
    in hum int
)
begin
	insert into enviromentVariables
    (temperature, humidity, fk_productionLine)
    values 
    (temp, hum, productionLine);
end //
DELIMITER ;

call SP_insert_eviromentVariable('Test', 22.2, 78);



#_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_

## 3.- Insert a new fruit. 

drop procedure if exists SP_insert_fruit;
DELIMITER //
create procedure SP_insert_fruit (
	in code varchar (3),
    in name varchar (15),
    in _description varchar (200),
    in urlImage varchar(250)
)
begin 
	insert into fruits 
    (fruitCode, fruitName, description, urlImage)
    values 
    (code, name, _description, urlImage);
end //
DELIMITER ;

call SP_insert_fruit('BAN', 'Banana', 'es una banana','banana.jpeg');
call SP_insert_fruit('APP', 'Apple', 'es una manzana','manzana.jpeg');


#_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_

## 4.- Insert or update a production line 
## relation between the fruit. 

drop procedure if exists SP_fruit_productionLine_relation;
DELIMITER //
create procedure SP_fruit_productionLine_relation (
	in pl char(8),
    in fruit char(3)
)
begin

	IF (EXISTS(
			select 
				fruit_productionLine.fk_productionLine
			from 
				fruit_productionLine
			where 
				fruit_productionLine.fk_productionLine = pl
		)
	) 
	THEN
	BEGIN
		update fruit_productionLine 
        set fk_fruit = fruit, last_change = current_timestamp()
        where fk_productionLine = pl;
	END;
    ELSE 
    BEGIN
		insert into fruit_productionLine
		(fk_productionLine, fk_fruit)
		values 
		(pl, fruit);
	END;
	END IF;

	
end //
DELIMITER ;

call SP_fruit_productionLine_relation('Test','BAN');
call SP_fruit_productionLine_relation('Test','APP');


#_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_

## 5.- Insert a new reading of the fruits. 

drop procedure if exists SP_insert_fruitReading;
DELIMITER //
create procedure SP_insert_fruitReading (
	in productionLine char(8),
    in _weight double,
    in r int,
    in g int,
    in b int
)
begin
	insert into readings
    (fk_productionLine, weight, R, G, B)
    values 
    (productionLIne, _weight, r, g, b);
end //
DELIMITER ;

call SP_insert_fruitReading
('Test',23.4,5,5,5);

