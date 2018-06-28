package com.hua.store.api.service;

import com.hua.store.api.dao.JedisClient;
import com.hua.store.common.pojo.Result;
import com.hua.store.common.utils.JsonUtils;
import com.hua.store.mapper.ItemDescriptionMapper;
import com.hua.store.mapper.ItemMapper;
import com.hua.store.mapper.ItemParameterItemMapper;
import com.hua.store.pojo.*;
import org.apache.solr.common.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Value("${REDIS_ITEM_KEY}")
    private String REDIS_ITEM_KEY;

    @Value("${REDIS_ITEM_TTL}")
    private Integer REDIS_ITEM_TTL;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemDescriptionMapper itemDescriptionMapper;

    @Autowired
    private ItemParameterItemMapper itemParameterItemMapper;

    @Autowired
    private JedisClient client;

    @Override
    public Result getItemBaseInfo(Long itemId) {

        try {
            String json = client.get(REDIS_ITEM_KEY + ":" + itemId + ":base");
            if (!StringUtils.isEmpty(json)) {
                return Result.OK(JsonUtils.jsonToPojo(json, Item.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Item item = itemMapper.selectByPrimaryKey(itemId);

        try {
            client.set(REDIS_ITEM_KEY + ":" + itemId + ":base", JsonUtils.objectToJson(item));
            client.expire(REDIS_ITEM_KEY + ":" + itemId + ":base", REDIS_ITEM_TTL); //设置有效期
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Result.OK(item);
    }

    @Override
    public Result getItemDescription(Long itemId) {
        try {
            String json = client.get(REDIS_ITEM_KEY + ":" + itemId + ":description");
            if (!StringUtils.isEmpty(json)) {
                return Result.OK(JsonUtils.jsonToPojo(json, ItemDescription.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ItemDescription itemDescription = itemDescriptionMapper.selectByPrimaryKey(itemId);

        try {
            client.set(REDIS_ITEM_KEY + ":" + itemId + ":description", JsonUtils.objectToJson(itemDescription));
            client.expire(REDIS_ITEM_KEY + ":" + itemId + ":description", REDIS_ITEM_TTL); //设置有效期
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Result.OK(itemDescription);
    }

    @Override
    public Result getItemParameter(Long itemId) {
        try {
            String json = client.get(REDIS_ITEM_KEY + ":" + itemId + ":param");
            if (!StringUtils.isEmpty(json)) {
                return Result.OK(JsonUtils.jsonToPojo(json, ItemParameterItem.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ItemParameterItemExample example = new ItemParameterItemExample();
        ItemParameterItemExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<ItemParameterItem> itemParameterItems = itemParameterItemMapper.selectByExampleWithBLOBs(example);

        if (itemParameterItems != null & itemParameterItems.size() > 0) {
            try {
                ItemParameterItem item = itemParameterItems.get(0);
                client.set(REDIS_ITEM_KEY + ":" + itemId + ":param", JsonUtils.objectToJson(item));
                client.expire(REDIS_ITEM_KEY + ":" + itemId + ":param", REDIS_ITEM_TTL); //设置有效期
                return Result.OK(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Result.build(400, "无此商品！");


    }
}
