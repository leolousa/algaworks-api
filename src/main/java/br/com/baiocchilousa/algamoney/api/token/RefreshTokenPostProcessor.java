package br.com.baiocchilousa.algamoney.api.token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import br.com.baiocchilousa.algamoney.api.config.property.AlgamoneyApiProperty;

/**
 * Classe processadora que remove o refresh_token do retorno da
 * solicitação de Access Token e coloca-o em um cookie HTTP
 * @author leolo
 *
 */
@ControllerAdvice
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken>{

    @Autowired //Injeta a propriedade customizada para o ambiente
    private AlgamoneyApiProperty algamoneyApiProperty;
    
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getMethod().getName().equals("postAccessToken");
    }

    @Override
    public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType,
            MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request, ServerHttpResponse response) {

        HttpServletRequest req = ((ServletServerHttpRequest)request).getServletRequest();
        HttpServletResponse resp = ((ServletServerHttpResponse)response).getServletResponse();
        
        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) body;
        
        //Propriedade refresh_token da requisição
        String refreshToken = body.getRefreshToken().getValue();
        
        adicionarRefreshTokenNoCookie(refreshToken, req, resp);

        removerRefreshTokenDoBody(token);
        
        return body;
    }

    private void removerRefreshTokenDoBody(DefaultOAuth2AccessToken token) {
        token.setRefreshToken(null);
    }

    private void adicionarRefreshTokenNoCookie(String refreshToken, HttpServletRequest req, HttpServletResponse resp) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(algamoneyApiProperty.getSeguranca().isEnableHttps()); 
        refreshTokenCookie.setPath(req.getContextPath() + "/oauth/token");
        refreshTokenCookie.setMaxAge(2592000); //Cookie expira em 30 dias
        resp.addCookie(refreshTokenCookie);
    }


}
