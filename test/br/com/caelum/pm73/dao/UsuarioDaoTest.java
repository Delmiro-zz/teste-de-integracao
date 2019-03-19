package br.com.caelum.pm73.dao;

import static org.junit.Assert.assertEquals;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import br.com.caelum.pm73.dominio.Usuario;

public class UsuarioDaoTest {
	
	private UsuarioDao usuarioDao;
	private Session session;
	
	@Before
	public void setUp() {
		this.session = new CriadorDeSessao().getSession();
		this.usuarioDao = new UsuarioDao(session);
		
		this.session.beginTransaction();
	}
	
	@Test
	public void deveEncontrarPeloNomeEemailMockado() {
		
		Usuario novoUsuario = new Usuario("Joao da Silva", "joao@dasilva.com.br");
		usuarioDao.salvar(novoUsuario);
		
		Usuario usuario = usuarioDao.porNomeEEmail("Joao da Silva", "joao@dasilva.com.br");
		
		assertEquals("Joao da Silva", usuario.getNome());
		assertEquals("joao@dasilva.com.br", usuario.getEmail());
	}
	
	@Test
	public void deveRetornarNuloSeNaoEncontrarUsuario() {
		Usuario usuario = usuarioDao.porNomeEEmail("Francisco de Assis", "chico@gmail.com");
		
		Assert.assertNull(usuario);
	}
	
	@After
	public void close() {
		this.session.beginTransaction().rollback();
		this.session.close();
	}
}
