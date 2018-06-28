package com.hua.store.api.service;

import com.hua.store.common.pojo.Result;

public interface ItemService {

    public Result getItemBaseInfo(Long itemId);
    public Result getItemDescription(Long itemId);
    public Result getItemParameter(Long itemId);
}
