package com.sharecampus.controller;


import com.sharecampus.dto.Result;
import com.sharecampus.service.IVenueTypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@RestController
@RequestMapping("/venue-type")
public class VenueTypeController {
    @Resource
    private IVenueTypeService typeService;
    /*
    @GetMapping("list")
    public Result queryTypeList() {
        List<ShopType> typeList = typeService
                .query().orderByAsc("sort").list();
        return Result.ok(typeList);
    }*/
    @GetMapping("list")
    public Result queryTypeList() {
        return typeService.queryList();
    }
}
