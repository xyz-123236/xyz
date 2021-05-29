package cn.xyz.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import lombok.Data;
import org.apache.poi.ss.usermodel.HorizontalAlignment;


@Data
//@ContentRowHeight()//内容行高
//@HeadRowHeight//表头行高
public class Student2 {
    @ExcelProperty(value = {"学员信息表","姓名"})
    private String name2;
    @ColumnWidth(30)
    @ExcelProperty(value = {"学员信息表","年级"})
    private String gender2;
    @DateTimeFormat("yyyy-MM-dd")
    private String birthday2;
    @ExcelProperty(value = "id", index = 1)
    @NumberFormat("#,##0.00")
    //@ExcelIgnore
    @ContentStyle(horizontalAlignment = HorizontalAlignment.RIGHT)
    private Double id2;
}
