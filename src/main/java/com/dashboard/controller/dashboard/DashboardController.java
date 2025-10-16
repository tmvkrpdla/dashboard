package com.dashboard.controller.dashboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {


    @RequestMapping(value = "/dashboard")
    public ModelAndView dashboard(ModelAndView mav) {

        mav.setViewName("dashboard/dashboard");

        return mav;
    }

    @RequestMapping(value = "/dashboard2")
    public ModelAndView dashboard2(ModelAndView mav) {

        mav.setViewName("dashboard/dashboard2");

        return mav;
    }

    @RequestMapping(value = "/dashboard3")
    public ModelAndView dashboard3(ModelAndView mav) {

        mav.setViewName("dashboard/dashboard3");

        return mav;
    }

    @RequestMapping(value = "/dashboard4")
    public ModelAndView dashboard4(ModelAndView mav) {

        mav.setViewName("dashboard/dashboard4");

        return mav;
    }

}
