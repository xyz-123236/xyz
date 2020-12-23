package cn.xyz.common.orm;

import cn.xyz.common.tools.Tools;

public class And extends Condition<And> {

	public And or(Or or){
		if(!Tools.isEmpty(this.sql.toString())) this.sql.append(AND);
		this.sql.append(LPAREN).append(or.getSql()).append(RPAREN);
		return this;
	}

}
