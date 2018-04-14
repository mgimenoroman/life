package com.mgr.life.plan;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.mgr.life.plan.PlanController.END_POINT;

@RestController()
@RequestMapping(path = END_POINT)
@Api(value = "plan", description = "Actions regarding plans.")
public class PlanController {

    static final String END_POINT = "/plan";

    @RequestMapping(method = RequestMethod.GET)
    public Plan planGet() {
        return new Plan("TestName");
    }

    @RequestMapping(method = RequestMethod.POST)
    public String planPost(@RequestBody() Plan plan) {
        return "Name is " + plan.getName();
    }
}
