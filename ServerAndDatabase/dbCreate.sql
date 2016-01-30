'CREATE DATABASE `mnemorizer` /*!40100 DEFAULT CHARACTER SET utf8 */'

'CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(100) DEFAULT NULL,
  `latitude` decimal(9,6) DEFAULT NULL,
  `longitude` decimal(9,6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8'


'CREATE TABLE `words` (
  `id` int(11) NOT NULL,
  `word` varchar(40) NOT NULL,
  `deckid` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8'

'CREATE TABLE `meanings` (
  `id` int(11) NOT NULL,
  `wordid` int(11) DEFAULT NULL,
  `meaning` varchar(150) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `wordid` (`wordid`),
  CONSTRAINT `meanings_ibfk_1` FOREIGN KEY (`wordid`) REFERENCES `words` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8'


'CREATE TABLE `mnemonics` (
  `id` int(11) NOT NULL,
  `mnemonic` varchar(500) DEFAULT NULL,
  `wordid` int(11) DEFAULT NULL,
  `creatorid` int(50) DEFAULT NULL,
  `latitude` decimal(9,6) DEFAULT NULL,
  `longitude` decimal(9,6) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `wordid` (`wordid`),
  KEY `creatorid` (`creatorid`),
  CONSTRAINT `mnemonics_ibfk_1` FOREIGN KEY (`wordid`) REFERENCES `words` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `mnemonics_ibfk_2` FOREIGN KEY (`creatorid`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8'