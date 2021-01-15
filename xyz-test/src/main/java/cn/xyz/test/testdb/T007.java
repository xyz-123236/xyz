package cn.xyz.test.testdb;

import cn.xyz.common.orm.DbBase;
import cn.xyz.common.orm.DbJdbc;

public class T007 {
    public static void main(String[] args) {
        DbBase db;
        try {
            db = DbBase.getJdbc(DbBase.MYSQL).startTransaction();
            System.out.println(db.select("select * from user"));
            db.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
