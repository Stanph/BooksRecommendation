/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50708
 Source Host           : localhost:3306
 Source Schema         : books

 Target Server Type    : MySQL
 Target Server Version : 50708
 File Encoding         : 65001

 Date: 11/07/2019 21:17:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bxbooks
-- ----------------------------
DROP TABLE IF EXISTS `bxbooks`;
CREATE TABLE `bxbooks`  (
  `bookid` int(11) NOT NULL,
  `ISBN` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `BookTitle` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `BookAuthor` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `YearOfPublication` int(11) NULL DEFAULT NULL,
  `Publisher` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `ImageURLS` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `ImageURLM` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `ImageURLL` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `score` double(11, 2) UNSIGNED NULL DEFAULT 0.00,
  PRIMARY KEY (`bookid`) USING BTREE,
  UNIQUE INDEX `bookid_UNIQUE`(`bookid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for bxusers
-- ----------------------------
DROP TABLE IF EXISTS `bxusers`;
CREATE TABLE `bxusers`  (
  `userid` int(11) NOT NULL AUTO_INCREMENT,
  `age` int(11) NULL DEFAULT NULL,
  `city` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `state` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `country` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `account` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pwd` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`userid`) USING BTREE,
  UNIQUE INDEX `userid_UNIQUE`(`userid`) USING BTREE,
  UNIQUE INDEX `account_UNIQUE`(`account`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 278860 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for ratings
-- ----------------------------
DROP TABLE IF EXISTS `ratings`;
CREATE TABLE `ratings`  (
  `userid` int(11) NULL DEFAULT NULL,
  `bookid` int(11) NULL DEFAULT NULL,
  `score` int(11) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
