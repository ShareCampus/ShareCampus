package com.shareCampus.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shareCampus.dto.Result;
import com.shareCampus.entity.VenueType;
import com.shareCampus.mapper.VenueTypeMapper;
import com.shareCampus.service.IVenueTypeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class VenueTypeServiceImpl extends ServiceImpl<VenueTypeMapper, VenueType> implements IVenueTypeService {

    @Override
    public Result queryList() {
        List<VenueType> typeList = this.query().orderByAsc("sort").list();
        List<String> venueTypeList = new ArrayList<>();
        for (VenueType type: typeList){
            venueTypeList.add(JSONUtil.toJsonStr(type));
        }
        return Result.ok(typeList);
    }
}
