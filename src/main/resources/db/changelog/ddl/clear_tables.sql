TRUNCATE transaction CASCADE;

TRUNCATE card CASCADE;

TRUNCATE payment_system CASCADE;

TRUNCATE response_code CASCADE;

TRUNCATE transaction_type CASCADE;

TRUNCATE terminal CASCADE;

TRUNCATE merchant_category_code CASCADE;

TRUNCATE sales_point CASCADE;

TRUNCATE acquiring_bank CASCADE;

DELETE
FROM databasechangelog;
