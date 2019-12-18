use water_delivery
if  exists  ( select name from sysobjects where  name = 'updUserTrigger'  and  type = 'tr' )
   drop  trigger updUserTrigger
go
create trigger updUserTrigger
on [User] for  update,insert as
declare @usr varchar(50)
declare @pwd varchar(50)
declare @ath int
select @usr=userName from inserted
if len(@usr)<=4 begin
  RAISERROR('ÓÃ»§ÃûÌ«¶Ì',16,1)
  ROLLBACK TRANSACTION
end
select @pwd=[password] from inserted
if len(@pwd)<=4 begin
  RAISERROR('ÃÜÂëÌ«¶Ì',16,1)
  ROLLBACK TRANSACTION
end
