package com.lejia.mapper;

import com.github.abel533.mapper.Mapper;
import com.lejia.pojo.ConsumerLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ConsumerLogMapper extends Mapper<ConsumerLog>,BaseMapper<ConsumerLog>{


    void insertConsumerLog(@Param("userid") Integer userid, @Param("nickName") String nickName,
                           @Param("sex") long sex);


    List findConsumerLogByOpenId(@Param("openid") String openid);

    List findConsumerLogByUnionId(@Param("unionId") String unionId);

    List findConsumerLogByUserId(@Param("userid") Integer userid);

    List checkUnionIdByUserId(@Param("userid") Integer userid, @Param("appId") String appId);

    void updateConsumerLogToUserId(@Param("id") Integer id, @Param("unionId") String unionId, @Param("userId") Integer userId);
    void updateConsumerLogUnionId(@Param("id") Integer id, @Param("unionId") String unionId);
}
