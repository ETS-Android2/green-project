## This is the second .sql command that 
## you should run. In this section we will see the 
## main views across the project. 

use greenProject;

## Basic views, you know, small views

drop view if exists VW_productionLines;
create view VW_productionLines as
select 
	pl.prCode as code,
    pl.ipAddress as IP,
    pl.description as description,
    pl.lastConnection as lastConnection,
    pl.status as status
from 
	productionLines pl;

drop view if exists VW_fruits;
create view VW_fruits as
select 
	f.fruitName name,
    f.fruitCode code, 
    f.description description
from 
	fruits f;


#_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_

# 1- A view that returns the readings in the last hour.
# this will include the device that the readings are coming for
# this may change into another thing better.

## To manage this consult we will use the
## functions subtime() and timestampdiff(), both of them
## allow us to extract our time 

# substracting the time! (Just one hour)
select subtime(current_timestamp(), '1:0');

# Extracting difference betweentime
select 'Hello there' as response 
where 
timestampdiff(minute, '2021-07-09 08:45:20', current_timestamp()) between 1 and 59 ;

drop view if exists VW_readings_last_hour;
create view VW_readings_last_hour as
select
	f.fruitName fruit,
    r.date_time date,
    r.weight as weight,
	r.R as R,
    r.G as G,
    r.B as B,
    pl.prCode as code,
    pl.ipAddress as IP,
    pl.lastConnection as lastConnection,
    pl.status as status
from 
	readings as r 
    join productionLines as pl on r.fk_productionLine = pl.prCode
    join fruits f on f.fruitCode = r.fk_fruit
where 
	timestampdiff(minute, r.date_time, current_timestamp()) between 0 and 59;

#_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_

# 2- A view that returns the fruits result, 
# it includes the fields for the device, and the fruit implemented
#

drop view if exists VW_fruits_result_today;
create view VW_fruits_result_today as
select 
	f.fruitName fruit,
	fr.day_date as date, 
	fr.acceptedFruits as accepted, 
    fr.rejectedFruits as rejected,
    pl.prCode as code,
    pl.ipAddress as IP,
    pl.lastConnection as lastConnection,
    pl.status as status
    
from 
	fruit_results fr 
	join productionLines pl on fr.fk_productionLine = pl.prCode
	join fruits f on f.fruitCode = fr.fk_fruit
    
where fr.day_date = current_date();

#_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_

# 3- A view that returns the enviroment variables , 
# it includes the fields for the device, and the tempetures
# it will be search in the last FIVE MINUTES!!

drop view if exists VW_enviroment_variable;
create view VW_enviroment_variable as
select 
	pl.prCode code,
    pl.ipAddress IP,
    pl.lastConnection lastConnection,
	pl.status status,
    ev.temperature temperature,
    ev.humidity humidity,
    ev.date_time date
from 
	enviromentVariables ev 
	join productionLines pl on ev.fk_productionLine=pl.prCode
where 
	timestampdiff(minute, date_time, current_timestamp()) between 0 and 5;





