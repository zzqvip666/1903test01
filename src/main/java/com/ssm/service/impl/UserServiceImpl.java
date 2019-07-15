package com.ssm.service.impl;

import com.ssm.mapper.UserMapper;
import com.ssm.pojo.PageBean;
import com.ssm.pojo.User;
import com.ssm.service.UserService;
import com.ssm.utils.MD5Utils;
import com.ssm.utils.StringUtils;
import jdk.nashorn.internal.runtime.UserAccessorProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String username, String password) {
        /**
         * 需要对密码加密之后进行查询
         */
        //调用mapper去实现登录
       User user= userMapper.queryUserByNameAndPwd(username,MD5Utils.encrypt(password));
        return user;
    }

    @Override
    public List<User> queryUsers(String username) {

        return userMapper.selectUsers(username);
    }

    @Override
    public boolean addUser(User user) {
        //校验用户名是否唯一 非空
        //1.对密码进行加密
        user.setPassword(MD5Utils.encrypt(user.getPassword()));
        //user.setCreateUserId();//从session里面取出当前登录的用户id
        //设置用户的头像
        /**
         * 扩展任务：给用户设置默认头像，
         * 可以在服务器上：static/defdaultheadimage/image里面存放5个默认头像
         * 头像1：http://localhost:80/static/defdaultheadimage/default001.png
         * 头像2：http://localhost:80/static/defdaultheadimage/default002.png
         * 头像3：http://localhost:80/static/defdaultheadimage/default003.png
         * 头像4：http://localhost:80/static/defdaultheadimage/default004.png
         * 头像5：http://localhost:80/static/defdaultheadimage/default005.png
         * 把这5个地址存储到List集合。
         * 随机生成一个下标0-4：取出里面的数据当做默认头像
         *
         */
        //user.setHeadImageUrl();
        user.setStatus(1);//默认是1 表示激活可用
        //设置用户创建的事件
        user.setCreateTime(new Date());

        return userMapper.addUser(user);//用户新增
    }

    @Override
    public User queryUserById(Integer id) {
        return userMapper.selectUserByUid(id);
    }

    @Override
    @Transactional
    public boolean deleteBacthUser(String ids) {
        if(StringUtils.isEmpty(ids)){
            return false;
        }
        String[] array = ids.split(",");

        if(array!=null&&array.length>0){
            //批量删除
            int row=userMapper.deleteBacth(array);
            return row>0;
        }

        return false;
    }

    /**
     * 分页显示所有数据
     * @param account
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public PageBean<User> userPages(String account, int currentPage, int pageSize) {
        PageBean<User> pb=new PageBean();
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);
        //计算总页数  int pageCount=total%pageSize==0?(total/pageSize):(total/pageSize+1)

        //设置总个数
        int total=userMapper.selectCountUsers(account);
        pb.setTotal(total);
        //设置分页数据
        List<User> list=userMapper.selectUserPages(account,(currentPage-1)*pageSize,pageSize);
        pb.setData(list);

        return pb;
    }

    @Override
    public boolean updateHeadImage(long uid, String url) {

        return userMapper.updateHeadImage(uid,url)>0;
    }

    @Override
    public String showHeadImage(Long uid) {
        return userMapper.selectHeadImage(uid);
    }
}
