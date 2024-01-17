/*
 * Copyright (c) 2023-2024 Tracehub
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to read
 * the Software only. Permissions is hereby NOT GRANTED to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package git.tracehub.pmo.security;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configurations.
 *
 * @since 0.0.0
 */
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Filter.
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @checkstyle NonStaticMethodCheck (30 lines)
     */
    @Bean
    @SneakyThrows
    public SecurityFilterChain chain(final HttpSecurity http) {
        return http.cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .sessionManagement(
                configurer ->
                    configurer.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS
                    )
            ).authorizeHttpRequests(
                auth -> auth
                    .requestMatchers("/login").permitAll()
                    .anyRequest().authenticated()
            ).exceptionHandling(
                configurer -> configurer
                    .accessDeniedHandler(
                        (request, response, ex) -> {
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.getWriter().write("Access denied");
                        }
                    )
            ).oauth2Login(Customizer.withDefaults())
            .build();
    }

}
