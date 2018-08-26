package br.com.baiocchilousa.algamoney.api.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import br.com.baiocchilousa.algamoney.api.config.token.CustomTokenEnhancer;

/**
 * Classe que configura o OAuth2 e que representa a autorização da API com JWT
 * utilizando o perfil "oauth-security"
 * @author leolo
 *
 */
@Profile("oauth-security")
@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    //Configura os clientes da API apenas na memória (inMemory)
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
            .withClient("angular")
            .secret("$2a$10$DixmFgB8itcp4RKJB3O2uuF3Xdo2CgkLdOTqKnG80Ovk7T7lqeP7W") // @ngul@r0
            .scopes("read", "write")
            .authorizedGrantTypes("password", "refresh_token")
            .accessTokenValiditySeconds(1800)//30 min
            .refreshTokenValiditySeconds(3600 * 24)// 1 dia
         .and()
             .withClient("mobile")
             .secret("$2a$10$D970IBYqed2GjlM.tiphP.u.U.GAd/s6Nbvw9Xv8aoe2o8fLV6TNq") // m0b1l30
             .scopes("read")
             .authorizedGrantTypes("password", "refresh_token")
             .accessTokenValiditySeconds(1800)//30 min
             .refreshTokenValiditySeconds(3600 * 24);// 1 dia
    }
    
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        
        //Objeto para receber os dados a serem colocados no Token
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
        
        endpoints
            .tokenStore(tokenStore())
            .tokenEnhancer(tokenEnhancerChain)
            .accessTokenConverter(accessTokenConverter())
            .reuseRefreshTokens(false)
            .userDetailsService(userDetailsService)
            .authenticationManager(authenticationManager);
    }
    

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey("algaworks");
        return accessTokenConverter;
    }

    @Bean
    public TokenStore tokenStore() {
      return new JwtTokenStore(accessTokenConverter());  
    }
    
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }
}
