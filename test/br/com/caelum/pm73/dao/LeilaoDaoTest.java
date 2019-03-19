package br.com.caelum.pm73.dao;

import static org.junit.Assert.assertEquals;

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
	
	@After
	public void close() {
		this.session.beginTransaction().rollback();
		this.session.close();
	}

}
