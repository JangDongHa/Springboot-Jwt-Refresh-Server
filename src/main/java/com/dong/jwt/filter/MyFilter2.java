package com.dong.jwt.filter;

import javax.servlet.*;
import java.io.IOException;

public class MyFilter2 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("필터2");
        chain.doFilter(request, response); // doFilter 안하면 서버 꺼짐 (필터가 진행되고 서버가 request 에 따른 처리를 진행하므로)
    }
}
