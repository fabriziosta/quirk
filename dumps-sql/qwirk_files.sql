-- MySQL dump 10.15  Distrib 10.0.29-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: localhost
-- ------------------------------------------------------
-- Server version	10.0.29-MariaDB-0ubuntu0.16.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `files`
--

DROP TABLE IF EXISTS `files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `files` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `FILE_NAME` varchar(50) NOT NULL DEFAULT '0',
  `ID_USER` int(11) NOT NULL DEFAULT '0',
  `PROFILE_PIC` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `files`
--

LOCK TABLES `files` WRITE;
/*!40000 ALTER TABLE `files` DISABLE KEYS */;
INSERT INTO `files` VALUES (24,'1.jpg',1,1),(25,'2.jpg',2,1),(26,'3.jpg',3,1),(27,'4.jpg',4,1),(28,'5.jpg',5,1),(30,'rufy.jpg',1,0),(31,'msi-icon.png',1,0),(32,'file.docx',1,0),(33,'msi-icon2.png',1,0),(34,'rufy.jpg',1,0),(35,'rufy.jpg',31,1),(36,'drago.png',1,0),(37,'drago.png',4,0),(38,'cagnolona.jpg',1,0),(39,'file.docx',4,0),(40,'ciao.png',1,0),(41,'ciao.png',1,0),(42,'ciao.png',1,0),(43,'ciao.png',1,0),(44,'ciao.png',1,0),(45,'ciao.png',1,0),(46,'ciao.png',1,0),(47,'ciao.png',1,0),(48,'ciao.png',1,0),(49,'ciao.png',1,0),(50,'ciao.png',1,0),(51,'ciao.png',1,0),(52,'ciao.png',1,0),(53,'file.png',1,0),(54,'picture.png',1,0),(55,'ciao.png',1,0),(56,'file.png',1,0),(57,'Problema montaggio partizione windows su linux.txt',1,0),(58,'Problema montaggio partizione windows su linux.txt',1,0),(59,'Problema montaggio partizione windows su linux.txt',1,0),(60,'ciao.png',1,0),(61,'fab.jpg',32,1),(62,'10.jpg',1,0),(63,'1a7654637b71d3ceb449cce334b58c20.jpg',3,0),(64,'J-AX & Fedez - Assenzio ft. Stash, Levante.mp3',3,0),(65,'FiwukSanshou.mp4',3,0);
/*!40000 ALTER TABLE `files` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-05-29 11:33:47
