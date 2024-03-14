package com.sharecampus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sharecampus.entity.BlogComments;
import com.sharecampus.mapper.BlogCommentsMapper;
import com.sharecampus.service.IBlogCommentsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class BlogCommentsServiceImpl extends ServiceImpl<BlogCommentsMapper, BlogComments> implements IBlogCommentsService {

}
