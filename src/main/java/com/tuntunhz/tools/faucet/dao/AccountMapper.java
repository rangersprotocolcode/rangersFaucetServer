package com.tuntunhz.tools.faucet.dao;

import com.tuntunhz.tools.faucet.pojo.AccountInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AccountMapper {

    AccountInfo getAccountInfo(@Param("address") String address);

    Integer updateGotCoinTime(@Param("address") String address, @Param("gotCoinTime") String gotCoinTime);

    Integer updateGotNFTTime(@Param("address") String address, @Param("gotNFTTime") String gotNFTTime);
}
