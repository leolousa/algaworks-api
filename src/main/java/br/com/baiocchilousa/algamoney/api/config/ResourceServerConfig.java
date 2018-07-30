package br.com.baiocchilousa.algamoney.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
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
@Configuration
@EnableWebSecurity
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true) //Hablita a segurança nos métodos
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    
    @Autowired
    private UserDetailsService userDetailsService;
    
    //Método para criar um usuário em memória para testes
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        
        //Usuário na memória para testes
        /*auth.inMemoryAuthentication()
            //.passwordEncoder(NoOpPasswordEncoder.getInstance())
            .withUser("admin").password("admin").roles("ROLE");*/
        
        //Usuário em banco de dados
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        
    }
    

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

    //Encoda a senha com BCrypt
    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}