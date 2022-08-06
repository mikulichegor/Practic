CREATE TABLE `vacation` (
  `id` int NOT NULL AUTO_INCREMENT,
  `userid` int NOT NULL,
  `basic` int DEFAULT '30',
  `daysleft` int DEFAULT '30',
  `daysgot` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`),
  CONSTRAINT `vacation_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;