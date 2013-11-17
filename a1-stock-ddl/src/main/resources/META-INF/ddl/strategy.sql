drop table if exists strategy;
create table strategy(
code char(8),
buy_date char(8),
buy_price float8,
buy_amount int,
sell_date char(8),
sell_price float8,
sell_amount int,
primary key (code,buy_date)
);