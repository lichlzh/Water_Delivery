use water_delivery

set xact_abort on

BEGIN TRANSACTION 

alter table Deliverer
add constraint DelivererFK foreign key (userName)
references [User](userName)

alter table DeliverTo
add constraint DeliverToFK foreign key (area)
references Area(area)

alter table Domitory
add constraint DomitoryFK foreign key (area)
references Area(area)

alter table InCharge
add constraint InChargeFK foreign key (area)
references Area(area)

alter table Performance
add constraint PerformaceFK foreign key (delivererNumber)
references Deliverer(delivererNumber)

alter table ReceiveOrder
add constraint ReceiveOrderFK foreign key (delivererNumber)
references Deliverer(delivererNumber)

COMMIT TRANSACTION