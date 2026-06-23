package com.hs.maritime.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hs.maritime.entity.BorrowRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

public interface BorrowRecordMapper extends BaseMapper<BorrowRecord> {

    /***
     * 统计用户借阅，归还，逾期，赔偿数量
     */
    @Select("select count(*) as totalCount," +
            "IFNULL(sum(case when status = 0 then 1 else 0 end),0) as borrowingCount," +
            "IFNULL(sum(case when status = 1 then 1 else 0 end),0) as returnedCount," +
            "IFNULL(sum(case when status = 2 then 1 else 0 end),0) as overdueCount," +
            "IFNULL(sum(case when status = 5 then 1 else 0 end),0) as compensateCount " +
            "from borrow_records where user_id = #{userId}")
    Map<String,Object> selectUserBorrowStats(@Param("userId")Long userId);

}
