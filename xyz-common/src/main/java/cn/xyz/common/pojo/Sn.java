package cn.xyz.common.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Sn {
	private String snFrom;//开始序号
	private String snTo;//结束序号
	private Integer number = 1;//数量
	private Integer radix;//进制
	private String range = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";//范围
	private String position;//变化位置
	private Integer beginIndex;//开始位置
	private Integer endIndex;//结束位置
	private Integer jump = 1;//跳号数量
	private Integer limit = 50000;//数量限制
	private String field;//生成的序列字段名
	public String getSnFrom() {
		return this.snFrom;
	}
	public void setSnFrom(String snFrom) {
		this.snFrom = snFrom;
	}
	public String getSnTo() {
		return this.snTo;
	}
	public void setSnTo(String snTo) {
		this.snTo = snTo;
	}
	public Integer getNumber() {
		return this.number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public Integer getRadix() {
		return this.radix;
	}
	public void setRadix(Integer radix) {
		this.radix = radix;
	}
	public String getRange() {
		return this.range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public String getPosition() {
		return this.position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public Integer getBeginIndex() {
		return this.beginIndex;
	}
	public void setBeginIndex(Integer beginIndex) {
		this.beginIndex = beginIndex;
	}
	public Integer getEndIndex() {
		return this.endIndex;
	}
	public void setEndIndex(Integer endIndex) {
		this.endIndex = endIndex;
	}
	public Integer getJump() {
		return this.jump;
	}
	public void setJump(Integer jump) {
		this.jump = jump;
	}
	public Integer getLimit() {
		return this.limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public String getField() {
		return this.field;
	}
	public void setField(String field) {
		this.field = field;
	}
	
	
}
