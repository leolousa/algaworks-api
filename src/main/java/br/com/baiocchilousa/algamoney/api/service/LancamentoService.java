package br.com.baiocchilousa.algamoney.api.service;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.baiocchilousa.algamoney.api.model.Lancamento;
import br.com.baiocchilousa.algamoney.api.model.Pessoa;
import br.com.baiocchilousa.algamoney.api.repository.LancamentoRepository;
import br.com.baiocchilousa.algamoney.api.repository.PessoaRepository;
import br.com.baiocchilousa.algamoney.api.service.exception.PessoaInexistenteOuInativaException;

/**
 * Classe de serviços da entidade Lancamento para
 * separar as regras de negócio da classe de controle
 * @author leolo
 *
 */
@Service
public class LancamentoService {
    
    @Autowired
    private PessoaRepository pessoaRepository;
    
    @Autowired
    private LancamentoRepository lancamentoRepository;

    public Lancamento salvar(@Valid Lancamento lancamento) {
        Pessoa pessoa = pessoaRepository.getOne(lancamento.getPessoa().getCodigo());
        if(pessoa == null || pessoa.isInativo()) {
            throw new PessoaInexistenteOuInativaException();
        }
        return lancamentoRepository.save(lancamento);
    }
    
    //Atualiza lançamento
    public Lancamento atualizar(Long codigo, Lancamento lancamento) {
        Lancamento lancamentoSalvo = buscarLancamentoExistente(codigo);
        if(!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) {
            validarPessoa(lancamento);
        }
        // Copia as propriedades de um objeto em outro menos a propriedade codigo 
        BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");
        
        return lancamentoRepository.save(lancamentoSalvo);
    }

    //Valida a pessoa
    private void validarPessoa(Lancamento lancamento) {
        Pessoa pessoa = null;
        if(lancamento.getPessoa().getCodigo() != null) {
            pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
        }
        
        if(pessoa == null || pessoa.isInativo()) {
            throw new PessoaInexistenteOuInativaException();
        }
    }

    //Busca lancamento existente
    private Lancamento buscarLancamentoExistente(Long codigo) {
        Lancamento lancamentoSalvo = lancamentoRepository.findOne(codigo);
        if(lancamentoSalvo == null) {
            throw new IllegalArgumentException();
        }
        
        return lancamentoSalvo;
    }

}
