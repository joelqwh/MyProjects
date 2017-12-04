CREATE TABLE student (
      id bigint AUTO_INCREMENT NOT NULL,
      name varchar(255),
      nick varchar(255),
      course varchar(255),
PRIMARY KEY(id),
INDEX student_id_INDEX (id));

INSERT INTO `sa45demo`.`student` (`name`, `nick`, `course`) VALUES ('J', 'Q', 'WH');
INSERT INTO `sa45demo`.`student` (`name`, `nick`, `course`) VALUES ('John', 'Bon', 'Jovi');
INSERT INTO `sa45demo`.`student` (`name`, `nick`, `course`) VALUES ('name', 'nick', 'course');
INSERT INTO `sa45demo`.`student` (`name`, `nick`, `course`) VALUES ('Joel', 'is', 'Java Programming');
