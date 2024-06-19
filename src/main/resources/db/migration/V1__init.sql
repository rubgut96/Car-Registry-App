CREATE TABLE `brand`
(
	`id` INTEGER,
	`name` VARCHAR(100),
	`warranty` INTEGER,
	`country` VARCHAR(100),
	PRIMARY KEY (`id`)
);
CREATE TABLE `car`
(
	`id` INTEGER,
	`brand_id` INTEGER,
	`model` VARCHAR(100),
	`mileage` INTEGER,
	`price` DOUBLE,
	`year` INTEGER,
	`description` VARCHAR(100),
	`fuel_type` VARCHAR(100),
	PRIMARY KEY (`id`),
	FOREIGN KEY (`brand_id`) REFERENCES `brand`(`id`)
);
CREATE TABLE `users`
(
	`id` INTEGER NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(100) NOT NULL,
	`mail` VARCHAR(100) NOT NULL UNIQUE,
	`password` VARCHAR(100) NOT NULL,
	`role` VARCHAR(100) NOT NULL,
	`image` MEDIUMTEXT,
	PRIMARY KEY (`id`)
);