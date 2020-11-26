package cn.xyz.test.testdb;

import cn.xyz.common.orm.And;
import cn.xyz.common.orm.DbTool;
import com.alibaba.fastjson.JSONObject;

public class T006 {

    public static void main(String[] args) throws Exception {
        DbTool db = DbTool.getInstance();
        System.out.println(db.createSelectSql("t1").where(new And().eq("id",10)).select());
        JSONObject obj = new JSONObject();
        obj.put("id",2);
        obj.put("k","e");
        obj.put("v",5);
        System.out.println(db.createInsertSql("t5",obj,null).insert());
        JSONObject obj2 = new JSONObject();
        obj2.put("id",8);
        obj2.put("code","33");
        obj2.put("name","aa");
        System.out.println(db.createInsertSql("t4",obj2,null).insert());
    }
}
