package biz.grundner.springframework.thumbor;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Stephan Grundner
 */
public class ThumborRequestInterceptor implements HandlerInterceptor {

    public static final int ORDINAL = 1000;

    @Autowired
    private ThumborRunner thumborRunner;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUrl = request.getRequestURL().toString();

        String thumbor = request.getParameter("thumbor");
        if (thumbor != null) {
            String size = request.getParameter("size");
            String thumborUrl = String.format("http://localhost:%d/unsafe/%s/%s",
                    thumborRunner.getPort(),
                    size,
                    requestUrl);

            UrlResource resource = new UrlResource(thumborUrl);
            try (InputStream inputStream = resource.getInputStream();
                 OutputStream outputStream = response.getOutputStream()) {

                IOUtils.copy(inputStream, outputStream);
                response.flushBuffer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return false;
        }

        return true;
    }
}
