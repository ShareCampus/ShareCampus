package com.shareCampus.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shareCampus.dto.Result;
import com.shareCampus.entity.Venue;
import com.shareCampus.mapper.VenueMapper;
import com.shareCampus.service.IVenueService;
import com.shareCampus.utils.CacheClient;
import com.shareCampus.utils.RedisConstants;
import com.shareCampus.utils.RedisData;
import com.shareCampus.utils.SystemConstants;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.shareCampus.utils.RedisConstants.SHOP_GEO_KEY;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class VenueServiceImpl extends ServiceImpl<VenueMapper, Venue> implements IVenueService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CacheClient cacheClient;

    @Override
    public Result queryById(Long id){
        //缓存穿透
//        Shop shop = queryWithPassThrough(id);
        Venue venue = cacheClient.queryWithPassThrough(
                RedisConstants.CACHE_SHOP_KEY, id, Venue.class, this::getById,
                RedisConstants.CACHE_SHOP_TTL,TimeUnit.MINUTES
                );
        //缓存击穿
        //Shop shop = queryWithMutex(id);
        //逻辑过期
//        Shop shop = cacheClient.queryWithLogicalExpire(
//                RedisConstants.CACHE_SHOP_KEY,id,Shop.class,this::getById,
//                RedisConstants.CACHE_SHOP_TTL,TimeUnit.MINUTES
//        );
        log.debug("shop:"+ venue);
        if (venue == null){
            return Result.fail("店铺不存在");
        }
        return Result.ok(venue);

    }

