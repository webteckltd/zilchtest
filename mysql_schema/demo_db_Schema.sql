SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema zilch
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `zilch` DEFAULT CHARACTER SET utf8 ;
USE `zilch` ;

-- -----------------------------------------------------
-- Table `zilch`.`customer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zilch`.`customer` (
  `objectID` VARCHAR(36) NOT NULL,   
  `firstName` VARCHAR(100) NOT NULL,
  `lastName` VARCHAR(100) NOT NULL,    
  `email` VARCHAR(100) NOT NULL,
  `contactNumber` VARCHAR(15) NULL,
  `password` varchar(500) NOT NULL,  
  `customerRole` varchar(50) NOT NULL,     
  `createDate` DATETIME NOT NULL,
  `lastUpdateDate` DATETIME NOT NULL,
  PRIMARY KEY (`objectID`),
  UNIQUE INDEX `emailID_UNIQUE` (`email` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `zilch`.`zilchcard`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zilch`.`zilchcard` (
  `objectID` VARCHAR(36) NOT NULL, 
  `cardNumber` VARCHAR(16) NOT NULL,    
  `expirationDate` DATE NOT NULL,
  `cvc` VARCHAR(3) NOT NULL,     
  `cardBalance` BIGINT NOT NULL,     
  `custmerObjectID` VARCHAR(36) NOT NULL,        
  `createDate` DATETIME NOT NULL,
  `lastUpdateDate` DATETIME NOT NULL,
  PRIMARY KEY (`objectID`),
  UNIQUE INDEX `cardNumber_UNIQUE` (`cardNumber` ASC),
  CONSTRAINT `FK_card2customer`
    FOREIGN KEY (`custmerObjectID`)
    REFERENCES `zilch`.`customer` (`objectID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



-- -----------------------------------------------------
-- Table `zilch`.`transaction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zilch`.`transaction` (
  `objectID` VARCHAR(36) NOT NULL, 
  `transactionAmount`BIGINT NOT NULL,  
  `transactionRefernce`  VARCHAR(100) NOT NULL,
  `transactionDescription` VARCHAR(250) NULL, 
  `idempotencyKey` VARCHAR(36) NOT NULL,
  `transactionStatus` boolean DEFAULT false,   
  `transactionFailureReason` VARCHAR(1000) NULL,    
  `zilchcardObjectID` VARCHAR(36) NOT NULL,        
  `createDate` DATETIME NOT NULL,
  `lastUpdateDate` DATETIME NOT NULL,
  PRIMARY KEY (`objectID`),  
  UNIQUE KEY `transaction_idempotency_idx` (`zilchcardObjectID` ,`idempotencyKey`),
  CONSTRAINT `FK_transaction2card`
    FOREIGN KEY (`zilchcardObjectID`)
    REFERENCES `zilch`.`zilchcard` (`objectID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
