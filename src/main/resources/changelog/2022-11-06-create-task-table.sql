create table public.task (
    id UUID primary key,
    md5 varchar(50) not null,
    fileName varchar(50),
    url varchar(100),
    width NUMERIC(5),
    height NUMERIC(5),
    created timestamp
)
