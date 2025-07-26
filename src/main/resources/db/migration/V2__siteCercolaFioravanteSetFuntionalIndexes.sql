USE siteCercolaFioravante;

ALTER TABLE `customer`
ADD COLUMN `name_lower` VARCHAR(255) AS (LOWER(`name`)) VIRTUAL,
ADD COLUMN `surname_lower` VARCHAR(255) AS (LOWER(`surname`)) VIRTUAL;
CREATE INDEX `idx_customer_name_lower` ON `customer` (`name_lower`);
CREATE INDEX `idx_customer_surname_lower` ON `customer` (`surname_lower`);
