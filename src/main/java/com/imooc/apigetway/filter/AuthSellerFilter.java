package com.imooc.apigetway.filter;

import com.imooc.apigetway.constant.RedisConstant;
import com.imooc.apigetway.utils.CookieUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * @ClassName: TokenFilter
 * @Description: 权限拦截(区分买家卖家)
 * @Author: ZhangChen
 * @Date: 2019/6/29 10:55
 **/
@Slf4j
@Component
public class AuthSellerFilter extends ZuulFilter {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    //代表是pre,还是post
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return PRE_DECORATION_FILTER_ORDER-1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        if("/order/order/finish".equals(request.getRequestURI())){
            return true;
        }
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        // /order/finish 只能卖家访问(cookie里有token,并且对应的redis中有值)
        Cookie cookie = CookieUtil.get(request, "token");
        if(cookie == null
                || StringUtils.isEmpty(cookie.getValue())
                || StringUtils.isEmpty(stringRedisTemplate.opsForValue().get(String.format(RedisConstant.Token_TEMPLATE,cookie.getValue())))){
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }
}
