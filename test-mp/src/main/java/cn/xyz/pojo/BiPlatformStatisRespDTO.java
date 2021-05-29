package cn.xyz.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class BiPlatformStatisRespDTO implements Serializable {

    private static final long serialVersionUID = 6070471415344415351L;

    @ExcelProperty(value = "统计字段", index = 1)
    private String param;

    /**
     * 参数的key
     */
    private String paramKey;

    /**
     * 参数描述
     */
    private String paramDesc;

    private String startDate;

    private String endDate;

    @ExcelProperty(value = "统计数据", index = 2)
    private String value;

    private String rateValue;

    private Long id;

    private Integer riseOrFall;

    public BiPlatformStatisRespDTO(String startDate, String paramKey, String value) {
        this.paramKey = paramKey;
        this.startDate = startDate;
        this.value = value;
    }

    public BiPlatformStatisRespDTO() {
    }
}
