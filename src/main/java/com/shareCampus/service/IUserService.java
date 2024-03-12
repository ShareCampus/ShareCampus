package com.shareCampus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shareCampus.dto.LoginFormDTO;
import com.shareCampus.dto.RegisterFormDTO;
import com.shareCampus.dto.Result;
import com.shareCampus.entity.User;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
public interface IUserService extends IService<User> {

    Result sendCode(String phone, HttpSession session);

    Result login(LoginFormDTO loginForm, HttpSession session);

    Result sign();

    Result signCount();

    Result register(RegisterFormDTO registerForm);

    Result logout(String token);
}
