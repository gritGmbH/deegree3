select * 
from recordfull

INSERT INTO userdefinedqueryableproperties VALUES (7);

INSERT INTO datasets VALUES (7,null,null,'','',null,FALSE,'','', '', null);

insert into recordfull (fk_datasets, format, data)
values (7, 1, 'this is a test...');



--to delete the inserted dataset
delete from recordbrief
where fk_datasets >= 7;

delete from recordfull
where fk_datasets >= 7;

delete from datasets 
where id >= 7;

delete from userdefinedqueryableproperties 
where fk_datasets >= 7;