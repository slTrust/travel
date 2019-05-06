package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    @Override
    public boolean regist(User user) {
        //1.根据用户查询用户对象
        User u = userDao.findByUsername(user.getUsername());

        // u==null 不存在
        if(u != null){
            // 用户名存在 注册失败
            return false;
        }
        //2. 保存用户信息
        userDao.save(user);
        return true;
    }
}