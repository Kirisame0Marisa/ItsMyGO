package com.mygo.bangmall.search.controller;

import com.mygo.bangmall.search.service.MallSearchService;
import com.mygo.bangmall.search.vo.SearchParam;
import com.mygo.bangmall.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SearchController {
    @Autowired
    MallSearchService mallSearchService;
    @GetMapping("/list.html")
    public String listPage(SearchParam param, Model model, HttpServletRequest request) {
        param.set_queryString(request.getQueryString());
        SearchResult result = mallSearchService.search(param);
        model.addAttribute("result", result);
        return "list";
    }
}
