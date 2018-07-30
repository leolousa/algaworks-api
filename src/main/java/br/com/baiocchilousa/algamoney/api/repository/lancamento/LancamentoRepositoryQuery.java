package br.com.baiocchilousa.algamoney.api.repository.lancamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.baiocchilousa.algamoney.api.model.Lancamento;
import br.com.baiocchilousa.algamoney.api.repository.filter.LancamentoFilter;
import br.com.baiocchilousa.algamoney.api.repository.projection.ResumoLancamento;

/**
 * Interface para implementar as queries customisadas da aplicação
 * @author leolo
 *
 */
public interface LancamentoRepositoryQuery {
    
    public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
    public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable);
}
