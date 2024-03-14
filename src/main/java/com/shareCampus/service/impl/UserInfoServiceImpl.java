package com.shareCampus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shareCampus.entity.UserInfo;
import com.shareCampus.mapper.UserInfoMapper;
import com.shareCampus.service.IUserInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

}
