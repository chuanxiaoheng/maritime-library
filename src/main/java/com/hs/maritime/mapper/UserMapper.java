package com.hs.maritime.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hs.maritime.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    @Select("select u.id,u.username,u.nickname from users u " +
            "where status = 1 and role_id = 3 and " +
            "not exists(select 1 from library_cards as c where c.user_id = u.id) ")
    List<User> selectWithCardUserList();
}
