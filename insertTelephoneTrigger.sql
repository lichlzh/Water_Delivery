use water_delivery
if  exists  ( select name from sysobjects where  name = 'insertTelephoneTrigger'  and  type = 'tr' )
   drop  trigger insertTelephoneTrigger
go
create trigger insertTelephoneTrigger
on deliverer for  update,insert as
declare @number varchar(50)
SELECT @number=telephone FROM inserted
if len(@number)!=11 begin
  RAISERROR('µç»°ºÅÂëÌ«¶Ì',16,1)
  ROLLBACK TRANSACTION
end
