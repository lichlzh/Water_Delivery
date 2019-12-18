use water_delivery
if exists (select name from sysobjects where name='changePassword'  and  type='p')
   drop procedure changePassword
go
create procedure changePassword(@usr varchar(50),@pwd varchar(50))
as
BEGIN
	declare @us int;
	set @us=0
	UPDATE [User] SET password = @pwd,@us=1
	WHERE userName = @usr
	if(@us=0)
		begin
			RAISERROR('用户不存在',16,1)
		end
END