use water_delivery
CREATE NONCLUSTERED 
INDEX AreaIndex
ON Area(area)

CREATE NONCLUSTERED 
INDEX DelivererIndex
ON Deliverer(delivererNumber)

CREATE NONCLUSTERED 
INDEX DeliverToIndex1
ON DeliverTo(orderNumber)

CREATE NONCLUSTERED 
INDEX DeliverToIndex2
ON DeliverTo(area)

CREATE NONCLUSTERED 
INDEX DomitoryIndex1
ON Domitory(area)

CREATE NONCLUSTERED 
INDEX DomitoryIndex2
ON Domitory(userName)

CREATE NONCLUSTERED 
INDEX InchargeIndex1
ON Incharge(delivererNumber)

CREATE NONCLUSTERED 
INDEX InchargeIndex2
ON Incharge(area)

CREATE NONCLUSTERED 
INDEX OrderIndex
ON [Order](orderNumber)

CREATE NONCLUSTERED 
INDEX PerformanceIndex
ON Performance(delivererNumber)

CREATE NONCLUSTERED 
INDEX RecieveOrderIndex1
ON ReceiveOrder(orderNumber)

CREATE NONCLUSTERED 
INDEX ReceiveOrderIndex2
ON ReceiveOrder(delivererNumber)

CREATE NONCLUSTERED 
INDEX UserIndex2
ON [User](UserName)
