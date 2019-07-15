package com.ssm.controller;

import com.ssm.pojo.PageBean;
import com.ssm.pojo.ResultVo;
import com.ssm.pojo.User;
import com.ssm.service.UserService;
import com.ssm.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@ResponseBody
public class UserController {
    @Autowired
    private UserService userService;

    /**
     *
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Object login(String username,String password){
        //调用service层
       User user= userService.login(username,password);
        //返回
        if(user!=null){//登陆成功
                user.setPassword("");
            return ResultVo.success(user);

        }else{//登录失败

            return ResultVo.error();
        }

    }

    /**
     * 显示用户列表
     * @param userAccount
     * @return
     */
    @RequestMapping(value = "/users",method = RequestMethod.GET)
    public  Object queryAUsers(String userAccount){
        System.out.println("------------------->"+userAccount);
       List<User> list= userService.queryUsers(userAccount);
        return list;
    }

    @RequestMapping(value = "/user/add",method = RequestMethod.POST)
    public  Object userAdd(User user){
        System.out.println("------------------->"+user);
        boolean f= userService.addUser(user);
       if(f){

           return ResultVo.success();
       }else{
           return ResultVo.error("添加失败");
       }

    }
    /**
     * http://localhost:80/user/'+userId
     */
    @RequestMapping(value = "/user/{id}",method = RequestMethod.GET)
    public  Object userQuery(@PathVariable Integer id){
        System.out.println("------------------->"+id);
        User user= userService.queryUserById(id);
        if(user!=null){
            return ResultVo.success(user);
        }else{
            return ResultVo.error("服务器异常");
        }

    }

    /**
     * 接口地址：http://localhost:80/user/deletebatch
     * @return
     */
    @RequestMapping("/user/deletebacth")
    public  Object deleteBatch(String ids){
        //调用业务层
        boolean f=userService.deleteBacthUser(ids);
        if(f){

            return ResultVo.success();
        }else{
            return ResultVo.error("服务器异常");
        }
    }

    /**
     * http://localhost/userpages?userAccount=jack&currentPage=1&pageSize=3
     *
     * userAccount：非必须
     *
     * currentPage：非必须 default 1
     *
     * pageSize:非必须 default 10
     * @return
     */
    @RequestMapping(value = "/userpages",method = RequestMethod.GET)
    public  Object userPages(String userAccount,
                            @RequestParam(defaultValue = "1") int currentPage,
                             @RequestParam(defaultValue = "1") int pageSize){
      PageBean<User> pb= userService.userPages(userAccount,currentPage,pageSize);

      return pb;
    }

    /**
     * http://localhost:80/headimage
     */
    @RequestMapping(value="/headimage",produces = "application/json;charset=utf-8")
    public  String upload(@RequestParam MultipartFile file, Long userId, HttpServletRequest request) throws IOException {
        System.out.println(file.getOriginalFilename()+"---------"+userId);
        /**
         * 1.把图片上传到服务器
         * 2.修改用户的头像地址
         */
        //获取 服务器下的路径：/static/headimgs
        String dir = request.getServletContext().getRealPath("/static/headimgs");
        File fileDir = new File(dir);
        if(!fileDir.isDirectory()){//判读是否是文件夹
            //有可能是文件
            fileDir.delete();//删除文件
            fileDir.mkdirs();//创建文件夹
        }
        //非空则上传
        if(file.isEmpty()){
            return "上传失败";
        }
        //确定上传的名字
        String fileName= UUIDUtils.getUUID()+".png";
        //上传
        file.transferTo(new File(dir+"/"+fileName));
        //去修用户的头像
        String urlImg="http://ip:port/static/headimgs/名字";
       boolean row= userService.updateHeadImage(userId,"http://localhost:80/static/headimgs/"+fileName);

        return row ?("上传头像成功"):("上传失败");
    }
    /**
     * url:'http://localhost:80/headimage/'+vue.userId,
     */
    @RequestMapping("/headimage/{id}")
    public Object  getImageHead(@PathVariable Long id){
        String url =userService.showHeadImage(id);
        return ResultVo.success(url);
    }
}
