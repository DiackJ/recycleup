
--create account table
CREATE TABLE account (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(50) DEFAULT NULL,
    password VARCHAR(255) NOT NULL
);

--create profile table
CREATE TABLE profile (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(45) NOT NULL,
    goal INT DEFAULT 0,
    items_recycled INT DEFAULT 0,
    progress DOUBLE DEFAULT 0,
    points INT DEFAULT 0,
    is_primary TINYINT DEFAULT 0,
    bronze_reward TINYINT DEFAULT 0,
    silver_reward TINYINT DEFAULT 0,
    gold_reward TINYINT DEFAULT 0,
    diamond_reward TINYINT DEFAULT 0,
    account_id INT NOT NULL,
    KEY account_id_idx (account_id),
    CONSTRAINT fk_account_id FOREIGN KEY (account_id) REFERENCES account (id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

--create item table
CREATE TABLE item (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    material VARCHAR(45) NOT NULL,
    amount INT NOT NULL,
    account_id INT,
    profile_id INT,
    KEY profile_id_idx (profile_id),
    CONSTRAINT fk_profile_id FOREIGN KEY (profile_id) REFERENCES profile (id)
        ON DELETE SET NULL ON UPDATE CASCADE
);