package cn.xyz.test.testdb;

import cn.xyz.common.orm.And;
import cn.xyz.common.orm.DbBase;
import cn.xyz.common.orm.DbTool;
import com.alibaba.fastjson.JSONObject;

public class T007 {
    public static void main(String[] args) {
        DbBase db;
        try {
            db = DbBase.getJdbc(DbBase.MYSQL);
            System.out.println(db.select("select * from user where userid = ?", "tang0.wu' or 1 = 1 #"));
            JSONObject obj = new JSONObject();
            obj.put("userid", "tang.wu' or 1 = 1 #");
            //System.out.println(db.select(DbTool.getInstance().createSelectSql("user").where(new And().eq("userid",obj)).getSql()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
