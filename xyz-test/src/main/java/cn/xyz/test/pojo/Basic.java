package cn.xyz.test.pojo;

import lombok.Data;

@Data
public class Basic {
	protected Integer page;// 开始页
	protected Integer rows;// 页面容量
	protected String sort;//排序的列
	protected String order;//升序还是降序
	protected String jsp_name; //请求页面
	protected String create_by;
	protected String create_date;
	protected String update_by;
	protected String update_date;
	protected String remark;

}
