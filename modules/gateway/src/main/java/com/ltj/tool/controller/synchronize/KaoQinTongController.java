package com.ltj.tool.controller.synchronize;

import com.ltj.tool.handler.KaoQinTongHandler;
import com.ltj.tool.model.dailyreports.DailyReports;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 融商云 controller
 *
 * @author Liu Tian Jun
 * @date 2021-03-16 09:43
 */

@RestController
@RequestMapping("/api/syn/kaoqintong")
public class KaoQinTongController {

    private final KaoQinTongHandler kaoQinTongHandler;

    public KaoQinTongController(KaoQinTongHandler kaoQinTongHandler) {
        this.kaoQinTongHandler = kaoQinTongHandler;
    }

    @GetMapping
    public DailyReports lookupMonthlyReports() {
        return kaoQinTongHandler.lookupMonthlyReports();
    }

}
