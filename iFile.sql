-- Adminer 4.7.1 MySQL dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

DROP TABLE IF EXISTS `files`;
CREATE TABLE `files` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `filename` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `user_id` int(11) NOT NULL,
  `upload_date` datetime NOT NULL,
  `path` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `files_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `files` (`id`, `filename`, `user_id`, `upload_date`, `path`) VALUES
(2,	'rt.txt',	4,	'2019-05-18 22:09:21',	'/var/lib/tomcat8/webapps/cs3220stu91/WEB-INF/uploads'),
(4,	'Vera Institute of Justice Cover Letter.docx',	5,	'2019-05-18 22:09:38',	'/var/lib/tomcat8/webapps/cs3220stu91/WEB-INF/uploads/Vera Institute of Justice Cover Letter.docx'),
(6,	'Alexander Banos - Master Resume.docx',	5,	'2019-05-18 22:09:58',	'/var/lib/tomcat8/webapps/cs3220stu91/WEB-INF/uploads/Alexander Banos - Master Resume.docx'),
(8,	'background.jpg',	6,	'2019-05-18 22:23:01',	'/var/lib/tomcat8/webapps/cs3220stu91/WEB-INF/uploads/background.jpg'),
(9,	'NewData',	2,	'2019-05-18 22:34:43',	'D:\\CalStateLA\\WebInterPrg\\spring2019\\apache-tomcat-8.5.37\\wtpwebapps\\cs3220stu91\\WEB-INF\\uploads'),
(10,	'Bean.txt',	2,	'2019-05-18 22:40:15',	'D:\\CalStateLA\\WebInterPrg\\spring2019\\apache-tomcat-8.5.37\\wtpwebapps\\cs3220stu91\\WEB-INF\\uploads/Bean.txt');

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `password` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `users` (`id`, `name`, `password`) VALUES
(1,	'albert',	'abcd'),
(2,	'htmnnew',	'hello'),
(3,	'Erika',	'doublenn'),
(4,	'test123',	'hi'),
(5,	'Alexander',	'abcd1234'),
(6,	'juan',	'lol');

-- 2019-05-19 05:59:33