//    public Shop queryWithMutex(Long id) {
//        // 从redis查缓存
//        String shopJson = stringRedisTemplate.opsForValue().get(RedisConstants.CACHE_SHOP_KEY + id.toString());
//        // 判断是否存在
//        if (StrUtil.isNotBlank(shopJson)) {
//            // 存在，直接返回，
//            Shop shop = JSONUtil.toBean(shopJson, Shop.class);
//            stringRedisTemplate.expire(RedisConstants.CACHE_SHOP_KEY + id,
//                    RedisConstants.CACHE_SHOP_TTL, TimeUnit.MINUTES);
//            return shop;
//        }
//        if (shopJson != null) {
//            return null;
//        }
//        //实现缓存重建
//        //获取互斥锁
//        String lockkey = RedisConstants.LOCK_SHOP_KEY + id;
//        Shop shop = null;
//        try {
//            boolean isLock = tryLock(lockkey);
//            //判断是否获取成功
//            while (!isLock) {
//                //失败，则休眠并重试
//                Thread.sleep(50L);
//                isLock = tryLock(lockkey);
//            }
//
//
//            //成功，根据id查询数据库
//            shop = getById(id);
//            //模拟重建延时
//            //Thread.sleep(200);
//            // 不存在，根据ID查数据库
//
//            // 不存在，返回错误
//            if (shop == null) {
//                stringRedisTemplate.opsForValue().set(
//                        RedisConstants.CACHE_SHOP_KEY + id.toString(), "",
//                        RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
//                return null;
//            }
//            //存在，写入redis
//            stringRedisTemplate.opsForValue().set(
//                    RedisConstants.CACHE_SHOP_KEY + id.toString(), JSONUtil.toJsonStr(shop),
//                    RedisConstants.CACHE_SHOP_TTL, TimeUnit.MINUTES);
//
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } finally {
//            unlock(lockkey);
//            return shop;
//        }
//
//
//    }
//    public Shop queryWithPassThrough(Long id){
//        // 从redis查缓存
//        String shopJson = stringRedisTemplate.opsForValue().get(RedisConstants.CACHE_SHOP_KEY + id.toString());
//
//        // 判断是否存在
//        if (StrUtil.isNotBlank(shopJson)) {
//            // 存在，直接返回，
//            Shop shop = JSONUtil.toBean(shopJson,Shop.class);
//            stringRedisTemplate.expire(RedisConstants.CACHE_SHOP_KEY + id,
//                    RedisConstants.CACHE_SHOP_TTL,TimeUnit.MINUTES);
//            return shop;
//        }
//        if (shopJson != null){
//            return null;
//        }
//        // 不存在，根据ID查数据库
//        Shop shop = getById(id);
//        // 不存在，返回错误
//        if (shop == null){
//            stringRedisTemplate.opsForValue().set(
//                    RedisConstants.CACHE_SHOP_KEY + id,"",
//                    RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
//            return null;
//        }
//        //存在，写入redis
//        stringRedisTemplate.opsForValue().set(
//                RedisConstants.CACHE_SHOP_KEY + id,JSONUtil.toJsonStr(shop),
//                RedisConstants.CACHE_SHOP_TTL, TimeUnit.MINUTES);
//
//        return shop;
//    }
//    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);
//    public Shop queryWithLogicalExpire(Long id){
//        // 从redis查缓存
//        String shopJson = stringRedisTemplate.opsForValue().get(RedisConstants.CACHE_SHOP_KEY + id);
//        //log.debug("shopJson"+shopJson);
//        // 判断是否存在
//        if (StrUtil.isBlank(shopJson)) {
//            // 不存在，直接返回，
//            return null;
//        }
//        // 命中，需要判断过期时间
//        RedisData redisData = JSONUtil.toBean(shopJson, RedisData.class);
//        JSONObject data = (JSONObject) redisData.getData();
//        Shop shop = JSONUtil.toBean(data, Shop.class);
//        LocalDateTime expireTime = redisData.getExpireTime();
//        //log.debug("expireTime: "+expireTime);
//        if (expireTime.isAfter(LocalDateTime.now())){
//            //log.debug("AfterNow");
//            return shop;
//        }
//        String lockKey = RedisConstants.LOCK_SHOP_KEY+id;
//        boolean islock = tryLock(lockKey);
//        if (islock){
//            //开启独立线程实现重建
//            CACHE_REBUILD_EXECUTOR.submit(
//                    ()->{
//                        try {
//                            this.saveShop2Redis(id, (long) 3600L);
//                        } catch (Exception e) {
//                            throw new RuntimeException(e);
//                        } finally {
//                            unlock(lockKey);
//                        }
//                    }
//            );
//        }
//        return shop;
//    }
//
//    private boolean tryLock(String key){
//        boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key,"1",RedisConstants.LOCK_SHOP_TTL,TimeUnit.SECONDS);
//        return BooleanUtil.isTrue(flag);
//    }
//
//    private  void  unlock(String key){
//        stringRedisTemplate.delete(key);
//    }

    public void saveShop2Redis(Long id,Long expireSeconds){
        Venue venue = getById(id);
        RedisData redisData = new RedisData();
        redisData.setData(venue);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(expireSeconds));
        stringRedisTemplate.opsForValue().set(RedisConstants.CACHE_SHOP_KEY+id,JSONUtil.toJsonStr(redisData));
    }
    @Override
    @Transactional
    public Result updateByIdWithCache(Venue venue) {
        //更新数据库
        updateById(venue);
        //删除缓存
        Long id = venue.getId();
        if (id == null){
            return Result.fail("店铺id为空");
        }
        stringRedisTemplate.delete(RedisConstants.CACHE_SHOP_KEY+id);
        return Result.ok();
    }

    @Override
    public Result queryVenueByType(Integer typeId, Integer current, Double x, Double y) {
        // 判断是否需要根据坐标查询
        if (x == null || y == null){
            Page<Venue> page = query()
                    .eq("type_id", typeId)
                    .page(new Page<>(current, SystemConstants.DEFAULT_PAGE_SIZE));
            return Result.ok(page.getRecords());
        }
        int from = (current-1)*SystemConstants.DEFAULT_PAGE_SIZE;
        int end = (current)*SystemConstants.DEFAULT_PAGE_SIZE;
        String key = SHOP_GEO_KEY+typeId;
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = stringRedisTemplate.opsForGeo()
                .search(key,
                        GeoReference.fromCoordinate(x, y),
                        new Distance(5000),
                        RedisGeoCommands.GeoSearchCommandArgs.newGeoSearchArgs().includeDistance().limit(end)
                );
        if (results == null){
            return Result.ok(Collections.emptyList());
        }
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> list = results.getContent();
        if (list.size()<=from){
            return  Result.ok(Collections.emptyList());
        }
        List<Long> ids = new ArrayList<>(list.size());
        Map<String,Distance> distanceMap = new HashMap<>(list.size());
        list.stream().skip(from).forEach(result->{
            String shopIdStr = result.getContent().getName();
            ids.add(Long.valueOf(shopIdStr));
            Distance dis = result.getDistance();
            distanceMap.put(shopIdStr,dis);
        });
        if (list.size()==0){
            return Result.ok(Collections.emptyList());
        }
        String idStr = StrUtil.join(",", ids);
        List<Venue> venues = query().in("id", ids).last("ORDER BY FIELD(id, "+idStr+")").list();
        for (Venue venue : venues){
            venue.setDistance(distanceMap.get(venue.getId().toString()).getValue());
        }
        return Result.ok(venues);
    }
}
