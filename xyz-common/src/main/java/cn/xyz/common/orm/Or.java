package cn.xyz.common.orm;

import cn.xyz.common.tools.Tools;

public class Or extends Condition<Or> {

    public Or or(And and){
        if(!Tools.isEmpty(this.sql.toString())) this.sql.append(OR);
        this.sql.append(LPAREN).append(and.getSql()).append(RPAREN);
        return this;
    }

}
