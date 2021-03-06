package cn.xyz.common.pojo.html;

public class Td {
    private String filed;
    private String filed_name;
    private Integer width;
    private String align;

    public Td() {
    }

    public Td(String filed, String filed_name, Integer width, Integer align) {
        this.filed = filed;
        this.filed_name = filed_name;
        this.width = width;
        if (align == 1) {
            this.align = "left";
        } else if (align == 3) {
            this.align = "right";
        } else {
            this.align = "center";
        }
    }

    public String getFiled() {
        return filed;
    }

    public void setFiled(String filed) {
        this.filed = filed;
    }

    public String getFiled_name() {
        return filed_name;
    }

    public void setFiled_name(String filed_name) {
        this.filed_name = filed_name;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }
}
