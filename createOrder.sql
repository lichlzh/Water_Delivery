use water_delivery
if exists (select name from sysobjects where name='createOrder'  and  type='p')
   drop procedure createOrder

go
create procedure createOrder(@ar int,@bu int,@fl int,@ro int,@cnt int)
as
BEGIN
	set xact_abort on
	BEGIN TRANSACTION
	if not exists(SELECT delivererNumber FROM InCharge WHERE area=@ar)
		begin
			RAISERROR('ÔÝÎÞÊ¦¸µ',16,1)
		end
	else
		begin
		declare @num int
		declare @dat datetime
		set @dat=CONVERT(date, GETDATE())
		set @num=-1
		SELECT @num=count FROM OrderCount WHERE date=@dat
		if @num=-1
			begin
				INSERT INTO OrderCount VALUES(0,@dat)
				set @num=0
			end
		set @num=@num+1
		UPDATE OrderCount set count=@num
		declare @ID varchar(30)
		set @ID=convert(varchar(30),@dat)
		if @num<1000 begin set @ID=@ID+'0' end
		if @num<100 begin set @ID=@ID+'0' end
		if @num<10 begin set @ID=@ID+'0' end
		set @ID=@ID+convert(varchar(4),@num)

		INSERT INTO [Order] VALUES(@ID,getdate(),DATEADD(mi,30,getdate()),null)
		declare @dn varchar(8)
		SELECT @dn=delivererNumber FROM InCharge WHERE area=@ar
		INSERT INTO ReceiveOrder VALUES(@ID,@dn)
		INSERT INTO DeliverTo VALUES(@ID,null,@ar,@bu,@fl,@ro,@cnt)
	end
	COMMIT TRANSACTION
END