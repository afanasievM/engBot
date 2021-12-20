DROP TABLE vocabulary;
DROP TABLE users;
CREATE TABLE users(
    id INT NOT NULL,
    username VARCHAR(100),
    first_name VARCHAR(100),
    user_language VARCHAR(20),
    role VARCHAR(20),
    join_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
CREATE TABLE learning_groups(
    id INT NOT NULL AUTO_INCREMENT,
    owner_id INT NOT NULL,
    group_name VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE(group_name)
);
CREATE TABLE vocabulary(
    word_id INT NOT NULL AUTO_INCREMENT,
    owner_id INT NOT NULL,
    word VARCHAR(100),
    word_translation VARCHAR(100),
    FOREIGN KEY(owner_id) REFERENCES users(id),
    PRIMARY KEY (word_id),
    UNIQUE(owner_id,word)
);
CREATE TABLE groups_users(
    group_id INT NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY(group_id) REFERENCES users(id),
    FOREIGN KEY(user_id) REFERENCES learning_groups(id),
    UNIQUE(group_id,user_id)
);
CREATE TABLE groups_admins(
    group_id INT NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY(group_id) REFERENCES users(id),
    FOREIGN KEY(user_id) REFERENCES learning_groups(id),
    UNIQUE(group_id,user_id)
);
CREATE TABLE groups_teachers(
    group_id INT NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY(group_id) REFERENCES users(id),
    FOREIGN KEY(user_id) REFERENCES learning_groups(id),
    UNIQUE(group_id,user_id)
);
