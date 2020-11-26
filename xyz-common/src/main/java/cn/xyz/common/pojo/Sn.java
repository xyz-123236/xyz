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
	
}
