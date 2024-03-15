-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: hotelmanagement
-- ------------------------------------------------------
-- Server version	8.0.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `checkin`
--

DROP TABLE IF EXISTS `checkin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `checkin` (
  `id` int NOT NULL AUTO_INCREMENT,
  `roomnumber` int DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `mobile` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `gender` varchar(45) DEFAULT NULL,
  `nationality` varchar(45) DEFAULT NULL,
  `adress` varchar(45) DEFAULT NULL,
  `checkin` date DEFAULT NULL,
  `roomtype` varchar(45) DEFAULT NULL,
  `bed` varchar(45) DEFAULT NULL,
  `price` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `checkin`
--

LOCK TABLES `checkin` WRITE;
/*!40000 ALTER TABLE `checkin` DISABLE KEYS */;
INSERT INTO `checkin` VALUES (1,105,'xcvcv','cvcv','cxvcv','Male','cvcv','cxvcv','2023-10-20','AC','Single',500);
/*!40000 ALTER TABLE `checkin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `checkout`
--

DROP TABLE IF EXISTS `checkout`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `checkout` (
  `chkId` int NOT NULL AUTO_INCREMENT,
  `roomnumber` int DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `cell` varchar(45) DEFAULT NULL,
  `price` float DEFAULT NULL,
  `totaldays` int DEFAULT NULL,
  `totalamount` float DEFAULT NULL,
  `checkindate` date DEFAULT NULL,
  `checkoutdate` date DEFAULT NULL,
  `tax` float DEFAULT NULL,
  `actualamount` float DEFAULT NULL,
  `received` float DEFAULT NULL,
  `cashreturn` float DEFAULT NULL,
  PRIMARY KEY (`chkId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `checkout`
--

LOCK TABLES `checkout` WRITE;
/*!40000 ALTER TABLE `checkout` DISABLE KEYS */;
INSERT INTO `checkout` VALUES (1,106,'sdd','sdsd','sdsd',63,1,63,'2023-10-20','2023-10-21',10,56.7,90,33.3),(2,105,'xcvcv','cxvcv','cvcv',500,10,5000,'2023-10-20','2023-10-30',10,4500,5000,500),(3,105,'xcvcv','cxvcv','cvcv',500,8,4000,'2023-10-20','2023-10-28',10,4400,4500,900);
/*!40000 ALTER TABLE `checkout` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manageroom`
--

DROP TABLE IF EXISTS `manageroom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `manageroom` (
  `RoomNumber` int NOT NULL,
  `RoomType` varchar(45) NOT NULL,
  `Bed` varchar(45) NOT NULL,
  `Price` float NOT NULL,
  `status` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`RoomNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manageroom`
--

LOCK TABLES `manageroom` WRITE;
/*!40000 ALTER TABLE `manageroom` DISABLE KEYS */;
INSERT INTO `manageroom` VALUES (101,'Non-AC','Double',250,'Unbooked'),(102,'Non-AC','Single',300,'Unbooked'),(103,'AC','Double',400,'Unbooked'),(104,'AC','Single',450,'Unbooked'),(105,'AC','Single',500,'Unbooked'),(106,'Non-AC','Single',150,'Unbooked'),(107,'Non-AC','Double',420,'Unbooked'),(108,'AC','Double',550,'Not Booked'),(110,'AC','Double',600,'Unbooked');
/*!40000 ALTER TABLE `manageroom` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-18  2:22:44
