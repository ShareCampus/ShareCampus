package com.shareCampus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shareCampus.entity.Voucher;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 */
public interface VoucherMapper extends BaseMapper<Voucher> {

    List<Voucher> queryVoucherOfShop(@Param("venueId") Long venueId);
}
