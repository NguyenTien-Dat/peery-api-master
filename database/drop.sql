
    alter table if exists attachment 
       drop constraint if exists FKojlabuvuc9xacax2kearuv3wc;

    alter table if exists attachment 
       drop constraint if exists FKbj8rm4iort67j9jp8ibdftkmq;

    alter table if exists bank_accounts 
       drop constraint if exists FK8ngd2pjw12xdt5wasywldwjy3;

    alter table if exists bank_accounts 
       drop constraint if exists FKahrj5m84hfc167gpma9vcwe0j;

    alter table if exists contracts 
       drop constraint if exists FK1w4vy90j0syoh0y1ysq37oukr;

    alter table if exists contracts 
       drop constraint if exists FKi3pmya6yiky3m2d5t4047gitn;

    alter table if exists contracts 
       drop constraint if exists FKqwglw36dsgp53d5nhuycxyjp3;

    alter table if exists contracts 
       drop constraint if exists FKb7hg3vkp7b4hg646u1u4oclq8;

    alter table if exists contracts 
       drop constraint if exists FKq10o4jo7bsr7xxegfknvn79eh;

    alter table if exists installment 
       drop constraint if exists FKbqcol4d8owkr8yvo1wf6ecda5;

    alter table if exists installment 
       drop constraint if exists FKim1hyvsw9jxj38ytd6p6pdc05;

    alter table if exists payments 
       drop constraint if exists FKqywegtqyijw241foqfkseq1l6;

    alter table if exists payments 
       drop constraint if exists FK1yiitpqbu2860a7cvurm96ndc;

    alter table if exists payments 
       drop constraint if exists FKj94hgy9v5fw1munb90tar2eje;

    alter table if exists requests 
       drop constraint if exists FKfb0i0kt7gkp1r18bnddogwg91;

    alter table if exists requests 
       drop constraint if exists FKxrm17d1pwutwvmgr6jm8h57u;

    alter table if exists requests 
       drop constraint if exists FK3mguim7wp8pqe8fw0lqpnb815;

    alter table if exists users 
       drop constraint if exists FKp56c1712k691lhsyewcssf40f;

    alter table if exists users 
       drop constraint if exists FK754d1ybe7r1wmk892xb9sfi1v;

    drop table if exists account_statuses cascade;

    drop table if exists attachment cascade;

    drop table if exists attachment_type cascade;

    drop table if exists bank_accounts cascade;

    drop table if exists banks cascade;

    drop table if exists contract_statuses cascade;

    drop table if exists contracts cascade;

    drop table if exists installment cascade;

    drop table if exists payment_status cascade;

    drop table if exists payment_method cascade;

    drop table if exists payments cascade;

    drop table if exists request_status cascade;

    drop table if exists requests cascade;

    drop table if exists roles cascade;

    drop table if exists setting cascade;

    drop table if exists terms cascade;

    drop table if exists users cascade;

    drop sequence if exists account_statuses_seq;

    drop sequence if exists attachment_type_seq;

    drop sequence if exists bank_accounts_seq;

    drop sequence if exists banks_seq;

    drop sequence if exists contract_statuses_seq;

    drop sequence if exists contracts_seq;

    drop sequence if exists installment_seq;

    drop sequence if exists payment_method_seq;

    drop sequence if exists payment_status_seq;

    drop sequence if exists payments_seq;

    drop sequence if exists request_status_seq;

    drop sequence if exists requests_seq;

    drop sequence if exists roles_seq;

    drop sequence if exists terms_seq;

    drop sequence if exists users_seq;
