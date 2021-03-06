package cn.xyz.common.pojo;

import cn.xyz.common.pojo.html.Td;
import com.alibaba.fastjson.JSONArray;

import java.util.List;

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
	private List<Td> tds;

	public String getFile_name() {
		return this.file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getFile_path() {
		return this.file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	public String getSheet_name() {
		return this.sheet_name;
	}

	public void setSheet_name(String sheet_name) {
		this.sheet_name = sheet_name;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String[] getHeads() {
		return this.heads;
	}

	public void setHeads(String[] heads) {
		this.heads = heads;
	}

	public String[] getFileds() {
		return this.fileds;
	}

	public void setFileds(String[] fileds) {
		this.fileds = fileds;
	}

	public JSONArray getData() {
		return this.data;
	}

	public void setData(JSONArray data) {
		this.data = data;
	}

	public Integer[] getFormats() {
		return this.formats;
	}

	public void setFormats(Integer[] formats) {
		this.formats = formats;
	}

	public Integer[] getWidths() {
		return this.widths;
	}

	public void setWidths(Integer[] widths) {
		this.widths = widths;
	}

	public Integer[] getLocks() {
		return this.locks;
	}

	public void setLocks(Integer[] locks) {
		this.locks = locks;
	}
}
