package com.davimc.cursomc;

import com.davimc.cursomc.domain.*;
import com.davimc.cursomc.enums.EstadoPagamento;
import com.davimc.cursomc.enums.TipoCliente;
import com.davimc.cursomc.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Set;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner {
	@Autowired
	private CategoriaRepository categoriaRepository;
	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private EstadoRepository estadoRepository;
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired
	private PagamentoRepository pagamentoRepository;
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Categoria cat1 = new Categoria(null,"Informática");
		Categoria cat2 = new Categoria(null,"Escritório");

		Produto p1 = new Produto(null,"Computador", 2000.00);
		Produto p2 = new Produto(null,"Impressora", 800.00);
		Produto p3 = new Produto(null,"Mouse", 80.00);

		cat1.getProdutos().addAll(Arrays.asList(p1,p2,p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));

		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1,cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));

		categoriaRepository.saveAll(Arrays.asList(cat1,cat2));
		produtoRepository.saveAll(Arrays.asList(p1,p2,p3));

		Estado est1 = new Estado("Minas Gerais");
		Estado est2 = new Estado("São Paulo");

		Cidade c1 = new Cidade("Uberlândia", est1);
		Cidade c2 = new Cidade("São Paulo", est2);
		Cidade c3 = new Cidade("Campinas", est2);

		est1.setCidades(Arrays.asList(c1));
		est2.setCidades(Arrays.asList(c2,c3));

		estadoRepository.saveAll(Arrays.asList(est1,est2));
		cidadeRepository.saveAll(Arrays.asList(c1,c2,c3));

		Cliente cli1 = new Cliente("Maria Silva", "maria@gmail.com","3637891277", TipoCliente.PESSOAFISICA);

		Endereco e1 = new Endereco("Rua Flores", "300","Apto 203", "Jardim", "3865429",cli1, c1);
		Endereco e2 = new Endereco("Avenida Matos", "105","Sala 800", "Centro", "5650321",cli1, c2);

		cli1.getEnderecos().addAll(Arrays.asList(e1,e2));
		cli1.getTelefone().addAll(Set.of("372584333","98259855"));

		clienteRepository.save(cli1);
		enderecoRepository.saveAll(Arrays.asList(e1,e2));

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Pedido ped1 = new Pedido(sdf.parse("30/09/2017 10:32"), cli1,e1);
		Pedido ped2 = new Pedido(sdf.parse("10/10/2017 19:35"), cli1,e2);

		Pagamento pagto1 = new PagamentoCartao(EstadoPagamento.QUITADO,ped1,6);
		Pagamento pagto2 = new PagamentoBoleto(EstadoPagamento.PENDENTE,ped2,sdf.parse("20/10/2017 00:00"), null);

		ped1.setPagamento(pagto1);
		ped2.setPagamento(pagto2);

		pedidoRepository.saveAll(Arrays.asList(ped1,ped2));
		pagamentoRepository.saveAll(Arrays.asList(pagto1,pagto2));

		ItemPedido ip1 = new ItemPedido(ped1,p1,0.00,1,2000.00);
		ItemPedido ip2 = new ItemPedido(ped1,p3,0.00,2,80.00);
		ItemPedido ip3 = new ItemPedido(ped2,p2,100.00,1,800.00);

		ped1.getItems().addAll(Arrays.asList(ip1,ip2));
		ped2.getItems().addAll(Arrays.asList(ip3));

		p1.getItems().addAll(Arrays.asList(ip1));
		p2.getItems().addAll(Arrays.asList(ip3));
		p3.getItems().addAll(Arrays.asList(ip2));

		itemPedidoRepository.saveAll(Arrays.asList(ip1,ip2,ip3));
	}
}
