package cn.xyz.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class MyServiceImpl<M extends BaseMapper<T>, T> {

    @Autowired
    protected M baseMapper;

    public M getBaseMapper() {
        return baseMapper;
    }
}
