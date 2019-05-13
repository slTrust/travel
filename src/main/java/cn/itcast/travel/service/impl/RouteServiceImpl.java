package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao = new RouteDaoImpl();
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize) {
        // 封装 PageBean
        PageBean<Route> pb = new PageBean<Route>();
        // 设置当前页码
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);
        // 设置总记录数量
        int totalCount = routeDao.findTotalCount(cid);
        pb.setTotalCount(totalCount);

        // 设置当前页面显示的集合
        int start = (currentPage - 1) * pageSize;// 开始的记录数
        List<Route> list = routeDao.findByPage(cid,start,pageSize);
        pb.setList(list);
        // 设置总页数量
        int totalPage = totalCount % pageSize ==0? totalCount / pageSize : (totalCount / pageSize) + 1 ;
        pb.setTotalPage(totalPage);
        return pb;
    }
}
