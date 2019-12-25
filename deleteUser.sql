use water_delivery
if exists (select name from sysobjects where name='deleteUser'  and  type='p')
   drop procedure deleteUser
go
create procedure deleteUser(@usr varchar(50))
as
BEGIN
	set xact_abort on
	BEGIN TRANSACTION

	declare @ath int
	set @ath=-1
	SELECT @ath=authority FROM [User] WHERE userName=@usr
	if @ath <> -1
	begin
		if(@ath=1)
			begin
				DELETE FROM Domitory WHERE userName=@usr
			end
		else if(@ath=2)
			begin
				declare @dn varchar(50)
				SELECT @dn=delivererNumber FROM Deliverer WHERE userName=@usr
				
				DELETE FROM Performance WHERE delivererNumber=@dn
				DELETE FROM DeliverTo WHERE orderNumber in (
					SELECT orderNumber FROM ReceiveOrder WHERE delivererNumber=@dn
				)
				DELETE FROM ReceiveOrder WHERE delivererNumber=@dn
				DELETE FROM Deliverer WHERE userName=@usr
				DELETE FROM Performance WHERE delivererNumber=@dn
			end
		DELETE FROM [User] WHERE userName=@usr
	end

	else 
		begin
			RAISERROR('该用户不存在',16,1)
			ROLLBACK TRANSACTION
		end
	COMMIT TRANSACTION
END