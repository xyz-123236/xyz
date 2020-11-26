package cn.xyz.common.orm;

import cn.xyz.common.tools.Tools;

public class Or extends Condition<Or> {

    public Or or(And and){
        if(!Tools.isEmpty(sql.toString())) sql.append(OR);
        sql.append(LPAREN).append(and.getSql()).append(RPAREN);
        return this;
    }

}
