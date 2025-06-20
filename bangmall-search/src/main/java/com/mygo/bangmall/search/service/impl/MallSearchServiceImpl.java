package com.mygo.bangmall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mygo.bangmall.search.config.BangmallElasticsearchConfig;
import com.mygo.bangmall.search.constant.EsConstant;
import com.mygo.bangmall.search.feign.ProductFeignService;
import com.mygo.bangmall.search.service.MallSearchService;
import com.mygo.bangmall.search.vo.AttrResponseVo;
import com.mygo.bangmall.search.vo.BrandVo;
import com.mygo.bangmall.search.vo.SearchParam;
import com.mygo.bangmall.search.vo.SearchResult;
import com.mygo.common.to.es.SkuEsModel;
import com.mygo.common.utils.R;
import com.mysql.cj.util.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MallSearchServiceImpl implements MallSearchService {
    @Autowired
    private RestHighLevelClient client;
    @Autowired
    ProductFeignService productFeignService;

    @Override
    public SearchResult search(SearchParam param) {
        SearchResult result = null;
        SearchRequest searchRequest =buildSearchRequest(param);
        try {
            SearchResponse response = client.search(searchRequest,BangmallElasticsearchConfig.COMMON_OPTIONS);
            result = buildSearchResult(response,param);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private SearchResult buildSearchResult(SearchResponse response,SearchParam param) {
        SearchResult result = new SearchResult();
        SearchHits hits = response.getHits();



        List<SkuEsModel> esModels = new ArrayList<>();
        if (hits.getHits()!=null && hits.getHits().length>0) {
            for (SearchHit hit : hits.getHits()) {
                String SourceAsString = hit.getSourceAsString();
               SkuEsModel esModel = JSON.parseObject(SourceAsString, SkuEsModel.class);
               if (!StringUtils.isNullOrEmpty(param.getKeyword())){
                   HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                   String string = skuTitle.getFragments()[0].string();
                   esModel.setSkuTitle(string);
               }

               esModels.add(esModel);
            }
        }
        result.setProducts(esModels);

        List<SearchResult.AttrVo> attrVos = new ArrayList<>();
        ParsedNested attr_agg = response.getAggregations().get("attr_agg");
        ParsedLongTerms attr_id_agg = attr_agg.getAggregations().get("attr_id_agg");
        for (Terms.Bucket bucket : attr_id_agg.getBuckets()) {
            SearchResult.AttrVo attrVo = new SearchResult.AttrVo();
            long attrId = bucket.getKeyAsNumber().longValue();
            String attrName = ((ParsedStringTerms)bucket.getAggregations().get("attr_name_agg")).getBuckets().get(0).getKeyAsString();
            List<String> attrValues = ((ParsedStringTerms)bucket.getAggregations().get("attr_value_agg")).getBuckets().stream().map(item->{
                String keyAsString = ((Terms.Bucket) item).getKeyAsString();
                return keyAsString;
            }).collect(Collectors.toList());
            attrVo.setAttrId(attrId);
            attrVo.setAttrName(attrName);
            attrVo.setAttrValue(attrValues);

            attrVos.add(attrVo);

        }
        result.setAttrs(attrVos);

        List<SearchResult.BrandVo> brandVos = new ArrayList<>();
        ParsedLongTerms brand_agg = response.getAggregations().get("brand_agg");
        for (Terms.Bucket bucket : brand_agg.getBuckets()) {
            SearchResult.BrandVo brandVo = new SearchResult.BrandVo();
            long brandId = bucket.getKeyAsNumber().longValue();
            String brandName = ((ParsedStringTerms)bucket.getAggregations().get("brand_name_agg")).getBuckets().get(0).getKeyAsString();
            String brandImg = ((ParsedStringTerms)bucket.getAggregations().get("brand_img_agg")).getBuckets().get(0).getKeyAsString();
            brandVo.setBrandId(brandId);
            brandVo.setBrandName(brandName);
            brandVo.setBrandImg(brandImg);
            brandVos.add(brandVo);
        }
        result.setBrands(brandVos);

        ParsedLongTerms catalog_agg = response.getAggregations().get("catalog_agg");

        List<SearchResult.CatalogVo> catalogVos = new ArrayList<>();
        List<? extends Terms.Bucket> buckets = catalog_agg.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            SearchResult.CatalogVo catalogVo = new SearchResult.CatalogVo();
            String keyAsString = bucket.getKeyAsString();
            catalogVo.setCatalogId(Long.parseLong(keyAsString));

            ParsedStringTerms catalog_name_agg = bucket.getAggregations().get("catalog_name_agg");
            String catalog_name = catalog_name_agg.getBuckets().get(0).getKeyAsString();
            catalogVo.setCatalogName(catalog_name);
            catalogVos.add(catalogVo);
        }
        result.setCatalogs(catalogVos);



        result.setPageNum(param.getPageNum());
        long total = hits.getTotalHits().value;
        result.setTotal(total);
        int totalPages = (int)total%EsConstant.PRODUCT_PAGESIZE == 0?(int)total/EsConstant.PRODUCT_PAGESIZE:((int)total/EsConstant.PRODUCT_PAGESIZE+1);
        result.setTotalPages(totalPages);
        List<Integer> pageNavs = new ArrayList<>();
        for (int i=1;i<totalPages;i++) {
            pageNavs.add(i);
        }
        result.setPageNavs(pageNavs);
        if (param.getAttrs()!=null&&param.getAttrs().size()>0) {
            List<SearchResult.NavVo> collect = param.getAttrs().stream().map(attr -> {
                SearchResult.NavVo navVo = new SearchResult.NavVo();
                String[] s = attr.split("_");
                navVo.setNavValue(s[1]);
                R r = productFeignService.attrInfo(Long.parseLong(s[0]));
                result.getAttrIds().add(Long.parseLong(s[0]));
                if (r.getCode() == 0){
                    AttrResponseVo data = r.getData("attr", new TypeReference<AttrResponseVo>() {});
                    navVo.setNavName(data.getAttrName());
                }else{
                    navVo.setNavName(s[0]);
                }
                String replace = replaceQueryString(param, attr,"attrs");
                navVo.setLink("http://search.bangmall.com/list.html?"+replace);
                return navVo;
            }).collect(Collectors.toList());
            result.setNavs(collect);
        }
        if (param.getBrandId()!=null&&param.getBrandId().size()>0) {
            List<SearchResult.NavVo> navs = result.getNavs();
            SearchResult.NavVo navVo = new SearchResult.NavVo();
            navVo.setNavName("品牌");
            R r = productFeignService.brandsinfo(param.getBrandId());
            if (r.getCode() == 0){
                List<BrandVo> brand = r.getData("brand", new TypeReference<List<BrandVo>>() {});
                StringBuffer buffer = new StringBuffer();
                String replace = "";
                for (BrandVo brandVo : brand) {
                    buffer.append(brandVo.getBrandName()+";");
                    replace = replaceQueryString(param, brandVo.getBrandId()+"","brandId");
                }
                navVo.setNavValue(buffer.toString());
                navVo.setLink("http://search.bangmall.com/list.html?"+replace);
            }
            navs.add(navVo);

        }


        return result;

    }

    private static String replaceQueryString(SearchParam param, String value,String key) {
        String encode = null;
        try {
            encode = URLEncoder.encode(value,"UTF-8");
            encode = encode.replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String replace = param.get_queryString().replace("&"+key+"=" +encode,"");
        return replace;
    }

    private SearchRequest buildSearchRequest(SearchParam param) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if(!StringUtils.isNullOrEmpty(param.getKeyword())){
            boolQuery.must(QueryBuilders.matchQuery("skuTitle",param.getKeyword()));
        }
        if (param.getCatalog3Id() != null) {
            boolQuery.filter(QueryBuilders.termQuery("catalog3Id", param.getCatalog3Id()));
        }
        if (param.getBrandId() != null && param.getBrandId().size() > 0) {
            boolQuery.filter(QueryBuilders.termsQuery("brandId", param.getBrandId()));
        }
        if (param.getAttrs() != null && param.getAttrs().size() > 0) {
            for(String attrStr : param.getAttrs()) {
                BoolQueryBuilder nestedboolQuery = QueryBuilders.boolQuery();
                String[] s = attrStr.split("_");
                String attrId = s[0];
                String[] attrValues = s[1].split(":");
                nestedboolQuery.must(QueryBuilders.termQuery("attrs.attrId",attrId));
                nestedboolQuery.must(QueryBuilders.termsQuery("attrs.attrValue",attrValues));
                NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery("attrs",nestedboolQuery, ScoreMode.None);
                boolQuery.filter(nestedQueryBuilder);
            }


        }
        if (param.getHasStock()!= null) {
            boolQuery.filter(QueryBuilders.termQuery("hasStock", param.getHasStock()==1));
        }


        if (!StringUtils.isNullOrEmpty(param.getSkuPrice())){
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
            String[] s = param.getSkuPrice().split("_");
            if (s.length == 2) {
                rangeQuery.gte(s[0]).lte(s[1]);
            }else if (s.length == 1) {
                if (param.getSkuPrice().startsWith("_")){
                    rangeQuery.lte(s[0]);
                }
                if (param.getSkuPrice().endsWith("_")){
                    rangeQuery.gte(s[0]);
                }

            }
        }

        sourceBuilder.query(boolQuery);

        if (!StringUtils.isNullOrEmpty(param.getSort())){
            String sort = param.getSort();
            String[] s = sort.split("_");
            SortOrder order = s[1].equalsIgnoreCase("asc")? SortOrder.ASC: SortOrder.DESC;
            sourceBuilder.sort(s[0],order);
        }

        if (!StringUtils.isNullOrEmpty(param.getKeyword())){
            HighlightBuilder builder = new HighlightBuilder();
            builder.field("skuTitle");
            builder.preTags("<b style='color:red'>");
            builder.postTags("</b>");
            sourceBuilder.highlighter(builder);
        }

        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg");
        brand_agg.field("brandId").size(50);
        brand_agg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName").size(1));
        brand_agg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(1));
        sourceBuilder.aggregation(brand_agg);

        TermsAggregationBuilder catalog_agg = AggregationBuilders.terms("catalog_agg").field("catalogId").size(20);
        catalog_agg.subAggregation(AggregationBuilders.terms("catalog_name_agg").field("catalogName").size(1));
        sourceBuilder.aggregation(catalog_agg);

        NestedAggregationBuilder attr_agg = AggregationBuilders.nested("attr_agg", "attrs");
        TermsAggregationBuilder attr_id_agg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(1));
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(50));
        attr_agg.subAggregation(attr_id_agg);
        sourceBuilder.aggregation(attr_agg);

        sourceBuilder.from((param.getPageNum()-1)*EsConstant.PRODUCT_PAGESIZE);
        sourceBuilder.size(EsConstant.PRODUCT_PAGESIZE);

        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, sourceBuilder);
        return searchRequest;
    }
}
