package com.hs.maritime.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hs.maritime.entity.Category;
import com.hs.maritime.mapper.CategoryMapper;
import com.hs.maritime.service.CategoryService;
import org.springframework.stereotype.Service;

/**
 * 图书分类业务实现类
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
