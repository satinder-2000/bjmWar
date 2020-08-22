-- MySQL Script generated by MySQL Workbench
-- Sat Aug 22 22:26:54 2020
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema bjm
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `bjm` ;

-- -----------------------------------------------------
-- Schema bjm
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `bjm` DEFAULT CHARACTER SET utf8 ;
USE `bjm` ;

-- -----------------------------------------------------
-- Table `bjm`.`STATE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bjm`.`STATE` ;

CREATE TABLE IF NOT EXISTS `bjm`.`STATE` (
  `CODE` CHAR(2) NOT NULL,
  `NAME` VARCHAR(40) NOT NULL,
  `POST_CODE_PREFIX` VARCHAR(10) NULL,
  PRIMARY KEY (`CODE`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bjm`.`USER`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bjm`.`USER` ;

CREATE TABLE IF NOT EXISTS `bjm`.`USER` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `FIRSTNAME` VARCHAR(45) NOT NULL,
  `LASTNAME` VARCHAR(45) NOT NULL,
  `EMAIL` VARCHAR(50) NOT NULL,
  `DOB` DATE NOT NULL,
  `STATE_CODE` CHAR(2) NOT NULL,
  `PROFILE_FILE` VARCHAR(45) NOT NULL,
  `IMAGE` BLOB NOT NULL,
  `GENDER` VARCHAR(6) NOT NULL,
  `PHONE` VARCHAR(45) NULL,
  `MOBILE` VARCHAR(45) NULL,
  `PASSWORD` VARCHAR(50) NULL,
  `FAILED_ATTEMPTS` INT NOT NULL DEFAULT 0,
  `CREATED_ON` TIMESTAMP NOT NULL,
  `UPDATED_ON` TIMESTAMP NOT NULL,
  `FS_REMINDER` INT NOT NULL DEFAULT 4,
  `LAST_FS_REMINDER` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  CONSTRAINT `fk_USER_STATE1`
    FOREIGN KEY (`STATE_CODE`)
    REFERENCES `bjm`.`STATE` (`CODE`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_USER_STATE1_idx` ON `bjm`.`USER` (`STATE_CODE` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `bjm`.`SURVEY_CATEGORY`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bjm`.`SURVEY_CATEGORY` ;

CREATE TABLE IF NOT EXISTS `bjm`.`SURVEY_CATEGORY` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `TYPE` VARCHAR(45) NOT NULL,
  `SUBTYPE` VARCHAR(45) NOT NULL,
  `DESCRIPTION` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bjm`.`SURVEY`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bjm`.`SURVEY` ;

CREATE TABLE IF NOT EXISTS `bjm`.`SURVEY` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `TITLE` VARCHAR(125) NOT NULL,
  `DESCRIPTION` VARCHAR(4000) NOT NULL,
  `DATED` TIMESTAMP NOT NULL,
  `USER_ID` INT NOT NULL,
  `SURVEY_CATEGORY_ID` INT NOT NULL,
  PRIMARY KEY (`ID`),
  CONSTRAINT `fk_SURVEY_USER1`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `bjm`.`USER` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SURVEY_SURVEY_CATEGORY1`
    FOREIGN KEY (`SURVEY_CATEGORY_ID`)
    REFERENCES `bjm`.`SURVEY_CATEGORY` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_SURVEY_USER1_idx` ON `bjm`.`SURVEY` (`USER_ID` ASC) VISIBLE;

CREATE INDEX `fk_SURVEY_SURVEY_CATEGORY1_idx` ON `bjm`.`SURVEY` (`SURVEY_CATEGORY_ID` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `bjm`.`FORUM_CATEGORY`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bjm`.`FORUM_CATEGORY` ;

CREATE TABLE IF NOT EXISTS `bjm`.`FORUM_CATEGORY` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `TYPE` VARCHAR(45) NOT NULL,
  `SUBTYPE` VARCHAR(45) NOT NULL,
  `DESCRIPTION` VARCHAR(150) NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bjm`.`FORUM`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bjm`.`FORUM` ;

CREATE TABLE IF NOT EXISTS `bjm`.`FORUM` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `TITLE` VARCHAR(125) NOT NULL,
  `DESCRIPTION` VARCHAR(4000) NOT NULL,
  `CREATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `FORUM_CATEGORY_ID` INT NOT NULL,
  `USER_ID` INT NOT NULL,
  PRIMARY KEY (`ID`),
  CONSTRAINT `fk_FORUM_FORUM_CATEGORY1`
    FOREIGN KEY (`FORUM_CATEGORY_ID`)
    REFERENCES `bjm`.`FORUM_CATEGORY` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_FORUM_USER1`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `bjm`.`USER` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_FORUM_FORUM_CATEGORY1_idx` ON `bjm`.`FORUM` (`FORUM_CATEGORY_ID` ASC) VISIBLE;

CREATE INDEX `fk_FORUM_USER1_idx` ON `bjm`.`FORUM` (`USER_ID` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `bjm`.`FORUM_COMMENT`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bjm`.`FORUM_COMMENT` ;

CREATE TABLE IF NOT EXISTS `bjm`.`FORUM_COMMENT` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `TEXT` VARCHAR(1500) NOT NULL,
  `DATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `FORUM_ID` INT NOT NULL,
  `USER_ID` INT NOT NULL,
  PRIMARY KEY (`ID`),
  CONSTRAINT `fk_FORUM_COMMENT_FORUM1`
    FOREIGN KEY (`FORUM_ID`)
    REFERENCES `bjm`.`FORUM` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_FORUM_COMMENT_USER1`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `bjm`.`USER` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_FORUM_COMMENT_FORUM1_idx` ON `bjm`.`FORUM_COMMENT` (`FORUM_ID` ASC) VISIBLE;

CREATE INDEX `fk_FORUM_COMMENT_USER1_idx` ON `bjm`.`FORUM_COMMENT` (`USER_ID` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `bjm`.`SURVEY_VOTE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bjm`.`SURVEY_VOTE` ;

CREATE TABLE IF NOT EXISTS `bjm`.`SURVEY_VOTE` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `VOTE_TYPE` VARCHAR(10) NOT NULL,
  `COMMENT` VARCHAR(1500) NULL,
  `DATED` TIMESTAMP NOT NULL,
  `SURVEY_ID` INT NOT NULL,
  `USER_ID` INT NOT NULL,
  PRIMARY KEY (`ID`),
  CONSTRAINT `fk_SURVEY_VOTE_SURVEY1`
    FOREIGN KEY (`SURVEY_ID`)
    REFERENCES `bjm`.`SURVEY` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SURVEY_VOTE_USER1`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `bjm`.`USER` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_SURVEY_VOTE_SURVEY1_idx` ON `bjm`.`SURVEY_VOTE` (`SURVEY_ID` ASC) VISIBLE;

CREATE INDEX `fk_SURVEY_VOTE_USER1_idx` ON `bjm`.`SURVEY_VOTE` (`USER_ID` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `bjm`.`EMAIL_TEMPLATE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bjm`.`EMAIL_TEMPLATE` ;

CREATE TABLE IF NOT EXISTS `bjm`.`EMAIL_TEMPLATE` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `TEMPLATE_TYPE` VARCHAR(45) NOT NULL,
  `FILE` TEXT NOT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bjm`.`ACTIVITY`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bjm`.`ACTIVITY` ;

CREATE TABLE IF NOT EXISTS `bjm`.`ACTIVITY` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `ACTIVITY_TYPE` VARCHAR(45) NOT NULL,
  `DATED` TIMESTAMP NOT NULL,
  `DESCRIPTION` VARCHAR(145) NOT NULL,
  `ACTIVITY_ID` INT NOT NULL,
  `USER_ID` INT NOT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bjm`.`VISITOR`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bjm`.`VISITOR` ;

CREATE TABLE IF NOT EXISTS `bjm`.`VISITOR` (
  `IP_ADDRESS` VARCHAR(45) NOT NULL,
  `TIME` TIMESTAMP NOT NULL,
  `LANG` CHAR(2) NULL DEFAULT 'en',
  `REMEMBER` TINYINT(1) NULL DEFAULT 0,
  PRIMARY KEY (`IP_ADDRESS`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bjm`.`BLOG`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bjm`.`BLOG` ;

CREATE TABLE IF NOT EXISTS `bjm`.`BLOG` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `DATED` TIMESTAMP NOT NULL,
  `TITLE` VARCHAR(100) NOT NULL,
  `TEXT` MEDIUMBLOB NOT NULL,
  `FILE` VARCHAR(45) NOT NULL,
  `USER_ID` INT NOT NULL,
  PRIMARY KEY (`ID`),
  CONSTRAINT `fk_BLOG_USER1`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `bjm`.`USER` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_BLOG_USER1_idx` ON `bjm`.`BLOG` (`USER_ID` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `bjm`.`FORUM_ABUSE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bjm`.`FORUM_ABUSE` ;

CREATE TABLE IF NOT EXISTS `bjm`.`FORUM_ABUSE` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `REPORT_TEXT` VARCHAR(250) NOT NULL,
  `REPORTED_ON` TIMESTAMP NOT NULL,
  `FORUM_COMMENT_ID` INT NOT NULL,
  `USER_ID` INT NOT NULL,
  PRIMARY KEY (`ID`),
  CONSTRAINT `fk_FORUM_ABUSE_FORUM_COMMENT1`
    FOREIGN KEY (`FORUM_COMMENT_ID`)
    REFERENCES `bjm`.`FORUM_COMMENT` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_FORUM_ABUSE_USER1`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `bjm`.`USER` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_FORUM_ABUSE_FORUM_COMMENT1_idx` ON `bjm`.`FORUM_ABUSE` (`FORUM_COMMENT_ID` ASC) VISIBLE;

CREATE INDEX `fk_FORUM_ABUSE_USER1_idx` ON `bjm`.`FORUM_ABUSE` (`USER_ID` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `bjm`.`SURVEY_ABUSE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bjm`.`SURVEY_ABUSE` ;

CREATE TABLE IF NOT EXISTS `bjm`.`SURVEY_ABUSE` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `REPORT_TEXT` VARCHAR(250) NOT NULL,
  `REPORTED_ON` TIMESTAMP NOT NULL,
  `SURVEY_VOTE_ID` INT NOT NULL,
  `USER_ID` INT NOT NULL,
  PRIMARY KEY (`ID`),
  CONSTRAINT `fk_SURVEY_ABUSE_SURVEY_VOTE1`
    FOREIGN KEY (`SURVEY_VOTE_ID`)
    REFERENCES `bjm`.`SURVEY_VOTE` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SURVEY_ABUSE_USER1`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `bjm`.`USER` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_SURVEY_ABUSE_SURVEY_VOTE1_idx` ON `bjm`.`SURVEY_ABUSE` (`SURVEY_VOTE_ID` ASC) VISIBLE;

CREATE INDEX `fk_SURVEY_ABUSE_USER1_idx` ON `bjm`.`SURVEY_ABUSE` (`USER_ID` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `bjm`.`BLOG_COMMENT`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bjm`.`BLOG_COMMENT` ;

CREATE TABLE IF NOT EXISTS `bjm`.`BLOG_COMMENT` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `TEXT` VARCHAR(1500) NOT NULL,
  `DATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `BLOG_ID` INT NOT NULL,
  `USER_ID` INT NOT NULL,
  PRIMARY KEY (`ID`),
  CONSTRAINT `fk_BLOG_COMMENT_BLOG1`
    FOREIGN KEY (`BLOG_ID`)
    REFERENCES `bjm`.`BLOG` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_BLOG_COMMENT_USER1`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `bjm`.`USER` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_BLOG_COMMENT_BLOG1_idx` ON `bjm`.`BLOG_COMMENT` (`BLOG_ID` ASC) VISIBLE;

CREATE INDEX `fk_BLOG_COMMENT_USER1_idx` ON `bjm`.`BLOG_COMMENT` (`USER_ID` ASC) VISIBLE;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
