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
    
    set @r_m=0.0;
	set @g_m=0.0;
	set @b_m=0.0;
    
    set @r_v=0.0;
	set @g_v=0.0;
	set @b_v=0.0;
    
	select r_avg into @r_m from fruit_requirements where fk_fruitCode = fruit;
	select g_avg into @g_m from fruit_requirements where fk_fruitCode = fruit;
	select b_avg into @b_m from fruit_requirements where fk_fruitCode = fruit;
    
	select r_var into @r_v from fruit_requirements where fk_fruitCode = fruit;
	select g_var into @g_v from fruit_requirements where fk_fruitCode = fruit;
	select b_var into @b_v from fruit_requirements where fk_fruitCode = fruit;
    
	IF ( @r_m is not null and @g_m is not null and @b_m is not null and @r_v is not null and @g_v is not null and @b_v is not null) 
	THEN
	BEGIN
    
		if (not EXISTS(Select * from fruit_results where fk_fruit=fruit and day_date = current_date() and fk_productionLine = new.fk_productionLine ))
		then
		begin
			insert into fruit_results 
            (fk_fruit, fk_productionLine, day_date, acceptedFruits, rejectedFruits)
            values (fruit, new.fk_productionLine, current_date(), 0, 0);
		end;
		end if;
    
		if ( (new.R > (@r_m + @r_v) or new.R < (@r_m - @r_v)) or (new.G > (@g_m + @g_v) or new.G < (@g_m - @g_v)) or (new.B > (@b_m + @b_v) or new.B < (@b_m - @b_v)))
        THEN
		BEGIN
			
            update fruit_results set rejectedFruits = (rejectedFruits+1) where fk_fruit=fruit and day_date = current_date() and fk_productionLine = new.fk_productionLine;
            
		END;
        else 
        begin 
			update fruit_results set acceptedFruits = (acceptedFruits+1) where fk_fruit=fruit and day_date = current_date() and fk_productionLine = new.fk_productionLine ;
        end;
		END IF;
	END;
	END IF;
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

## 3- a Trigger that is activated every time a new fruit is
## inserted.

drop trigger if exists TR_insert_fruit;
DELIMITER // 
Create trigger TR_insert_fruit after insert on fruits
for each row 
begin 
	insert into fruit_requirements (fk_fruitCode)
    values (new.fruitCode);
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
		set lastConnection = current_timestamp(),
        description = _description,
        ipAddress = ip,
        status = _status
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

#_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_

## 5.- Insert a new reading of the fruits. 


drop procedure if exists SP_insert_fruitRequirements;
DELIMITER //
create procedure SP_insert_fruitRequirements (
	in fruit char(3),
    
    in r_m double,
    in g_m double,
    in b_m double,
    
    in r_v double,
    in g_v double,
    in b_v double
    
)
begin
	update fruit_requirements
    set  r_avg = r_m, g_avg = g_m, b_avg = b_m, r_var = r_v, g_var = g_v, b_var = b_v
    where fk_fruitCode = fruit;
end //
DELIMITER ;


#_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_

## 6.- Insert a new reading of the fruits. 

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

