create table task (
                      id bigserial not null,
                      assigned_to varchar(10),
                      completed_time timestamp(6),
                      creation_time timestamp(6) not null,
                      description varchar(500) not null,
                      name varchar(50) not null,
                      status varchar(255) not null,
                      primary key (id)
)