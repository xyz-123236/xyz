package cn.xyz.mes;

import cn.xyz.common.orm.DbBase;

public class T020 {
    public static void main(String[] args) {
        try {
            DbBase db = DbBase.getJdbc(DbBase.SYBASE);
            db.execute(" EXEC abse_calc '2021' , 1 , '2021/01/31' ,'2021/01/31' , '0136985' , '0136985' , '' , 'Z' , '' , 'Z'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
