package br.com.baiocchilousa.algamoney.api.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Classe de Metamodel do JPA para uso com Criteria
 * @author leolo
 *
 */
@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Pessoa.class)
public abstract class Pessoa_ {

    public static volatile SingularAttribute<Pessoa, Long> codigo;
    public static volatile SingularAttribute<Pessoa, Boolean> ativo;
    public static volatile SingularAttribute<Pessoa, Endereco> endereco;
    public static volatile SingularAttribute<Pessoa, String> nome;

}
