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
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `FNAME` char(50) DEFAULT NULL,
  `LNAME` char(50) DEFAULT NULL,
  `EMAIL` varchar(50) DEFAULT NULL,
  `USERNAME` varchar(50) DEFAULT NULL,
  `PASSWORD` varchar(50) DEFAULT NULL,
  `STATUS` varchar(50) DEFAULT NULL,
  `TOKEN` varchar(50) DEFAULT NULL,
  `IP` varchar(15) DEFAULT NULL,
  `PORT1` varchar(4) NOT NULL DEFAULT '1050',
  `PORT2` varchar(4) NOT NULL DEFAULT '1051',
  `groups` char(150) DEFAULT ';',
  `channels` char(150) DEFAULT ';',
  `COUNT_TRIES` int(11) DEFAULT '0',
  `BAN_DATE` char(100) DEFAULT '0000.00.00.00.00.00',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Fabrizio','Asta','aa@aa.aa','fabriziosta','0cc175b9c0f1b6a831c399e269772661','cristian',NULL,'192.168.0.27','1800','1051',';10;9;',';3;4;',0,'0000.00.00.00.00.00'),(2,'Davide','Parlapiano','bb@bb.bb','davi94','0cc175b9c0f1b6a831c399e269772661','ciao',NULL,'192.168.0.6','1050','1051',';',';4;5;',0,'0000.00.00.00.00.00'),(3,'Riccardo','Genna','cc@cc.cc','ricca91','0cc175b9c0f1b6a831c399e269772661','sica',NULL,'0.0.0.0','1050','1051',';11;10;14;',';5;4;',0,'0000.00.00.00.00.00'),(4,'Carlo','Guastavino','dd@dd.dd','carlo60','0cc175b9c0f1b6a831c399e269772661','ciao',NULL,'0.0.0.0','1050','1051',';10;',';3;4;',0,'0000.00.00.00.00.00'),(5,'Giovanni','Ghelfi','ee@ee.ee','giuvà','0cc175b9c0f1b6a831c399e269772661','Hello World!',NULL,'0.0.0.0','1050','1051',';10;9;',';3;4;5;6;7;8;',0,'0000.00.00.00.00.00'),(31,'Salvatore','Messina','ff@ff.ff','salvo94','60a59ae943cb6f0983ed9876395df197','Hello World',NULL,'0.0.0.0','1050','1051',';',';',0,'0000.00.00.00.00.00'),(32,'Fabrizio','Asta','gg@gg.gg','supinfo92','dbc92dfad8f93fd24e484ab9a17ccab3','Away',NULL,'0.0.0.0','1050','1051',';12;13;14;',';6;7;8;',0,'0000.00.00.00.00.00');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
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
