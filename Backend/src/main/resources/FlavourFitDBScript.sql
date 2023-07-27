-- -----------------------------------------------------
-- Table `Users`
-- -----------------------------------------------------
CREATE TABLE `Users` (
  `User_id` INT NOT NULL AUTO_INCREMENT,
  `First_name` VARCHAR(45) NULL,
  `Last_name` VARCHAR(45) NULL,
  `Phone` VARCHAR(15) NULL,
  `Email` VARCHAR(100) NOT NULL,
  `Age` INT NULL,
  `Street_address` VARCHAR(45) NULL,
  `City` VARCHAR(45) NULL,
  `State` VARCHAR(45) NULL,
  `Zip_code` VARCHAR(6) NULL,
  `Current_weight` DOUBLE NULL,
  `Target_Weight` DOUBLE NULL,
  `Type` ENUM('guest','registered'),
  `Password` VARCHAR(100) NULL,
  PRIMARY KEY (`User_id`),
  UNIQUE (`Email`)
  );


-- -----------------------------------------------------
-- Table `User_Auth_Tokens`
-- -----------------------------------------------------
CREATE TABLE `User_Auth_Tokens` (
  `Token_id` INT NOT NULL AUTO_INCREMENT,
  `Token` VARCHAR(45) NULL,
  `Expiry` DATETIME NULL,
  `Is_valid` TINYINT(1) NULL,
  `User_id` INT NOT NULL,
  PRIMARY KEY (`Token_id`),
  FOREIGN KEY (`User_id`) REFERENCES `Users` (`User_id`)
);


-- -----------------------------------------------------
-- Table `Recipes`
-- -----------------------------------------------------
CREATE TABLE `Recipes` (
  `Recipe_id` INT NOT NULL AUTO_INCREMENT,
  `Recipe_name` VARCHAR(45) NULL,
  `Recipe_description` TEXT NULL,
  `Types` VARCHAR(1000) NULL,
  PRIMARY KEY (`Recipe_id`)
);

-- -----------------------------------------------------
-- Table `Ingredients`
-- -----------------------------------------------------
CREATE TABLE `Ingredients` (
  `Ingredient_id` INT NOT NULL AUTO_INCREMENT,
  `Ingredient_name` VARCHAR(45) NULL,
  `quantity` DOUBLE NULL,
  `quantity_unit` VARCHAR(45) NULL,
  `Recipe_id` INT NULL,
  PRIMARY KEY (`Ingredient_id`),
  FOREIGN KEY (`Recipe_id`) REFERENCES `Recipes` (`Recipe_id`)
);


-- -----------------------------------------------------
-- Table `Saved_Recipes`
-- -----------------------------------------------------
CREATE TABLE `Saved_Recipes` (
  `Recipe_id` INT NOT NULL AUTO_INCREMENT,
  `User_id` INT NOT NULL,
  PRIMARY KEY (`Recipe_id`, `User_id`),
  FOREIGN KEY (`Recipe_id`) REFERENCES `Recipes` (`Recipe_id`),
  FOREIGN KEY (`User_id`) REFERENCES `Users` (`User_id`)
);

-- -----------------------------------------------------
-- Table `Calorie_History`
-- -----------------------------------------------------
CREATE TABLE `Calorie_History` (
  `Calorie_history_id` INT NOT NULL AUTO_INCREMENT,
  `Calorie_Count` DOUBLE NULL,
  `Update_Date` DATE NULL,
  `User_id` INT NULL,
  PRIMARY KEY (`Calorie_history_id`),
  FOREIGN KEY (`User_id`) REFERENCES `Users` (`User_id`)
);

-- -----------------------------------------------------
-- Table `Water_History`
-- -----------------------------------------------------
CREATE TABLE `Water_History` (
  `Water_history_id` INT NOT NULL AUTO_INCREMENT,
  `Water_intake` DOUBLE NULL,
  `Update_Date` DATE NULL,
  `User_id` INT NULL,
  PRIMARY KEY (`Water_history_id`),
  FOREIGN KEY (`User_id`) REFERENCES `Users` (`User_id`)
);

