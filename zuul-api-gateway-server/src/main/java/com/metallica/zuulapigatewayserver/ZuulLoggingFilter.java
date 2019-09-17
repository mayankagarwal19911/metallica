package com.metallica.zuulapigatewayserver;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public
class ZuulLoggingFilter extends ZuulFilter {

    private static Logger logger = LoggerFactory.getLogger ( ZuulLoggingFilter.class );
    @Override
    public
    String filterType ( ) {  // request should get filter before it gets executed/ after it gets executed or only to request that has caused an exception
        return "pre";
    }

    @Override
    public
    int filterOrder ( ) {
        return 0;
    }

    @Override
    public
    boolean shouldFilter ( ) {
        return true;
    }

    @Override
    public
    Object run ( ) throws ZuulException { // logic for logging

        HttpServletRequest request = RequestContext.getCurrentContext ( ).getRequest ();
        logger.info ( "Request is -> {}, request uri is -> {} ", request, request.getRequestURI () );


        return null;
    }
}
