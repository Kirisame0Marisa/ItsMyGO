package com.mygo.bangmall.search.service;

import com.mygo.bangmall.search.vo.SearchParam;
import com.mygo.bangmall.search.vo.SearchResult;

public interface MallSearchService {
    SearchResult search(SearchParam param);
}
