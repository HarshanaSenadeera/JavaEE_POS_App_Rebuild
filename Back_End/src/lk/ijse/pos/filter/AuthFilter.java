package lk.ijse.pos.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/*@WebFilter(urlPatterns = "/*")*/
public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        String pos = servletRequest.getParameter("POS");
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String servletPath = req.getServletPath();

        if(servletPath.equals("/pages/customer")){
            filterChain.doFilter(servletRequest,servletResponse);
        } else if (servletPath.equals("/pages/item")) {
            filterChain.doFilter(servletRequest,servletResponse);
        }else {
            System.out.println("wade awl");
        }


    }

    @Override
    public void destroy() {

    }
}
