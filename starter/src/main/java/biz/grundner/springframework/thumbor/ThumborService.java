package biz.grundner.springframework.thumbor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author Stephan Grundner
 */
@Service
public class ThumborService {

    @Autowired
    private ApplicationContext applicationContext;

    public ThumborRunner getDefaultRunner() {
        return applicationContext.getBean(ThumborRunner.class);
    }
}
