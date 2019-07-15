package com.ssm.service;

import com.ssm.pojo.PageBean;
import com.ssm.pojo.User;

import java.util.List;

public interface UserService {
    User login(String username, String password);

    List<User> queryUsers(String username);

    boolean addUser(User user);

    User queryUserById(Integer id);

    boolean deleteBacthUser(String ids);
    PageBean<User> userPages(String account,int currentPage,int pageSize);
    boolean updateHeadImage(long uid,String url);
    String showHeadImage(Long uid) ;
}
