package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {
    /**
     * 根据 cid查询总记录数
     * @return
     */
    public int findTotalCount(int cid);

    /**
     * 根据cid,start,pageSize查询当前数据集合
     * @param cid
     * @param start
     * @param pageSize
     * @return
     */
    public List<Route> findByPage(int cid, int start, int pageSize);
}
