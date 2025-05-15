--create account table
CREATE TABLE account (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(50) DEFAULT NULL,
    password VARCHAR(255) DEFAULT NULL
);

--create profile table
CREATE TABLE profile (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    account_id INT DEFAULT NULL,
    name VARCHAR(45) DEFAULT NULL,
    goal INT DEFAULT 0,
    items_recycled INT DEFAULT 0,
    progress DOUBLE DEFAULT 0,
    points INT DEFAULT 0,
    is_primary TINYINT DEFAULT 0,
    bronze_reward TINYINT DEFAULT 0,
    silver_reward TINYINT DEFAULT 0,
    gold_reward TINYINT DEFAULT 0,
    diamond_reward TINYINT DEFAULT 0,
    KEY account_id_idx (account_id),
    CONSTRAINT fk_account_id FOREIGN KEY (account_id) REFERENCES account (id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

--create item table
CREATE TABLE item (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    account_id INT,
    profile_id INT,
    material VARCHAR(45) DEFAULT NULL,
    amount INT DEFAULT NULL,
);
