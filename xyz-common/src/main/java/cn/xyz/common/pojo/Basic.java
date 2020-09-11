package cn.xyz.common.pojo;

public class Basic {
	protected int page;// 开始页
	protected int rows;// 页面容量
	protected String sort;//排序的列
	protected String order;//升序还是降序
	protected String jsp_name; //请求页面
	protected String create_by;
	protected String create_date;
	protected String update_by;
	protected String update_date;
	protected String remark;
	
	public int getPage() {
		return this.page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return this.rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public String getSort() {
		return this.sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getOrder() {
		return this.order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getJsp_name() {
		return this.jsp_name;
	}
	public void setJsp_name(String jsp_name) {
		this.jsp_name = jsp_name;
	}
	public String getCreate_by() {
		return this.create_by;
	}
	public void setCreate_by(String create_by) {
		this.create_by = create_by;
	}
	public String getCreate_date() {
		return this.create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getUpdate_by() {
		return this.update_by;
	}
	public void setUpdate_by(String update_by) {
		this.update_by = update_by;
	}
	public String getUpdate_date() {
		return this.update_date;
	}
	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}
	public String getRemark() {
		return this.remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
