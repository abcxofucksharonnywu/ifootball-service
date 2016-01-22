package com.abcxo.ifootball.service.controllers;

import com.abcxo.ifootball.service.models.Data;
import com.abcxo.ifootball.service.repos.DataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by shadow on 15/11/15.
 */
@RestController
public class DataController {
    @Autowired
    private DataRepo dataRepo;

    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public Data get(@RequestParam("uid") long uid, @RequestParam("name") String name,
                    @RequestParam("category") String category) {
        Data data = dataRepo.findByNameAndCategory(name, category);
        if (data == null) {
            data = new Data();
        }
        return data;
    }


}
