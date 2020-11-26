package cn.xyz.common.pojo;

import com.alibaba.fastjson.JSONArray;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Excel {
	private String file_name;//文件名
	private String file_path;//文件路径
	private String sheet_name;//excel表名
	private String title;//标题
	private String[] heads;//表头
	private String[] fileds;
	private JSONArray data;//数据
	private Integer[] formats;//格式化:字符串11/12/13，整数20，小数31/32/33/34
	private Integer[] widths;//宽度
	private Integer[] locks;//锁行

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	public String getSheet_name() {
		return sheet_name;
	}

	public void setSheet_name(String sheet_name) {
		this.sheet_name = sheet_name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String[] getHeads() {
		return heads;
	}

	public void setHeads(String[] heads) {
		this.heads = heads;
	}

	public String[] getFileds() {
		return fileds;
	}

	public void setFileds(String[] fileds) {
		this.fileds = fileds;
	}

	public JSONArray getData() {
		return data;
	}

	public void setData(JSONArray data) {
		this.data = data;
	}

	public Integer[] getFormats() {
		return formats;
	}

	public void setFormats(Integer[] formats) {
		this.formats = formats;
	}

	public Integer[] getWidths() {
		return widths;
	}

	public void setWidths(Integer[] widths) {
		this.widths = widths;
	}

	public Integer[] getLocks() {
		return locks;
	}

	public void setLocks(Integer[] locks) {
		this.locks = locks;
	}
}
