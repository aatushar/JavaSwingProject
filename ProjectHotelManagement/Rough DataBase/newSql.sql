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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `checkin`
--

LOCK TABLES `checkin` WRITE;
/*!40000 ALTER TABLE `checkin` DISABLE KEYS */;
INSERT INTO `checkin` VALUES (1,104,'dfgfdg','dfgdf','gdfgdf','Male','dfgdfg','dfgdfg','2023-10-15','AC','Single',450),(2,105,'hgfhf','gfhgf','hgfh','Female','gfhfg','hfg','2023-10-18','AC','Single',450),(3,103,'vcngvb','nbvnbv','nvbn','Male','vbn','vbnvb','2023-10-12','AC','Double',400),(4,106,'fgsdfg','fsgfg','sfgsfg','Male','sdg','sdgg',NULL,'Non-AC','Single',150),(5,104,'fgfg','dfgfdg','dfgfg','Male','fdgf','dfgfg',NULL,'AC','Single',450);
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
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `checkout`
--

LOCK TABLES `checkout` WRITE;
/*!40000 ALTER TABLE `checkout` DISABLE KEYS */;
INSERT INTO `checkout` VALUES (1,104,'sdsd','sdsd','sdsd',450,16,7200,'2023-10-04','2023-10-20',NULL,NULL,NULL,NULL),(2,104,'dfgfdg','gdfgdf','dfgdf',450,6,2700,'2023-10-15','2023-10-21',NULL,NULL,NULL,NULL),(3,103,'vcngvb','nvbn','nbvnbv',400,2,800,'2023-10-12','2023-10-14',NULL,NULL,NULL,NULL),(4,103,'vcngvb','nvbn','nbvnbv',400,3,1200,'2023-10-12','2023-10-15',NULL,NULL,NULL,NULL),(5,105,'hgfhf','hgfh','gfhgf',450,6,2700,'2023-10-18','2023-10-24',NULL,NULL,NULL,NULL),(6,104,'dfgfdg','gdfgdf','dfgdf',450,4,1800,'2023-10-15','2023-10-19',NULL,NULL,NULL,NULL),(7,104,'fgfg','dfgfg','dfgfdg',450,6,2700,'2023-10-20','2023-10-26',5222,552,55,55),(8,104,'fgfg','dfgfg','dfgfdg',450,4,1800,'2023-10-27','2023-10-31',552,52,22,22),(9,104,'fgfg','dfgfg','dfgfdg',450,9,4050,'2023-10-12','2023-10-21',424224,524,4242,242),(10,104,'fgfg','dfgfg','dfgfdg',450,8,3600,'2023-10-13','2023-10-21',45,45,45,45),(11,104,'fgfg','dfgfg','dfgfdg',450,8,3600,'2023-10-13','2023-10-21',5,5,5,5),(12,105,'hgfhf','hgfh','gfhgf',450,2,900,'2023-10-18','2023-10-20',455,4,4,45),(13,105,'hgfhf','hgfh','gfhgf',450,4,1800,'2023-10-18','2023-10-22',10,1620,2000,380),(14,105,'hgfhf','hgfh','gfhgf',450,7,3150,'2023-10-18','2023-10-25',10,2835,3000,165),(15,105,'hgfhf','hgfh','gfhgf',450,7,3150,'2023-10-18','2023-10-25',10,2835,3000,165),(16,105,'hgfhf','hgfh','gfhgf',450,2,900,'2023-10-18','2023-10-20',10,810,1000,190),(17,104,'fgfg','dfgfg','dfgfdg',450,4,1800,'2023-10-20','2023-10-24',5,1710,2000,290),(18,104,'fgfg','dfgfg','dfgfdg',450,4,1800,'2023-10-21','2023-10-25',5,1710,2000,290);
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

-- Dump completed on 2023-10-17  2:31:28
