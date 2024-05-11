
    create sequence account_statuses_seq start with 1 increment by 50;

    create sequence attachment_type_seq start with 1 increment by 50;

    create sequence bank_accounts_seq start with 1 increment by 50;

    create sequence banks_seq start with 1 increment by 50;

    create sequence contract_statuses_seq start with 1 increment by 50;

    create sequence contracts_seq start with 1 increment by 50;

    create sequence installment_seq start with 1 increment by 50;

    create sequence payment_method_seq start with 1 increment by 50;

    create sequence payment_status_seq start with 1 increment by 50;

    create sequence payments_seq start with 1 increment by 50;

    create sequence request_status_seq start with 1 increment by 50;

    create sequence requests_seq start with 1 increment by 50;

    create sequence roles_seq start with 1 increment by 50;

    create sequence terms_seq start with 1 increment by 50;

    create sequence users_seq start with 1 increment by 50;

    create table account_statuses (
        id bigint not null,
        name varchar(255),
        primary key (id)
    );

    create table attachment (
        type_id bigint not null,
        user_id bigint not null,
        uuid uuid not null,
        description varchar(255),
        primary key (uuid)
    );

    create table attachment_type (
        id bigint not null,
        type varchar(255),
        primary key (id)
    );

    create table bank_accounts (
        bank_id bigint not null,
        id bigint not null,
        user_id bigint,
        account_number varchar(255),
        primary key (id)
    );

    create table banks (
        id bigint not null,
        code varchar(255),
        name varchar(255),
        primary key (id)
    );

    create table contract_statuses (
        id bigint not null,
        name varchar(255),
        primary key (id)
    );

    create table contracts (
        apr numeric(5,4),
        amount_interest bigint,
        amount_principal bigint,
        amount_remaining bigint,
        borrower_id bigint not null,
        close_date timestamp(6),
        created_date timestamp(6),
        id bigint not null,
        lender_id bigint not null,
        request_id bigint not null,
        status_id bigint not null,
        term_id bigint,
        updated_date timestamp(6),
        primary key (id)
    );

    create table installment (
        date date,
        id integer not null,
        payment_method_id integer,
        status smallint check (status between 0 and 2),
        amount bigint,
        payment_id bigint,
        note varchar(255),
        primary key (id)
    );

    comment on column installment.amount is
        'Số tiền trả trong lần thanh toán';

    create table payment_status (
        id bigint not null,
        name varchar(255),
        primary key (id)
    );

    create table payment_method (
        id integer not null,
        name varchar(255),
        primary key (id)
    );

    create table payments (
        payment_num integer,
        type smallint not null check (type between 0 and 1),
        amount_paid bigint,
        contract_id bigint,
        create_date timestamp(6),
        due_date timestamp(6),
        id bigint not null,
        interest_due bigint,
        principal_due bigint,
        status_id bigint not null,
        total_due bigint,
        update_date timestamp(6),
        user_id bigint,
        primary key (id)
    );

    create table request_status (
        id bigint not null,
        name varchar(255),
        primary key (id)
    );

    create table requests (
        apr numeric(5,4),
        amount bigint,
        borrower_id bigint not null,
        created_date timestamp(6),
        id bigint not null,
        status_id bigint not null,
        term_id bigint,
        updated_date timestamp(6),
        note varchar(255),
        primary key (id)
    );

    create table roles (
        id bigint not null,
        name varchar(255) not null,
        primary key (id)
    );

    create table setting (
        description varchar(255),
        key varchar(255) not null,
        value varchar(255),
        primary key (key)
    );

    create table terms (
        id bigint not null,
        number_of_month bigint,
        primary key (id)
    );

    create table users (
        credit_score integer,
        created_date timestamp(6),
        dob timestamp(6) not null,
        id bigint not null,
        role_id bigint,
        status_id bigint,
        updated_date timestamp(6),
        cic_no varchar(12) not null unique,
        phone varchar(31) not null unique,
        password varchar(64) not null,
        email varchar(254) not null unique,
        address varchar(255) not null,
        confirm_token varchar(255),
        full_name varchar(255) not null,
        primary key (id)
    );

    alter table if exists attachment 
       add constraint FKojlabuvuc9xacax2kearuv3wc 
       foreign key (type_id) 
       references attachment_type;

    alter table if exists attachment 
       add constraint FKbj8rm4iort67j9jp8ibdftkmq 
       foreign key (user_id) 
       references users;

    alter table if exists bank_accounts 
       add constraint FK8ngd2pjw12xdt5wasywldwjy3 
       foreign key (bank_id) 
       references banks;

    alter table if exists bank_accounts 
       add constraint FKahrj5m84hfc167gpma9vcwe0j 
       foreign key (user_id) 
       references users;

    alter table if exists contracts 
       add constraint FK1w4vy90j0syoh0y1ysq37oukr 
       foreign key (borrower_id) 
       references users;

    alter table if exists contracts 
       add constraint FKi3pmya6yiky3m2d5t4047gitn 
       foreign key (lender_id) 
       references users;

    alter table if exists contracts 
       add constraint FKqwglw36dsgp53d5nhuycxyjp3 
       foreign key (request_id) 
       references requests;

    alter table if exists contracts 
       add constraint FKb7hg3vkp7b4hg646u1u4oclq8 
       foreign key (status_id) 
       references contract_statuses;

    alter table if exists contracts 
       add constraint FKq10o4jo7bsr7xxegfknvn79eh 
       foreign key (term_id) 
       references terms;

    alter table if exists installment 
       add constraint FKbqcol4d8owkr8yvo1wf6ecda5 
       foreign key (payment_id) 
       references payments;

    alter table if exists installment 
       add constraint FKim1hyvsw9jxj38ytd6p6pdc05 
       foreign key (payment_method_id) 
       references payment_method;

    alter table if exists payments 
       add constraint FKqywegtqyijw241foqfkseq1l6 
       foreign key (contract_id) 
       references contracts;

    alter table if exists payments 
       add constraint FK1yiitpqbu2860a7cvurm96ndc 
       foreign key (status_id) 
       references payment_status;

    alter table if exists payments 
       add constraint FKj94hgy9v5fw1munb90tar2eje 
       foreign key (user_id) 
       references users;

    alter table if exists requests 
       add constraint FKfb0i0kt7gkp1r18bnddogwg91 
       foreign key (borrower_id) 
       references users;

    alter table if exists requests 
       add constraint FKxrm17d1pwutwvmgr6jm8h57u 
       foreign key (status_id) 
       references request_status;

    alter table if exists requests 
       add constraint FK3mguim7wp8pqe8fw0lqpnb815 
       foreign key (term_id) 
       references terms;

    alter table if exists users 
       add constraint FKp56c1712k691lhsyewcssf40f 
       foreign key (role_id) 
       references roles;

    alter table if exists users 
       add constraint FK754d1ybe7r1wmk892xb9sfi1v 
       foreign key (status_id) 
       references account_statuses;
