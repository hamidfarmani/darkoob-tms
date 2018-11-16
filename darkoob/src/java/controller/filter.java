package controller;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "filter", urlPatterns = {"/*"})
public class filter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String sessionUser = (String) req.getSession().getAttribute("username");
        String currentPath = req.getRequestURL().toString();
        HttpSession session = req.getSession(false);
        if (sessionUser != null && !session.isNew()) {
            if (currentPath.contains("index.xhtml")) {
                resp.sendRedirect(req.getContextPath() + "/faces/mainMenu.xhtml");
            } else {
                chain.doFilter(request, response);
            }
        } else {
            if (currentPath.contains("main")) {
                resp.sendRedirect(req.getContextPath() + "/faces/index.xhtml");
            } else {
                chain.doFilter(request, response);
            }
        }

    }

    public void destroy() {

    }
}
