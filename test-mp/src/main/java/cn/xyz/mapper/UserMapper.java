package cn.xyz.mapper;

import cn.xyz.pojo.User;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user ${ew.customSqlSegment}")
    List<User> find(@Param(Constants.WRAPPER)Wrapper<User> wrapper);

    List<User> selectAll(@Param(Constants.WRAPPER)Wrapper<User> wrapper);

    List<User> get(User user);

    IPage<User> findPage(Page<User> page, @Param(Constants.WRAPPER)Wrapper<User> wrapper);
}
