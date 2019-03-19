package br.com.caelum.pm73.dao;

import static org.junit.Assert.assertEquals;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;

import br.com.caelum.pm73.dominio.Usuario;

public class UsuarioDaoTest {
	
	@Test
	public void deveEncontrarPeloNomeEemailMockado() {
		Session session = new CriadorDeSessao().getSession();
		UsuarioDao usuarioDao = new UsuarioDao(session);
		
		Usuario novoUsuario = new Usuario("Joao da Silva", "joao@dasilva.com.br");
		usuarioDao.salvar(novoUsuario);
		
		Usuario usuario = usuarioDao.porNomeEEmail("Joao da Silva", "joao@dasilva.com.br");
		
		assertEquals("Joao da Silva", usuario.getNome());
		assertEquals("joao@dasilva.com.br", usuario.getEmail());
		
		session.close();
	}
	
	@Test
	public void deveRetornarNuloSeNaoEncontrarUsuario() {
		Session session = new CriadorDeSessao().getSession();
		UsuarioDao usuarioDao = new UsuarioDao(session);
		
		Usuario usuario = usuarioDao.porNomeEEmail("Francisco de Assis", "chico@gmail.com");
		
		Assert.assertNull(usuario);
	}
	
}
