package cn.xyz.test.testJdbc;

import com.alibaba.fastjson.JSONArray;

import cn.xyz.orm.db.DbBase;

public class Test2 {

	public static void main(String[] args) {
		try {
			DbBase db = DbBase.getJdbc().startTransaction();
			db.execute("lock tables t1 write;");
			JSONArray data = db.find("select * from t1");
			System.out.println(data);
			Thread.sleep(100000);
			db.commit();
			/*	SET AUTOCOMMIT=0;
				LOCAK TABLES t1 WRITE, t2 READ, ...;
				[do something with tables t1 and here];
				COMMIT;
				UNLOCK TABLES;
				查看当前线程处理情况，如果不使用full关键字，信息字段中只会显示每个语句的前100个字符。
				show processlist; 
				show full processlist;
				
				查询表级锁争用情况 Table_locks_immediate  指的是能够立即获得表级锁的次数  Table_locks_waited  指的是不能立即获取表级锁而需要等待的次数
				show status like 'Table%';
				
				获取锁定次数、锁定造成其他线程等待次数，以及锁定等待时间信息
				show status like '%lock%';
				
				查锁表：查看正在被锁定的的表
				show OPEN TABLES where In_use > 0;
				
				查看被锁住的
				SELECT * FROM INFORMATION_SCHEMA.INNODB_LOCKS; 
				
				等待锁定
				SELECT * FROM INFORMATION_SCHEMA.INNODB_LOCK_WAITS; 
				
				查看表索引信息
				SHOW INDEX FROM account;

				cmd：
					连接：mysql -h主机地址 -u用户名 －p用户密码 （注:u与root可以不用加空格，其它也一样） 
					断开：exit （回车） 
					
					创建授权：grant select on 数据库.* to 用户名@登录主机 identified by \"密码\" 
					修改密码：mysqladmin -u用户名 -p旧密码 password 新密码 
					删除授权: revoke select,insert,update,delete om *.* from test2@localhost; 
					
					显示数据库：show databases; 
					显示数据表：show tables; 
					显示表结构：describe 表名; 
					
					创建库：create database 库名; 
					删除库：drop database 库名; 
					使用库：use 库名; 
					
					创建表：create table 表名 (字段设定列表); 
					删除表：drop table 表名; 
					修改表：alter table t1 rename t2 
					查询表：select * from 表名; 
					清空表：delete from 表名; 
					备份表: mysqlbinmysqldump -h(ip) -uroot -p(password) databasename tablename > tablename.sql 
					恢复表: mysqlbinmysql -h(ip) -uroot -p(password) databasename tablename < tablename.sql（操作前先把原来表删除） 
					
					增加列：ALTER TABLE t2 ADD c INT UNSIGNED NOT NULL AUTO_INCREMENT,ADD INDEX (c); 
					修改列：ALTER TABLE t2 MODIFY a TINYINT NOT NULL, CHANGE b c CHAR(20); 
					删除列：ALTER TABLE t2 DROP COLUMN c; 
					
					备份数据库：mysql\bin\mysqldump -h(ip) -uroot -p(password) databasename > database.sql 
					恢复数据库：mysql\bin\mysql -h(ip) -uroot -p(password) databasename < database.sql 
					复制数据库：mysql\bin\mysqldump --all-databases > all-databases.sql 
					修复数据库：mysqlcheck -A -o -uroot -p54safer 
					
					文本数据导入： load data local infile \"文件名\" into table 表名; 
					数据导入导出：mysql\bin\mysqlimport database tables.txt
					
					主键：primary key
					
					自增：auto_increment
					
					multiQueriesEnabled为true，则jdbc支持执行的一条语句中包含多条由分号分割的语句。
					rewriteBatchedStatements=true才是jdbc实现批量操作的关键。
			*/
			db.execute("unlock tables;");
			System.out.println("结束");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
