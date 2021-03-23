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

}
