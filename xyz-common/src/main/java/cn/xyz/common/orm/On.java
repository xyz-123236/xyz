package cn.xyz.common.orm;

import cn.xyz.common.tools.Tools;

public class On  extends Condition<Or> {

    public On or(And and){
        if(!Tools.isEmpty(sql.toString())) sql.append(ON);
        sql.append(LPAREN).append(and.getSql()).append(RPAREN);
        return this;
    }
}
