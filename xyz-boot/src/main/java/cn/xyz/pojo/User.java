package cn.xyz.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

//数据库下划线可自动转驼峰,实体类字段必须要用驼峰，不然取不到数据（可配置）
//用实体类作为Wrapper参数，会使用@TableField(condition="%s&lt;#{%s}")生成条件
@EqualsAndHashCode(callSuper = true)//AR
@Data
@TableName("user")//别名
public class User extends Model<User> {//extends Model<User> =>>  AR

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)//主键默认为id,默认填充雪花算法，（不是id时，新增时不用@TableId则不会填充主键，不能新增）
	private Long u_id;
    @TableField(value = "name", condition = SqlCondition.LIKE)//别名
    private String name;
    @TableField(condition="%s&lt;#{%s}")
    private Integer age;
    private String email;
    private Long manager_id;
    private LocalDateTime create_time;
    @TableField(exist = false)//false不是数据库字段
    private transient String remark;
    //private static String remark;//静态只有一份
    //private transient String remark;//不参与序列化
}
