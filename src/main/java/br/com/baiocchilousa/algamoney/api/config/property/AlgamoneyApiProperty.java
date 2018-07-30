package br.com.baiocchilousa.algamoney.api.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Classe de configuração das propriedades da aplicação
 * os perfis de ambientes prod e dev
 * @author leolo
 *
 */
@ConfigurationProperties("algamoney")
public class AlgamoneyApiProperty {

    private String origemPermitida = "http://localhost:8000";
    
    private final Seguranca seguranca = new Seguranca();
    
    public Seguranca getSeguranca() {
        return seguranca;
    }  
    
    public String getOrigemPermitida() {
        return origemPermitida;
    }

    public void setOrigemPermitida(String origemPermitida) {
        this.origemPermitida = origemPermitida;
    }

    //Classe estática para configurações por grupos
    public static class Seguranca {
        
        private boolean enableHttps;

        public boolean isEnableHttps() {
            return enableHttps;
        }

        public void setEnableHttps(boolean enableHttps) {
            this.enableHttps = enableHttps;
        }
        
        
    }

}
