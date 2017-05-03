
-- jdbc:mysql://mysql.storage.cloud.wso2.com:3306/LicenseManager_dummy2744	
-- 4:7facLC_e
-- userx_AC2rbQAt


CREATE TABLE user (
userid INT PRIMARY KEY AUTO_INCREMENT,
username VARCHAR(100) UNIQUE NOT NULL,
password TEXT NOT NULL,
fullname VARCHAR(100) NOT NULL,
email VARCHAR(100) NOT NULL,
useruuid VARCHAR(256) NOT NULL,
notes TEXT);

-- insert into user (username, password, fullname, email) values ('thilina', '6b3a55e0261b0304143f805a24924d0c1c44524821305f31d9277843b8a10f4e', 'Thilina Piyasundara', 'thilinapiy@gmail.com');

-- select count(userid) from user where username='thilina' and password='6b3a55e0261b0304143f805a24924d0c1c44524821305f31d9277843b8a10f4e';
