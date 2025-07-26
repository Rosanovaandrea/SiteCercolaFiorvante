USE siteCercolaFioravante;

CREATE TABLE IF NOT EXISTS `customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `phone_number` varchar(10) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `surname` varchar(255) NOT NULL,
  `token_registration` varchar(255) DEFAULT NULL,
  `role` enum('ADMIN','CUSTOMER','CUSTOMER_IN_LOCO') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKrosd2guvs3i1agkplv5n8vu82` (`phone_number`),
  UNIQUE KEY `UKdwk6cx0afu8bs9o4t536v1j5v` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

CREATE TABLE IF NOT EXISTS `day` (
  `date` date NOT NULL,
  `is_available` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

CREATE TABLE IF NOT EXISTS `day_occupied_hour` (
  `day_date` date NOT NULL,
  `occupied_hour` int(11) DEFAULT NULL,
  KEY `FKi1cpfmm34uftitus6jvuxamb` (`day_date`),
  CONSTRAINT `FKi1cpfmm34uftitus6jvuxamb` FOREIGN KEY (`day_date`) REFERENCES `day` (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

CREATE TABLE IF NOT EXISTS `service` (
  `price` double NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(1500) NOT NULL,
  `first_image` varchar(255) DEFAULT NULL,
  `service_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK3fdbfgw6oo8g5ivmiyk7tssmo` (`service_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

CREATE TABLE IF NOT EXISTS `service_images` (
  `service_id` bigint(20) NOT NULL,
  `images` varchar(255) DEFAULT NULL,
  KEY `FK6jv831cqlmjy8y0w4us0xv3pa` (`service_id`),
  CONSTRAINT `FK6jv831cqlmjy8y0w4us0xv3pa` FOREIGN KEY (`service_id`) REFERENCES `service` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

CREATE TABLE IF NOT EXISTS `reservation` (
  `day_date` date NOT NULL,
  `hour` int(11) DEFAULT NULL,
  `is_completed` bit(1) NOT NULL DEFAULT b'0',
  `is_deletable` bit(1) NOT NULL DEFAULT b'1',
  `customer_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `service_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKk425lmwr5eaham9hsiksu6il4` (`day_date`,`hour`),
  KEY `FK41v6ueo0hiran65w8y1cta2c2` (`customer_id`),
  KEY `FKfs48sk5vb72pf2xvjokok8b8i` (`service_id`),
  CONSTRAINT `FK41v6ueo0hiran65w8y1cta2c2` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`),
  CONSTRAINT `FKaw7wj8pxmy8exyrc1vij9dfqq` FOREIGN KEY (`day_date`) REFERENCES `day` (`date`),
  CONSTRAINT `FKfs48sk5vb72pf2xvjokok8b8i` FOREIGN KEY (`service_id`) REFERENCES `service` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
