-- create database

CREATE SCHEMA qr_entry_control;

CREATE USER qr_entry_admin WITH password 'squd';

ALTER USER qr_entry_admin WITH SUPERUSER;

GRANT USAGE ON SCHEMA qr_entry_control TO qr_entry_admin;

ALTER SCHEMA qr_entry_control OWNER TO qr_entry_admin;
