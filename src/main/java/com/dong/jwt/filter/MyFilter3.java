package com.dong.jwt.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter3 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request; // 실제로 request가 오는 정보 (이것을 그냥 javax HttpServletRequest 가 부모라서 형변환만 해준것)
        HttpServletResponse res = (HttpServletResponse) response;

        String tokenName = "cos";

        System.out.println("필터3");
        // 토큰을 만들었다고 가정 (토큰이름 : COS)
        // 이제 실제로 토큰을 만들어야하는데 우선 ID,PW 가 정상적으로 들어와서 로그인이 완료되면 토큰을 만들어주고 그걸 응답을 해줌
        // 요청할 때마다 헤더에 Authorization 에 Value 값으로 토큰을 가져올 것
        // 그 때 해당 토큰에 대한 검증 과정을 거치고 검증이 되면 doFilter, 안되면 에러페이지
        if(req.getMethod().equals("POST")){
            System.out.println("POST 요청됨");
            String headerAuth = req.getHeader("Authorization"); // 요청의 헤더부분 내 Authorization 이라는 곳의 데이터를 받아올 수 있음
            if (headerAuth.equals(tokenName)){
                chain.doFilter(request, response);
            }
            else{
                PrintWriter out = res.getWriter();
                out.println("인증 안됨"); // 이렇게 doFilter 를 하지 않으면 필터 이후에 컨트롤러 단으로 들어가는데 그 전에 종료되버림 (즉, 컨트롤러로조차 가지 못함)
            }
        }
        else
            chain.doFilter(request, response);


    }
}
