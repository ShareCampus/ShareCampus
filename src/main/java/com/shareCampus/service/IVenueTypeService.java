package com.shareCampus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shareCampus.dto.Result;
import com.shareCampus.entity.VenueType;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface IVenueTypeService extends IService<VenueType> {
    Result queryList();
}
