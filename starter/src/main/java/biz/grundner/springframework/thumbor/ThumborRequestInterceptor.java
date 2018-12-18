package biz.grundner.springframework.thumbor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Stephan Grundner
 */
public class ThumborRequestInterceptor implements HandlerInterceptor {

    public static final int ORDINAL = 1000;

    @Autowired
    private ThumborService thumborService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String thumbor = request.getParameter("thumbor");
        if (thumbor != null) {
            ThumborRunner runner = thumborService.getDefaultRunner();
            runner.process(request, response);

            return false;
        }

        return true;
    }
}
