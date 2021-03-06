package cn.xyz.common.pojo.html;

import com.alibaba.fastjson.JSONArray;

import java.util.List;

public class Table {
    private List<Td> tds;
    private JSONArray data;

    public List<Td> getTds() {
        return tds;
    }

    public void setTds(List<Td> tds) {
        this.tds = tds;
    }

    public JSONArray getData() {
        return data;
    }

    public void setData(JSONArray data) {
        this.data = data;
    }
}