-- -----------------------------------------------------
-- Table `Weight_History`
-- -----------------------------------------------------
CREATE TABLE `Weight_History` (
  `weight_history_id` INT NOT NULL AUTO_INCREMENT,
  `weight` DOUBLE NULL,
  `Update_Date` DATE NULL,
  `User_id` INT NULL,
  PRIMARY KEY (`weight_history_id`),
  FOREIGN KEY (`User_id`) REFERENCES `Users` (`User_id`)
);

-- -----------------------------------------------------
-- Table `Feeds`
-- -----------------------------------------------------
CREATE TABLE `Feeds` (
  `Feed_id` INT NOT NULL AUTO_INCREMENT,
  `Feed_content` TEXT NULL,
  `Like_count` INT NULL,
  `User_id` INT NULL,
  PRIMARY KEY (`Feed_id`),
  FOREIGN KEY (`User_id`) REFERENCES `Users` (`User_id`)
);

-- -----------------------------------------------------
-- Table `Comments`
-- -----------------------------------------------------
CREATE TABLE `Comments` (
  `Comment_id` INT NOT NULL AUTO_INCREMENT,
  `Comment_content` TEXT NULL,
  `Feed_id` INT NULL,
  `User_id` INT NOT NULL,
  PRIMARY KEY (`Comment_id`),
  FOREIGN KEY (`Feed_id`) REFERENCES `Feeds` (`Feed_id`),
  FOREIGN KEY (`User_id`) REFERENCES `Users` (`User_id`)
);

-- -----------------------------------------------------
-- Table `Premium_Memberships`
-- -----------------------------------------------------
CREATE TABLE `Premium_Memberships` (
  `Premium_membership_id` INT NOT NULL AUTO_INCREMENT,
  `Start_date` DATE NULL,
  `Expiry_date` DATE NULL,
  `Is_active` TINYINT(1) NULL,
  `User_id` INT NULL,
  PRIMARY KEY (`Premium_membership_id`),
  FOREIGN KEY (`User_id`) REFERENCES `Users` (`User_id`)
);

-- -----------------------------------------------------
-- Table `Payments`
-- -----------------------------------------------------
CREATE TABLE `Payments` (
  `Payment_id` INT NOT NULL AUTO_INCREMENT,
  `Amount` DOUBLE NULL,
  `Reason` VARCHAR(45) NULL,
  `User_id` INT NOT NULL,
  `Premium_membership_id` INT NULL,
  PRIMARY KEY (`Payment_id`),
  FOREIGN KEY (`User_id`) REFERENCES `Users` (`User_id`),
  FOREIGN KEY (`Premium_membership_id`) REFERENCES `Premium_Memberships` (`Premium_membership_id`)
);

-- -----------------------------------------------------
-- Table `Fitness_Routines`
-- -----------------------------------------------------
CREATE TABLE `Fitness_Routines` (
  `Routine_id` INT NOT NULL AUTO_INCREMENT,
  `Routine_name` VARCHAR(45) NULL,
  `Routine_description` TEXT NULL,
  `Tips` TEXT NULL,
  `Fitness_Routinescol` VARCHAR(45) NULL,
  `User_id` INT NULL,
  PRIMARY KEY (`Routine_id`),
  FOREIGN KEY (`User_id`) REFERENCES `Users` (`User_id`)
);

-- -----------------------------------------------------
-- Table `Diets`
-- -----------------------------------------------------
CREATE TABLE `Diets` (
  `Diet_id` INT NOT NULL AUTO_INCREMENT,
  `Diet_name` VARCHAR(45) NULL,
  `Diet_description` TEXT NULL,
  `User_id` INT NULL,
  PRIMARY KEY (`Diet_id`),
  FOREIGN KEY (`User_id`) REFERENCES `Users` (`User_id`)
);

-- -----------------------------------------------------
-- Table `Health_Coaches`
-- -----------------------------------------------------
CREATE TABLE `Health_Coaches` (
  `Coach_id` INT NOT NULL AUTO_INCREMENT,
  `First_name` VARCHAR(45) NULL,
  `Last_Name` VARCHAR(45) NULL,
  `Years_of_experience` INT NULL,
  `Speciality` VARCHAR(45) NULL,
  `Description` TEXT NULL,
  PRIMARY KEY (`Coach_id`)
  );


