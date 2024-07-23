drop table if exists juice_order_shipment;



    create table juice_order_shipment (
        created_date timestamp,
        last_modified_date datetime(6) default null,
        version bigint default null,
        id varchar(36) not null PRIMARY KEY ,
        juice_order_id varchar(36) UNIQUE ,
        tracking_number varchar(255),
        CONSTRAINT bos_pk FOREIGN KEY (juice_order_id) REFERENCES juice_order (id)

    ) engine=InnoDB;
ALTER TABLE juice_order
    ADD COLUMN juice_order_shipment_id VARCHAR(36);

ALTER TABLE juice_order
    ADD CONSTRAINT bos_shipment_fk
        FOREIGN KEY (juice_order_shipment_id) REFERENCES juice_order_shipment (id);
