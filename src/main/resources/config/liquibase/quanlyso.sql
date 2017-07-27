-- MySQL dump 10.13  Distrib 5.7.17, for Linux (x86_64)
--
-- Host: localhost    Database: quanlyso
-- ------------------------------------------------------
-- Server version	5.7.18

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
-- Dumping data for table `DATABASECHANGELOG`
--

LOCK TABLES `DATABASECHANGELOG` WRITE;
/*!40000 ALTER TABLE `DATABASECHANGELOG` DISABLE KEYS */;
INSERT INTO `DATABASECHANGELOG` VALUES ('00000000000001','jhipster','classpath:config/liquibase/changelog/00000000000000_initial_schema.xml','2017-06-07 10:09:53',1,'EXECUTED','7:fbd01f1f203fe7a8f7e4783ec56fc32d','createTable tableName=jhi_user; createIndex indexName=idx_user_login, tableName=jhi_user; createIndex indexName=idx_user_email, tableName=jhi_user; createTable tableName=jhi_authority; createTable tableName=jhi_user_authority; addPrimaryKey tableN...','',NULL,'3.5.3',NULL,NULL,'6830193467'),('20170522140759-1','jhipster','classpath:config/liquibase/changelog/20170522140759_added_entity_Factor.xml','2017-06-07 10:09:53',2,'EXECUTED','7:465685f74edc6a7aab7fb66a69f96a62','createTable tableName=factor','',NULL,'3.5.3',NULL,NULL,'6830193467'),('20170522140700-1','jhipster','classpath:config/liquibase/changelog/20170522140700_added_entity_Style.xml','2017-06-07 10:09:53',3,'EXECUTED','7:6f4de410a61d54626b69408414663823','createTable tableName=style','',NULL,'3.5.3',NULL,NULL,'6830193467'),('20170522140701-1','jhipster','classpath:config/liquibase/changelog/20170522140701_added_entity_Types.xml','2017-06-07 10:09:53',4,'EXECUTED','7:1718753d8242394e7a92bef3c18d5099','createTable tableName=types','',NULL,'3.5.3',NULL,NULL,'6830193467'),('20170522140702-1','jhipster','classpath:config/liquibase/changelog/20170522140702_added_entity_Channel.xml','2017-06-07 10:09:53',5,'EXECUTED','7:2cc72efe21b134dd3f557e52937f3c4f','createTable tableName=channel','',NULL,'3.5.3',NULL,NULL,'6830193467'),('20170522140703-1','jhipster','classpath:config/liquibase/changelog/20170522140703_added_entity_ProfitFactor.xml','2017-06-07 10:09:53',6,'EXECUTED','7:4ed0f95f726676acddc07d86ca8b7057','createTable tableName=profit_factor','',NULL,'3.5.3',NULL,NULL,'6830193467'),('20170522140704-1','jhipster','classpath:config/liquibase/changelog/20170522140704_added_entity_CostFactor.xml','2017-06-07 10:09:53',7,'EXECUTED','7:66482fe54f87688362e6249ec2196579','createTable tableName=cost_factor','',NULL,'3.5.3',NULL,NULL,'6830193467'),('20170522140705-1','jhipster','classpath:config/liquibase/changelog/20170522140705_added_entity_Transactions.xml','2017-06-07 10:09:53',8,'EXECUTED','7:e27dbc5aceef1416b4c95be61503d216','createTable tableName=transactions','',NULL,'3.5.3',NULL,NULL,'6830193467'),('20170522141221-1','jhipster','classpath:config/liquibase/changelog/20170522141221_added_entity_TransactionDetails.xml','2017-06-07 10:09:53',9,'EXECUTED','7:7f3edd10fd3330b8c6be2360ee69d79b','createTable tableName=transaction_details','',NULL,'3.5.3',NULL,NULL,'6830193467'),('20170522140703-2','jhipster','classpath:config/liquibase/changelog/20170522140703_added_entity_constraints_ProfitFactor.xml','2017-06-07 10:09:54',10,'EXECUTED','7:e43eb0406fb7461aa17249211952906b','addForeignKeyConstraint baseTableName=profit_factor, constraintName=fk_profit_factor_factors_id, referencedTableName=factor; addForeignKeyConstraint baseTableName=profit_factor, constraintName=fk_profit_factor_styles_id, referencedTableName=style;...','',NULL,'3.5.3',NULL,NULL,'6830193467'),('20170522140704-2','jhipster','classpath:config/liquibase/changelog/20170522140704_added_entity_constraints_CostFactor.xml','2017-06-07 10:09:54',11,'EXECUTED','7:354b34fde1b8f99da21121112b13d057','addForeignKeyConstraint baseTableName=cost_factor, constraintName=fk_cost_factor_factors_id, referencedTableName=factor; addForeignKeyConstraint baseTableName=cost_factor, constraintName=fk_cost_factor_styles_id, referencedTableName=style; addFore...','',NULL,'3.5.3',NULL,NULL,'6830193467'),('20170522141221-2','jhipster','classpath:config/liquibase/changelog/20170522141221_added_entity_constraints_TransactionDetails.xml','2017-06-07 10:09:54',12,'EXECUTED','7:f341b4b47c4b6bb708d4f2d704b67cd7','addForeignKeyConstraint baseTableName=transaction_details, constraintName=fk_transaction_details_transactions_id, referencedTableName=transactions; addForeignKeyConstraint baseTableName=transaction_details, constraintName=fk_transaction_details_ch...','',NULL,'3.5.3',NULL,NULL,'6830193467'),('20170522140705-2','jhipster','classpath:config/liquibase/changelog/20170522140705_added_entity_constraints_Transactions.xml','2017-06-07 10:09:54',13,'EXECUTED','7:628cb10c090a5016b4a10839e9aa8b3d','addForeignKeyConstraint baseTableName=transactions, constraintName=fk_transactions_users_id, referencedTableName=jhi_user','',NULL,'3.5.3',NULL,NULL,'6830193467');
/*!40000 ALTER TABLE `DATABASECHANGELOG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `DATABASECHANGELOGLOCK`
--

LOCK TABLES `DATABASECHANGELOGLOCK` WRITE;
/*!40000 ALTER TABLE `DATABASECHANGELOGLOCK` DISABLE KEYS */;
INSERT INTO `DATABASECHANGELOGLOCK` VALUES (1,'\0',NULL,NULL);
/*!40000 ALTER TABLE `DATABASECHANGELOGLOCK` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `channel`
--

LOCK TABLES `channel` WRITE;
/*!40000 ALTER TABLE `channel` DISABLE KEYS */;
INSERT INTO `channel` VALUES (1,'Miền bắc','mien-bac','','','','','','',''),(2,'An Giang','an-giang','\0','\0','\0','\0','','\0','\0'),(3,'Bạc Liêu','bac-lieu','\0','\0','','\0','\0','\0','\0'),(4,'Bến Tre','ben-tre','\0','\0','','\0','\0','\0','\0'),(5,'Bình Dương','binh-duong','\0','\0','\0','\0','\0','','\0'),(6,'Bình Phước','binh-phuoc','\0','\0','\0','\0','\0','\0',''),(7,'Bình Thuận','binh-thuan','\0','\0','\0','\0','','\0','\0'),(8,'Cà Mau','ca-mau','\0','','\0','\0','\0','\0','\0');
/*!40000 ALTER TABLE `channel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `cost_factor`
--

LOCK TABLES `cost_factor` WRITE;
/*!40000 ALTER TABLE `cost_factor` DISABLE KEYS */;
INSERT INTO `cost_factor` VALUES (1,0.75,1,1,1);
/*!40000 ALTER TABLE `cost_factor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `factor`
--

LOCK TABLES `factor` WRITE;
/*!40000 ALTER TABLE `factor` DISABLE KEYS */;
INSERT INTO `factor` VALUES (1,'Hệ A'),(2,'Hệ B');
/*!40000 ALTER TABLE `factor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `jhi_authority`
--

LOCK TABLES `jhi_authority` WRITE;
/*!40000 ALTER TABLE `jhi_authority` DISABLE KEYS */;
INSERT INTO `jhi_authority` VALUES ('ROLE_ADMIN'),('ROLE_USER');
/*!40000 ALTER TABLE `jhi_authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `jhi_persistent_audit_event`
--

LOCK TABLES `jhi_persistent_audit_event` WRITE;
/*!40000 ALTER TABLE `jhi_persistent_audit_event` DISABLE KEYS */;
INSERT INTO `jhi_persistent_audit_event` VALUES (1,'admin','2017-06-12 07:43:32','AUTHENTICATION_SUCCESS'),(2,'admin','2017-06-12 08:03:07','AUTHENTICATION_SUCCESS'),(3,'admin','2017-06-12 08:31:59','AUTHENTICATION_SUCCESS'),(4,'admin','2017-06-17 13:03:55','AUTHENTICATION_SUCCESS'),(5,'admin','2017-06-18 13:07:02','AUTHENTICATION_SUCCESS'),(6,'admin','2017-06-18 13:08:59','AUTHENTICATION_SUCCESS'),(7,'admin','2017-06-18 13:49:25','AUTHENTICATION_SUCCESS'),(8,'admin','2017-06-19 08:33:15','AUTHENTICATION_SUCCESS'),(9,'admin','2017-06-21 08:17:17','AUTHENTICATION_SUCCESS');
/*!40000 ALTER TABLE `jhi_persistent_audit_event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `jhi_persistent_audit_evt_data`
--

LOCK TABLES `jhi_persistent_audit_evt_data` WRITE;
/*!40000 ALTER TABLE `jhi_persistent_audit_evt_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `jhi_persistent_audit_evt_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `jhi_user`
--

LOCK TABLES `jhi_user` WRITE;
/*!40000 ALTER TABLE `jhi_user` DISABLE KEYS */;
INSERT INTO `jhi_user` VALUES (1,'system','$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG','System','System','system@localhost','','','vi',NULL,NULL,'system','2017-06-07 10:09:53',NULL,'system',NULL,NULL,NULL),(2,'anonymoususer','$2a$10$j8S5d7Sr7.8VTOYNviDPOeWX8KcYILUVJBsYV83Y5NtECayypx9lO','Anonymous','User','anonymous@localhost','','','vi',NULL,NULL,'system','2017-06-07 10:09:53',NULL,'system',NULL,NULL,NULL),(3,'admin','$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC','Administrator','Administrator','admin@localhost','','','vi',NULL,NULL,'system','2017-06-07 10:09:53',NULL,'system',NULL,NULL,NULL),(4,'user','$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K','User','User','user@localhost','','','vi',NULL,NULL,'system','2017-06-07 10:09:53',NULL,'system',NULL,NULL,NULL),(5,'dai.mai','$2a$10$3c4lN292Gm6Mvil9KfXxi.T2fWilRcHWTunmpC9KYSKFsHB/0XoU6','Paul','Mai','dai.maithanh@gmail.com',NULL,'','vi',NULL,'53947724521352269942','admin','2017-06-13 07:47:33','2017-06-13 07:47:33','admin','2017-06-13 07:47:33','2017-06-13 12:00:00','2017-06-30 12:00:00');
/*!40000 ALTER TABLE `jhi_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `jhi_user_authority`
--

LOCK TABLES `jhi_user_authority` WRITE;
/*!40000 ALTER TABLE `jhi_user_authority` DISABLE KEYS */;
INSERT INTO `jhi_user_authority` VALUES (1,'ROLE_ADMIN'),(3,'ROLE_ADMIN'),(1,'ROLE_USER'),(3,'ROLE_USER'),(4,'ROLE_USER');
/*!40000 ALTER TABLE `jhi_user_authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `profit_factor`
--

LOCK TABLES `profit_factor` WRITE;
/*!40000 ALTER TABLE `profit_factor` DISABLE KEYS */;
INSERT INTO `profit_factor` VALUES (1,75,1,1,1);
/*!40000 ALTER TABLE `profit_factor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `style`
--

LOCK TABLES `style` WRITE;
/*!40000 ALTER TABLE `style` DISABLE KEYS */;
INSERT INTO `style` VALUES (1,'2 số'),(2,'3 số'),(3,'4 số');
/*!40000 ALTER TABLE `style` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `types`
--

LOCK TABLES `types` WRITE;
/*!40000 ALTER TABLE `types` DISABLE KEYS */;
INSERT INTO `types` VALUES (1,'Số đầu'),(2,'Số cuối'),(3,'Đầu cuối'),(4,'Bao lô');
/*!40000 ALTER TABLE `types` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-06-22  9:07:15
