CREATE DATABASE IF NOT EXISTS `domino`
    CHARACTER SET utf8
    COLLATE utf8_general_ci;
USE `domino`;

CREATE TABLE IF NOT EXISTS `domino`.`bones` (
  `id` INT(10) NOT NULL AUTO_INCREMENT,
  `num1` INT(2) NULL,
  `num2` INT(2) NULL,
  PRIMARY KEY (`id`)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `domino`.`sets` (
  `id_set` BIGINT(20) NOT NULL,
  `set_bon` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id_set`)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `domino`.`combs` (
  `id_comb` BIGINT(20) NOT NULL,
  `comb` VARCHAR(255) NOT NULL,
  `id_set` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id_comb`)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

--

INSERT INTO `users` (firstName, lastName, birthday, phone, email, country, city, region) VALUES('test1Name', 'last1Name', '2017-01-01', '0777776611', 'test1@mail.ua', 'country1', 'city1', 'region1');
INSERT INTO `users` (firstName, lastName, birthday, phone, email, country, city, region) VALUES('test2Name', 'last2Name', '2017-02-02', '0777776622', 'test2@mail.ua', 'country2', 'city2', 'region2');
INSERT INTO `users` (firstName, lastName, birthday, phone, email, country, city, region) VALUES('test3Name', 'last3Name', '2017-03-03', '0777776633', 'test3@mail.ua', 'country3', 'city3', 'region3');
INSERT INTO `users` (firstName, lastName, birthday, phone, email, country, city, region) VALUES('test4Name', 'last4Name', '2017-04-04', '0777776644', 'test4@mail.ua', 'country4', 'city4', 'region4');
IN