package br.com.baiocchilousa.algamoney.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

/**
 * Classe de configuração de segurança com OAUTH2 da API
 * 
 * Oauth2 - fluxo Password Flow:
 * Autenticação e autorização utilizando o framework OAuth2 
 * no fluxo Password Flow
 * 
 * @author leolo
 *
 */
@Profile("oauth-security")
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/categorias").permitAll() //Libera a url para qualquer um que usar a API
                .anyRequest().authenticated() //Qualquer request deverá ser autenticado
                .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and() //Desabilita controle de sessão
            .csrf().disable(); //Desabilita o Cross-site request forgery
    }
    
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.stateless(true);
    }
    
    //Handler para conseguir fazer a segurança dos métodos com o OAuth2
    @Bean
    public MethodSecurityExpressionHandler createExpressionHandler() {
        return new OAuth2MethodSecurityExpressionHandler();
    }

}
