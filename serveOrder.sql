use water_delivery
if exists (select name from sysobjects where name='serveOrder'  and  type='p')
   drop procedure serveOrder
go
create procedure serveOrder(@odn varchar(30))
as
BEGIN
	set xact_abort on
	BEGIN TRANSACTION

	declare @dt datetime
	set @dt=getdate()
	declare @pt datetime
	declare @ar int
	declare @bu int
	declare @fl int
	declare @ro int
	declare @ba int
	SELECT @pt=pridictTime FROM [Order] WHERE orderNumber=@odn
	SELECT @ar=area,@bu=building,@fl=floor,@ro=room,@ba=barrelNumber FROM DeliverTo WHERE orderNumber=@odn
	UPDATE [Order] set endTime=@dt WHERE orderNumber=@odn
	UPDATE DeliverTo set endTime=@dt WHERE orderNumber=@odn
	UPDATE Domitory set barrelNumber=barrelNumber+@ba WHERE area=@ar and building=@bu and floor=@fl and room=@ro
	declare @dn varchar(8)
	SELECT @dn=delivererNumber FROM ReceiveOrder WHERE orderNumber=@odn

	if exists(SELECT * FROM Area WHERE area=@ar)
		begin
			UPDATE Area set barrelNumber=barrelNumber+@ba WHERE area=@ar
		end
	else
		begin
			INSERT INTO Area VALUES(@ar,@ba)
		end
	
	if @dt>@pt
		begin
			UPDATE Performance set accuracy=accuracy*orderCount/(orderCount+1),orderCount=orderCount+1 
			WHERE delivererNumber=@dn
		end
	else 
		begin
			UPDATE Performance set accuracy=(accuracy*orderCount+1)/(orderCount+1),orderCount=orderCount+1 
			WHERE delivererNumber=@dn
		end
	COMMIT TRANSACTION
END