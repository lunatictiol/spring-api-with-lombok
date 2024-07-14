drop table if exists juice;

drop table if exists customer;

create table juice (
                      id varchar(36) not null,
                      juice_name varchar(50) not null,
                      juice_style smallint not null,
                      created_date datetime(6),
                      price decimal(38,2) not null,
                      quantity_on_hand integer,
                      upc varchar(255) not null,
                      update_date datetime(6),
                      version integer,
                      primary key (id)
) engine=InnoDB;

create table customer (
                          id varchar(36) not null,
                          created_date datetime(6),
                          name varchar(255),
                          update_date datetime(6),
                          version integer,
                          primary key (id)
) engine=InnoDB;