package com.scoup.server.config.security;

import com.scoup.server.config.resolver.ServiceTokenResolver;
import com.scoup.server.config.resolver.UserIdResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final UserIdResolver userIdResolver;
    private final ServiceTokenResolver serviceTokenResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userIdResolver);
        resolvers.add(serviceTokenResolver);
    }
}
