drop table if exists stock;
create table stock (
code char(8),
date char(8),
open float8,
high float8,
low float8,
close float8,
volumn int,
primary key (code,date)
)