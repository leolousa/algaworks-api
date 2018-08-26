package br.com.baiocchilousa.algamoney.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.baiocchilousa.algamoney.api.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>{

    public Page<Pessoa> findByNomeContainingOrderByNome(String nome, Pageable pageable); 
}
