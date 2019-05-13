package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {

    private CategoryDao categoryDao = new CategoryDaoImpl();
    @Override
    public List<Category> findAll() {
        // 1. 从 redis里查询
        // 1.1 获取 jedis客户端
        Jedis jedis = JedisUtil.getJedis();
        // 期望数据库中存储的顺序就是展示的顺序
        // 1.2 使用sortedset排序查询
        Set<String> categorys = jedis.zrange("category",0,-1);

        // 2. 判断查询的集合是否为空
        List<Category> cs = null;
        if(categorys == null || categorys.size() ==0 ){
            System.out.println("从mysql");
            // 3. 如果为空，需要从数据库查询，在数据库存入redis,
            // 3.1 从数据库查询
            cs = categoryDao.findAll();
            // 3.2 将集合数据存储到redis中 category的key
            for (int i = 0; i < cs.size(); i++) {
                jedis.zadd("category",cs.get(i).getCid(),cs.get(i).getCname());
            }
        }else{
            System.out.println("从redis");
            // 4. 如果不为空，则直接返回
            // 4.1将set数据存入list
            cs = new ArrayList<Category>();
            for (String name:categorys) {
                Category category = new Category();
                category.setCname(name);
                cs.add(category);
            }
        }

        return cs;
    }
}
