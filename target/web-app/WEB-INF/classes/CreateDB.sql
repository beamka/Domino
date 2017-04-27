CREATE DATABASE IF NOT EXISTS `domino`
    CHARACTER SET utf8
    COLLATE utf8_general_ci;
USE `domino`;

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

INSERT INTO `domino`.`sets` (id_set, set_bon) VALUES('1', '[<3:3>, <0:6>, <0:3>, <0:2>]');
INSERT INTO `domino`.`combs` (id_comb, comb, id_set) VALUES('1', '[<3:3>, <3:0>, <0:2>]', '1');
INSERT INTO `domino`.`combs` (id_comb, comb, id_set) VALUES('2', '[<0:3>, <3:3>]', '1');
INSERT INTO `domino`.`combs` (id_comb, comb, id_set) VALUES('3', '[<3:0>, <0:6>]', '1');
INSERT INTO `domino`.`combs` (id_comb, comb, id_set) VALUES('4', '[<6:0>, <0:2>]', '1');
INSERT INTO `domino`.`combs` (id_comb, comb, id_set) VALUES('5', '[<3:0>, <0:2>]', '1');
INSERT INTO `domino`.`combs` (id_comb, comb, id_set) VALUES('6', '[<0:6>]', '1');
INSERT INTO `domino`.`combs` (id_comb, comb, id_set) VALUES('7', '[<6:0>, <0:3>, <3:3>]', '1');
INSERT INTO `domino`.`combs` (id_comb, comb, id_set) VALUES('8', '[<0:2>]', '1');
INSERT INTO `domino`.`combs` (id_comb, comb, id_set) VALUES('9', '[<3:3>, <3:0>, <0:6>]', '1');
INSERT INTO `domino`.`combs` (id_comb, comb, id_set) VALUES('10', '[<2:0>, <0:6>]', '1');
INSERT INTO `domino`.`combs` (id_comb, comb, id_set) VALUES('11', '[<2:0>, <0:3>, <3:3>]', '1');
