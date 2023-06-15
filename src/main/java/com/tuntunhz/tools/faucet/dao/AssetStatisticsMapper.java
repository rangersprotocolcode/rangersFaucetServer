package com.tuntunhz.tools.faucet.dao;

import com.tuntunhz.tools.faucet.pojo.AccountInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AssetStatisticsMapper {

    Integer increaseCoinFunded();

    Integer getRPGFunded();

}
