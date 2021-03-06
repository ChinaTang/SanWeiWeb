package filter;


import databases.DataBaseTools;
import reqhead.CheckReqHead;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class CheckFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        servletResponse.setCharacterEncoding("UTF-8");

        if(CheckReqHead.CheckPublicParam(servletRequest)
                && CheckReqHead.CheckAccessToken((HttpServletRequest)servletRequest)){
            filterChain.doFilter(servletRequest, servletResponse);
        }else{

        }
    }

    @Override
    public void destroy() {

    }
}
