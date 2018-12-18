package biz.grundner.springframework.thumbor;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Stephan Grundner
 */
@ConfigurationProperties(prefix = "thumbor")
public class ThumborProperties {

    private String secureKey;

    public String getSecureKey() {
        return secureKey;
    }

    public void setSecureKey(String secureKey) {
        this.secureKey = secureKey;
    }
}
