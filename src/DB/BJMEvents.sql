DROP PROCEDURE IF EXISTS `updateFSReminder`;

-- CREATE PROCEDURE bjm.updateFSReminder()
-- BEGIN 
--  select LAST_FS_REMINDER from bjm.USER
-- END;




select ID,FS_REMINDER,LAST_FS_REMINDER from bjm.USER where current_timestamp()-LAST_FS_REMINDER>=FS_REMINDER;

select ID,datediff(current_timestamp(),LAST_FS_REMINDER) from bjm.USER where datediff(current_timestamp(),LAST_FS_REMINDER)> FS_REMINDER*7;

update bjm.USER set LAST_FS_REMINDER='2020-08-15 21:52:00' where ID=24;

select * from bjm.USER;
    


