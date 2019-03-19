package br.com.caelum.pm73.dao;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import br.com.caelum.pm73.dominio.Leilao;
import br.com.caelum.pm73.dominio.Usuario;

public class LeilaoDaoTest {
	
	private LeilaoDao leilaoDao;
	private Session session;
	private UsuarioDao usuarioDao;
	
	@Before
	public void setUp() {
		this.session = new CriadorDeSessao().getSession();
		this.leilaoDao = new LeilaoDao(session);
		this.usuarioDao = new UsuarioDao(session);
		
		this.session.beginTransaction();
	}
	
	@Test
	public void deveContarLeiloesNaoEncerrrados(){
		Usuario usuarioTeste = new Usuario("Usuario", "usuario@fake.com.br");
		Leilao ativo = new Leilao("Geladeira", 1500.0, usuarioTeste, false);
		Leilao encerrado = new Leilao("PS4", 2500.0, usuarioTeste, false);
		encerrado.encerra();
		
		usuarioDao.salvar(usuarioTeste);
		leilaoDao.salvar(ativo);
		leilaoDao.salvar(encerrado);
		
		long total = leilaoDao.total();
		
		assertEquals(1l, total);
	}
	
	@Test
	public void deveTrazerLeiloesNaoEncerradosNoPeriodo() {
		Calendar comecoIntervalo = Calendar.getInstance();
		Calendar fimIntervalo = Calendar.getInstance();
		Calendar dataPrimeiroLeilao = Calendar.getInstance();
		Calendar dataSegundoLeilao = Calendar.getInstance();
		
		comecoIntervalo.add(Calendar.DAY_OF_MONTH, -10);
		dataPrimeiroLeilao.add(Calendar.DAY_OF_MONTH, -2);
		dataSegundoLeilao.add(Calendar.DAY_OF_MONTH, -20);
		
		System.out.println(comecoIntervalo);
		System.out.println(dataPrimeiroLeilao);
		System.out.println(dataSegundoLeilao);
		
		Usuario usuarioX = new Usuario("Usuario X", "usuarioX@gmail.com.br");
		
		Leilao primeiroLeilao = new Leilao("PS4", 2500.0, usuarioX, false);
		primeiroLeilao.setDataAbertura(dataPrimeiroLeilao);
		
		Leilao segundoLeilao = new Leilao("XBox", 100.0, usuarioX, false);
		segundoLeilao.setDataAbertura(dataSegundoLeilao);
		
		usuarioDao.salvar(usuarioX);
		leilaoDao.salvar(primeiroLeilao);
		leilaoDao.salvar(segundoLeilao);
		
		List<Leilao> leiloes = leilaoDao.porPeriodo(comecoIntervalo, fimIntervalo);
		assertEquals(1, leiloes.size());
		assertEquals("PS2", leiloes.get(0).getNome());
	}
	
	@After
	public void close() {
		this.session.beginTransaction().rollback();
		this.session.close();
	}

}
