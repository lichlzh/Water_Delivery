use water_delivery
if exists (select name from sysobjects where name='queryMost'  and  type='p')
   drop procedure queryMost
go
create procedure queryMost(@from date,@to date)
as
BEGIN
	SELECT R.delivererNumber
	FROM ReceiveOrder R,[Order] O
	WHERE R.orderNumber=O.orderNumber and O.orderTime>=@from and O.orderNumber<=@to and COUNT(R.orderNumber)=mx
	group by delivererNumber
END