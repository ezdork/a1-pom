drop table if exists stock_name;
create table stock_name(
code char(8),
name char(20),
kind int,
primary key (code)
);