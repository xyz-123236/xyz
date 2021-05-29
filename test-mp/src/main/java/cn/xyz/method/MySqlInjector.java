package cn.xyz.method;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.extension.injector.methods.AlwaysUpdateSomeColumnById;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import com.baomidou.mybatisplus.extension.injector.methods.LogicDeleteByIdWithFill;

import java.util.List;

public class MySqlInjector extends DefaultSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        methodList.add(new DeleteAllMethod());
        //定义批量插入，排除逻辑字段和字段名为age不插入
        methodList.add(new InsertBatchSomeColumn(t -> !t.isLogicDelete() && !t.getColumn().equals("age")));
        //逻辑删除选装件:删除时可以修改某些字段，字段要加注解 @TableField(fill = FieldFill.UPDATE)
        methodList.add(new LogicDeleteByIdWithFill());
        //不更新name
        methodList.add(new AlwaysUpdateSomeColumnById(t -> !t.getColumn().equals("name")));
        return methodList;
    }
}
