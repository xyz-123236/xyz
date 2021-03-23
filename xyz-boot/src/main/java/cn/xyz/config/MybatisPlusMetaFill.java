package cn.xyz.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MybatisPlusMetaFill implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        if(metaObject.hasSetter("create_time")){//如果有这个字段就自动填充
            this.setFieldValByName("create_time", LocalDateTime.now(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object val = getFieldValByName("update_time", metaObject);
        if(val == null){//如果没有手动设置值，就自动填充
            this.setFieldValByName("update_time", LocalDateTime.now(), metaObject);
        }

    }
}
