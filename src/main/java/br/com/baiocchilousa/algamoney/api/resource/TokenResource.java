package br.com.baiocchilousa.algamoney.api.resource;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.baiocchilousa.algamoney.api.config.property.AlgamoneyApiProperty;

/**
 * Classe para implementar o Logout manipulando
 * o Access Token e o Rrfresh Token 
 * @author leolo
 *
 */
@RestController
@RequestMapping("/tokens")
public class TokenResource {
    
    @Autowired //Injeta a propriedade customizada para o ambiente
    private AlgamoneyApiProperty algamoneyApiProperty;

    //Revoga o cookie com o refresh token (remove o seu valor)
    @DeleteMapping("/revoke")
    public void revoke(HttpServletRequest req, HttpServletResponse resp) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(algamoneyApiProperty.getSeguranca().isEnableHttps());
        cookie.setPath(req.getContextPath() + "/oauth/token");
        cookie.setMaxAge(0);
        
        resp.addCookie(cookie);
        resp.setStatus(HttpStatus.NO_CONTENT.value());
    }
}
