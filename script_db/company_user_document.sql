CREATE TABLE `user_document` (
  `user_id` int NOT NULL,
  `document_id` int NOT NULL,
  `confirmdir` varchar(20) DEFAULT 'В обработке',
  `confirmacc` varchar(20) DEFAULT 'В обработке',
  PRIMARY KEY (`user_id`,`document_id`),
  KEY `constr_document_id_fk` (`document_id`),
  CONSTRAINT `constr_document_id_fk` FOREIGN KEY (`document_id`) REFERENCES `document` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `constr_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
