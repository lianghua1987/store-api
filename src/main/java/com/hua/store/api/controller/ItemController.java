package com.hua.store.api.controller;

import com.hua.store.api.service.ItemService;
import com.hua.store.common.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemController {

    @Autowired
    private ItemService service;

    @RequestMapping(value = "/item/info/{itemId}")
    @ResponseBody
    public Result getItemBaseInfo(@PathVariable Long itemId) {
        return service.getItemBaseInfo(itemId);
    }

    @RequestMapping(value = "/item/desc/{itemId}")
    @ResponseBody
    public Result getItemDescription(@PathVariable Long itemId) {
        return service.getItemDescription(itemId);
    }

    @RequestMapping(value = "/item/param/{itemId}")
    @ResponseBody
    public Result getItemParameter(@PathVariable Long itemId) {
        return service.getItemParameter(itemId);
    }
}
