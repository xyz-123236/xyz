package cn.xyz.test.test;

import cn.xyz.common.orm.And;
import cn.xyz.common.orm.DbTool;
import cn.xyz.common.orm.Or;
import org.apache.commons.lang3.StringUtils;

public class T012 {

    public static void main(String[] args) {
        String[] s = new String[5];
        int[] i = new int[5];

        Integer[] ie = new Integer[5];
        print(s);
        print(i);
        print(ie);
        String sql = new And().or(new Or().like("beginno","beginno").or(
                new And().le("beginno","beginno").ge("endno","beginno")
        )).getSql();
        System.out.println(sql);
        sql = DbTool.getInstance().createSelectSql("test").where().le("beginno","beginno").ge("endno","beginno").getSql();
        System.out.println("2----"+sql);
        sql = DbTool.getInstance().createSelectSql("test").where(new And().or(new Or().like("beginno","beginno").or(
                new And().le("beginno","beginno").ge("endno","beginno")
        )).getSql()).getSql();
        System.out.println(sql);
        sql = new And().eq("1","1").getSql();
        System.out.println(sql);
        sql = new And().or(
                new Or().or(new And().isNotEmpty("test","").isEmpty("test","").eq("beginno","beginno").eq("endno","endno").nin("test",new String[]{"a","b","c"}))
                        .or(new And().eqN("beginno","beginno").eq("endno","endno").in("test",new Integer[]{1,2,3}))
                        .or(new And().eq("beginno","beginno").eqN("endno","endno").in("test","1,2,3").nin("test","'a','b','c'"))
        ).eq("1","1").getSql();
        System.out.println(sql);
        sql = new Or().eq("1","1").or(
                new And().eq("beginno","beginno").eq("endno","endno")
        ).getSql();
        System.out.println(sql);
        System.out.println(new Or().eq("abc","123").eq("xyz","456").getSql());
        System.out.println(new And().eq("abc","123").eq("xyz","456").getSql());
        //JSONArray orders = new JSONArray();
        //System.out.println("orders.getJSONObject(0) = " + orders.getJSONObject(0));
        //System.out.println("1.0/0.0 = " + 1.0 / 0.0);
        //System.out.println("1.0/0.0 = " + 1 / 0);
        System.out.println("StringUtils.isBlank(\"  \") = " + StringUtils.isBlank("  "));
        System.out.println("StringUtils.isBlank(\"  \") = " + StringUtils.defaultIfBlank("  ","abf"));

    }

    public static void print(Object obj){
        if(obj instanceof  String[]){
            System.out.println("string");
        }
        if(obj instanceof int[]){
            System.out.println("int");
        }
        if(obj instanceof Integer[]){
            System.out.println("Integer");
        }
        if(obj.getClass().isArray()){
            System.out.println("isArray");
        }
    }
    /*static class Or{
        public String sql = "";
        public Or eq(String key, String value){
            if(!sql.equals("")) sql += " or ";
            sql += key + " = " + value;
            return this;
        }
        public Or or(And and){
            if(!sql.equals("")) sql += " or ";
            sql += and.getSql();
            return this;
        }
        public String getSql(){
            return this.sql;
        }
    }
    static class And{
        public String sql = "";
        public And eq(String key, String value){
            if(!sql.equals("")) sql += " and ";
            sql += key + " = " + value;
            return this;
        }
        public And and(Or or){
            if(!sql.equals("")) sql += " and ";
            sql += or.getSql();
            return this;
        }
        public String getSql(){
            return this.sql;
        }
    }*/
}
