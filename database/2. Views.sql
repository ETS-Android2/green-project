## This is the second .sql command that 
## you should run. In this section we will see the 
## main views across the project. 

use greenProject;

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

## the view ;v
select * from readings as r join productionLines as pl on r.fk_productionLine = pl.prCode
where 
timestampdiff(minute, date_time, current_timestamp()) between 1 and 59;

#_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_

# 2- A view that returns the fruits' result, 
# it includes the fields for the device, and the fruit implemented
#


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



