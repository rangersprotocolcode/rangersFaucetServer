create database faucet;
use faucet;


DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(42) DEFAULT NULL COMMENT '账户地址',
  `gotCoinTime` varchar(40) DEFAULT NULL COMMENT '最近一次领取测试币的时间',
  `gotNFTTime` varchar(40) DEFAULT NULL COMMENT '最近一次领取测试NFT的时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `address_index` (`address`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='账户最近领取测试资产信息';



CREATE TABLE `asset_statistics` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(42) DEFAULT NULL COMMENT '资产名字',
  `count` bigint(11) NOT NULL DEFAULT '0' COMMENT '资产总量',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_index` (`name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='领取测试资产统计';