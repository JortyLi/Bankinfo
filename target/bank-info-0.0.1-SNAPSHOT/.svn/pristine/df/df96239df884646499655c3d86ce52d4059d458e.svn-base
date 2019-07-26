/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50721
 Source Host           : localhost:3306
 Source Schema         : zsyh_data

 Target Server Type    : MySQL
 Target Server Version : 50721
 File Encoding         : 65001

 Date: 21/12/2018 13:23:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bank_login
-- ----------------------------
DROP TABLE IF EXISTS `bank_login`;
CREATE TABLE `bank_login`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bank_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `login_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `login_pass` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `bank_state` int(255) NULL DEFAULT NULL,
  `bank_confID` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;



ALTER TABLE `zsyh_data`.`record_copy1` 
ADD COLUMN `bname` varchar(255) NULL COMMENT '收款银行信息-如：张三-招商银行' AFTER `is_sum`;
ALTER TABLE `zsyh_data`.`record` 
ADD COLUMN `bname` varchar(255) NULL COMMENT '收款银行信息-如：张三-招商银行' AFTER `is_sum`;


update `record` set is_sum=0 where LOCATE('2019-04-16',tradingHour) ;

--2019.04.16--
ALTER TABLE `zsyh_data`.`record`
ADD COLUMN `token` varchar(255) NOT NULL COMMENT '唯一token值' AFTER `bname`;
ALTER TABLE `zsyh_data`.`record_copy1`
ADD COLUMN `token` varchar(255) NULL COMMENT '唯一token值' AFTER `bname`;


--2019-06-03--
DROP TRIGGER `add_not_copy1`;

CREATE DEFINER = `root`@`localhost` TRIGGER `add_not_copy1` BEFORE INSERT ON `record` FOR EACH ROW BEGIN 
		
		DECLARE _bname varchar(255) default null;
		
		select bname into _bname from record where incoming > 0 ORDER BY tradingHour DESC limit 1;
		IF new.incoming < 0 THEN
			
			set new.bname = _bname;
		END IF;
		
END;
