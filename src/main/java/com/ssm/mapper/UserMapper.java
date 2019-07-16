package com.ssm.mapper;

import com.ssm.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    User queryUserByNameAndPwd(@Param("username") String username,@Param("password") String password);

    List<User> selectUsers(@Param("uname") String username);

    boolean addUser(User user);

    User selectUserByUid(Integer id);

    int deleteBacth(@Param("ids") String[] array);
    int selectCountUsers(@Param("name") String account);
    List<User>selectUserPages(@Param("name") String account,
                              @Param("begin") int currentPage,
                              @Param("limit") int pageSize);

    int updateHeadImage(@Param("uid")long uid,@Param("url")String url);
    String selectHeadImage(Long uid);
}
