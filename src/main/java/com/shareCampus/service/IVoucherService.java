package com.shareCampus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shareCampus.dto.Result;
import com.shareCampus.entity.Voucher;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
public interface IVoucherService extends IService<Voucher> {

    Result queryVoucherOfShop(Long shopId);

    void addSeckillVoucher(Voucher voucher);
}
