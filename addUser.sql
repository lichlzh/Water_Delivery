use water_delivery
if exists (select name from sysobjects where name='addUser'  and  type='p')
   drop procedure addUser
go
create procedure addUser(@usr varchar(50),@pwd varchar(50),@ath int,@status int)
as
BEGIN
	set xact_abort on
	BEGIN TRANSACTION
	if @status=3 or (@status=4 and @ath<=2) 
		begin
			insert into [User] VALUES (@usr,@pwd,@ath)
			if @ath=1
				begin
					INSERT INTO Domitory VALUES(0,0,0,0,0,@usr)
				end

			if @ath=2
				begin
					declare @num int
					declare @yer int
					SELECT @yer=year(getdate())
					set @num=-1
					SELECT @num=count FROM DelivererCount WHERE year=@yer
					if @num=-1
						begin
							INSERT INTO DelivererCount VALUES(0,@yer)
							set @num=0
						end
					set @num=@num+1
					UPDATE DelivererCount set count=@num
					declare @ID varchar(8)
					set @ID=convert(varchar,@yer)
					if @num<1000 begin set @ID=@ID+'0' end
					if @num<100 begin set @ID=@ID+'0' end
					if @num<10 begin set @ID=@ID+'0' end
					set @ID=@ID+convert(varchar,@num)

					INSERT INTO Deliverer VALUES(@ID,null,null,@usr)
					INSERT INTO InCharge VALUES(@ID,0)
					INSERT INTO Performance VALUES(@ID,0,0)
				end

		end
	else
		RAISERROR('无权限',16,1)
	COMMIT TRANSACTION
END