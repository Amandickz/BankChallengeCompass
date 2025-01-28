-- MySQL Script generated by MySQL Workbench
-- Tue Jan 28 11:37:35 2025
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`BankCustomer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`BankCustomer` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NULL,
  `date` DATE NULL,
  `cpf` VARCHAR(15) NULL,
  `phone` VARCHAR(15) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Account` (
  `numberAccount` INT NOT NULL,
  `type` INT NULL,
  `password` VARCHAR(45) NULL,
  `balance` FLOAT NULL,
  `BankCustomer_id` INT NOT NULL,
  PRIMARY KEY (`numberAccount`),
  INDEX `fk_Account_BankCustomer_idx` (`BankCustomer_id` ASC) VISIBLE,
  CONSTRAINT `fk_Account_BankCustomer`
    FOREIGN KEY (`BankCustomer_id`)
    REFERENCES `mydb`.`BankCustomer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Bank`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Bank` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `accountNumber` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Bank_Account1_idx` (`accountNumber` ASC) VISIBLE,
  CONSTRAINT `fk_Bank_Account1`
    FOREIGN KEY (`accountNumber`)
    REFERENCES `mydb`.`Account` (`numberAccount`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`BankStatement`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`BankStatement` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `dateTransition` DATE NULL,
  `function` INT NULL,
  `amount` FLOAT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Transitions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Transitions` (
  `BankStatement_id` INT NOT NULL,
  `Bank_id` INT NOT NULL,
  INDEX `fk_Transitions_BankStatement1_idx` (`BankStatement_id` ASC) VISIBLE,
  INDEX `fk_Transitions_Bank1_idx` (`Bank_id` ASC) VISIBLE,
  CONSTRAINT `fk_Transitions_BankStatement1`
    FOREIGN KEY (`BankStatement_id`)
    REFERENCES `mydb`.`BankStatement` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Transitions_Bank1`
    FOREIGN KEY (`Bank_id`)
    REFERENCES `mydb`.`Bank` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
