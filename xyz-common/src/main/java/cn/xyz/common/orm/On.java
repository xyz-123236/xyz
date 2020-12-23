package cn.xyz.common.orm;

import cn.xyz.common.tools.Tools;

public class On  extends Condition<Or> {

    public On or(And and){
        if(!Tools.isEmpty(this.sql.toString())) this.sql.append(ON);
        this.sql.append(LPAREN).append(and.getSql()).append(RPAREN);
        return this;
    }
}