-- -----------------------------------------------------
-- Table `Appointments`
-- -----------------------------------------------------
CREATE TABLE `Appointments` (
  `Appointment_id` INT NOT NULL AUTO_INCREMENT,
  `Start_date` DATETIME NULL,
  `End_Date` DATETIME NULL,
  `User_id` INT NULL,
  `Coach_id` INT NULL,
  PRIMARY KEY (`Appointment_id`),
  FOREIGN KEY (`Coach_id`) REFERENCES `Health_Coaches` (`Coach_id`),
  FOREIGN KEY (`User_id`) REFERENCES `Users` (`User_id`)
);

-- -----------------------------------------------------
-- Table `Events`
-- -----------------------------------------------------
CREATE TABLE `Events` (
  `Event_id` INT NOT NULL AUTO_INCREMENT,
  `Event_name` VARCHAR(255) NULL,
  `Start_date` DATETIME NULL,
  `End_date` DATETIME NULL,
  `Capacity` INT NULL,
  `Host_Name` VARCHAR(255) NULL,
  `Event_description` TEXT NULL,
  PRIMARY KEY (`Event_id`)
);


-- -----------------------------------------------------
-- Table `Fitness_Tips`
-- -----------------------------------------------------
CREATE TABLE `Fitness_Tips` (
  `Tip_id` INT NOT NULL AUTO_INCREMENT,
  `quote_of_the_day` TEXT NULL,
  `tip_of_the_day` TEXT NULL,
  PRIMARY KEY (`Tip_id`)
);

-- Add editable field to recipes
ALTER TABLE `Recipes`
    ADD COLUMN `editable` TINYINT NULL DEFAULT 0 AFTER `Types`;

-- Add comment username to comments
ALTER TABLE `Comments`
    ADD COLUMN `Comment_username` VARCHAR(100) NULL DEFAULT NULL;

-- Create stored procedures for calorie and water streaks
DELIMITER $$
CREATE PROCEDURE `iterateCalorieDates`(IN userId INT,OUT avgCal DOUBLE, OUT totalRows INT)
BEGIN
  DECLARE done INT DEFAULT FALSE;
  DECLARE currentDate DATE;
  DECLARE calories DOUBLE;
  DECLARE rowCount INT;

  SET currentDate = curdate();
  SET avgCal = 0.0;
  SET totalRows = 0;

  my_loop: LOOP
SELECT COUNT(*), Calorie_Count INTO rowCount,calories
FROM Calorie_History
WHERE Update_Date = currentDate AND User_id=userId;

IF calories >= 0 || calories <=0 THEN
		SET avgCal = avgCal + calories;
END IF;

    SET totalRows = totalRows + rowCount;

    IF rowCount = 0 THEN
      LEAVE my_loop;
END IF;

    SET currentDate = DATE_SUB(currentDate, INTERVAL 1 DAY);
END LOOP my_loop;

END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE `iterateWaterDates`(IN userId INT,OUT avgWater DOUBLE, OUT totalRows INT)
BEGIN
  DECLARE done INT DEFAULT FALSE;
  DECLARE currentDate DATE;
  DECLARE water DOUBLE;
  DECLARE rowCount INT;

  SET currentDate = curdate();
  SET avgWater = 0.0;
  SET totalRows = 0;

  my_loop: LOOP
SELECT COUNT(*), Water_intake INTO rowCount,water
FROM Water_History
WHERE Update_Date = currentDate AND User_id=userId;

IF water >= 0 || water <=0 THEN
		SET avgWater = avgWater + water;
END IF;

    SET totalRows = totalRows + rowCount;

    IF rowCount = 0 THEN
      LEAVE my_loop;
END IF;

    SET currentDate = DATE_SUB(currentDate, INTERVAL 1 DAY);
END LOOP my_loop;

END$$
DELIMITER ;

