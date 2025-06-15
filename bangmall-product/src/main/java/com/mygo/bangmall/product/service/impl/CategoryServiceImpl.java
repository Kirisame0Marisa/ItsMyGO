package com.mygo.bangmall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mygo.bangmall.product.service.CategoryBrandRelationService;
import com.mygo.bangmall.product.vo.Catelog2Vo;
import com.mysql.cj.util.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mygo.common.utils.PageUtils;
import com.mygo.common.utils.Query;

import com.mygo.bangmall.product.dao.CategoryDao;
import com.mygo.bangmall.product.entity.CategoryEntity;
import com.mygo.bangmall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;
    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    RedissonClient redisson;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> entities = baseMapper.selectList(null);

        List<CategoryEntity> level1Menus = entities.stream().filter(categoryEntity ->
                categoryEntity.getParentCid() == 0).map((menu)->{
            menu.setChildren(getChildren(menu, entities));
            return menu;

        }).sorted((menu1,menu2)->{

            return (menu1.getSort() == null?0:menu1.getSort()) - (menu2.getSort() == null?0:menu2.getSort());
        }).collect(Collectors.toList());


        return level1Menus;
    }

    @Override
    public void removeMenuByIds(List<Long> aslist) {
        baseMapper.deleteBatchIds(aslist);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
      List<Long> parentpath = findParentPath(catelogId, paths);
        Collections.reverse(parentpath);
        return parentpath.toArray(new Long[parentpath.size()]);
    }


    @Caching(evict = {
            @CacheEvict(value = {"category"},key = "'getLevel1Categorys'"),
            @CacheEvict(value = {"category"},key = "'getCatalogJson'")
    })
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());

    }

    @Cacheable(value = {"category"},key = "#root.method.name",sync = true)
    @Override
    public List<CategoryEntity> getLevel1Categorys() {
       List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
       return categoryEntities;
    }

    @Cacheable(value = {"category"},key = "#root.method")
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson(){
        List<CategoryEntity> selectList = baseMapper.selectList(null);
        // 查询所有一级分类
        List<CategoryEntity> level1Categorys = getParentCid(selectList, 0L);

        // 2 封装数据
        Map<String, List<Catelog2Vo>> parent_cid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
                    // 1 每一个的一级分类，查到这个一级分类的二级分类
                    List<CategoryEntity> categoryEntities = getParentCid(selectList,v.getCatId());
                    // 2 分装上面的结果
                    List<Catelog2Vo> catelog2Vos = null;

                    if (categoryEntities != null) {

                        catelog2Vos = categoryEntities.stream().map(l2 -> {
                            Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                            // 1 找当前二级分类的三级分类封装成vo
                            List<CategoryEntity> level3Catelog = getParentCid(selectList,l2.getCatId());
                            if (level3Catelog != null) {
                                List<Catelog2Vo.Catelog3Vo> collect = level3Catelog.stream().map(l3 -> {
                                    // 2 分装成指定格式
                                    Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                                    return catelog3Vo;
                                }).collect(Collectors.toList());
                                catelog2Vo.setCatalog3List(collect);
                            }
                            return catelog2Vo;
                        }).collect(Collectors.toList());
                    }
                    return catelog2Vos;

                }

        ));

        return parent_cid;
    }


    public Map<String, List<Catelog2Vo>> getCatalogJson2(){
        String catalogJson = redisTemplate.opsForValue().get("catalogJson");
        if(StringUtils.isNullOrEmpty(catalogJson)){
           Map<String,List<Catelog2Vo>> catalogJsonFromDb = getCatalogJsonFromDbWithRedisLock();

           return catalogJsonFromDb;
        }
        Map<String,List<Catelog2Vo>> result = JSON.parseObject(catalogJson,new TypeReference<Map<String,List<Catelog2Vo>>>(){});
        return result;
    }
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWithRedissonLock() {
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock","1111",300, TimeUnit.SECONDS);
        if(lock) {
            //redisTemplate.expire("lock",30,TimeUnit.SECONDS);
            Map<String,List<Catelog2Vo>> dataFromDb = getDataFromDb();
            redisTemplate.delete("lock");
            return dataFromDb;
        }else {
            return getCatalogJsonFromDbWithRedisLock();
        }



    }

    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWithRedisLock() {
            Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock","1111",300, TimeUnit.SECONDS);
            if(lock) {
                //redisTemplate.expire("lock",30,TimeUnit.SECONDS);
                Map<String,List<Catelog2Vo>> dataFromDb = getDataFromDb();
                redisTemplate.delete("lock");
                return dataFromDb;
            }else {
               return getCatalogJsonFromDbWithRedisLock();
            }



    }

    private Map<String, List<Catelog2Vo>> getDataFromDb() {
        String catalogJson = redisTemplate.opsForValue().get("catalogJson");
        if(StringUtils.isNullOrEmpty(catalogJson)){
            Map<String,List<Catelog2Vo>> result = JSON.parseObject(catalogJson,new TypeReference<Map<String,List<Catelog2Vo>>>(){});
            return result;
        }
        List<CategoryEntity> selectList = baseMapper.selectList(null);
        // 查询所有一级分类
        List<CategoryEntity> level1Categorys = getParentCid(selectList, 0L);

        // 2 封装数据
        Map<String, List<Catelog2Vo>> parent_cid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
                    // 1 每一个的一级分类，查到这个一级分类的二级分类
                    List<CategoryEntity> categoryEntities = getParentCid(selectList,v.getCatId());
                    // 2 分装上面的结果
                    List<Catelog2Vo> catelog2Vos = null;

                    if (categoryEntities != null) {

                        catelog2Vos = categoryEntities.stream().map(l2 -> {
                            Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                            // 1 找当前二级分类的三级分类封装成vo
                            List<CategoryEntity> level3Catelog = getParentCid(selectList,l2.getCatId());
                            if (level3Catelog != null) {
                                List<Catelog2Vo.Catelog3Vo> collect = level3Catelog.stream().map(l3 -> {
                                    // 2 分装成指定格式
                                    Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                                    return catelog3Vo;
                                }).collect(Collectors.toList());
                                catelog2Vo.setCatalog3List(collect);
                            }
                            return catelog2Vo;
                        }).collect(Collectors.toList());
                    }
                    return catelog2Vos;

                }

        ));
        String s = JSON.toJSONString( parent_cid);
        redisTemplate.opsForValue().set("catalogJson", s,1, TimeUnit.DAYS);
        return parent_cid;
    }

    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWithLocalLock() {
        synchronized(this){
            return getDataFromDb();
        }


    }

    private List<CategoryEntity> getParentCid(List<CategoryEntity> selectList,Long parent_cid) {
        List<CategoryEntity> collect =selectList.stream().filter(item->item.getParentCid() == parent_cid).collect(Collectors.toList());
        return collect;
    }

    private List<Long> findParentPath(Long catelogId,List<Long> paths) {
        paths.add(catelogId);
       CategoryEntity byId =  this.getById(catelogId);
       if(byId.getParentCid()!=0){
           findParentPath(byId.getParentCid(),paths);
       }
       return paths;
    }


    private  List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {

        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
                    return categoryEntity.getParentCid() == root.getCatId();
                }).map(categoryEntity -> {
                    categoryEntity.setChildren(getChildren(categoryEntity, all));
                    return categoryEntity;
        }).sorted((menu1,menu2)->{
            return (menu1.getSort() == null?0:menu1.getSort()) - (menu2.getSort() == null?0:menu2.getSort());
        }).collect(Collectors.toList());
        return children;
    }

}