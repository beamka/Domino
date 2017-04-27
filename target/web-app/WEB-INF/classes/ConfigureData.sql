CREATE DEFINER=`ibt`@`localhost` PROCEDURE `incert_bones`(IN size INT)
BEGIN
    DECLARE n1, n2 INT;
    SET n1 = 0;
    SET n2 = 0;
    WHILE n1 <= size DO
		 WHILE n2 <= size DO
			INSERT INTO bones (num1, num2) VALUES (n1, n2);
			SET n2 = n2 + 1;
		END WHILE;
		SET n1 = n1 + 1;
		SET n2 = n1;
    END WHILE;
END