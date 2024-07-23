

    drop table if exists category;

    drop table if exists juice_category;


    create table category (
        created_date datetime(6),
        last_modified_date datetime(6),
        version bigint,
        id varchar(36) not null,
        description varchar(255),
        primary key (id)
    ) engine=InnoDB;



    create table juice_category (
        category_id varchar(36) not null,
        juice_id varchar(36) not null,
        primary key (category_id, juice_id),
        constraint pc_juice_id_fk FOREIGN KEY (juice_id) references juice (id),
        constraint pc_category_id_fk FOREIGN KEY (category_id) references category (id)

    ) engine=InnoDB;


