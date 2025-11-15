package br.com.ldr.devshe.services;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.ldr.devshe.domain.Perfil;
import br.com.ldr.devshe.domain.Permissao;
import br.com.ldr.devshe.domain.Usuario;
import br.com.ldr.devshe.repositories.PerfilRepository;
import br.com.ldr.devshe.repositories.PermissaoRepository;
import br.com.ldr.devshe.repositories.UsuarioRepository;

@SpringBootTest
class SecurityServiceTest {

    @Autowired
    private SecurityService service;

    @Autowired
    private PermissaoRepository permissaoRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Test
    void testInit() throws Throwable {
        assertNotNull(service);
        InputStream input = getClass().getClassLoader().getResourceAsStream("roles.properties");
        assertNotNull(input, "Arquivo roles.properties não foi encontrado");
        Properties props = new Properties();
        props.load(input);
        assertFalse(props.isEmpty(), "Arquivo roles.properties está vazio");
        for (Object key : props.keySet()) {
            Permissao permissao = permissaoRepository.findByAuthority(key.toString());
            assertNotNull(permissao, "Permissão não encontrada: " + key.toString());
        }

        Perfil perfilAdmin = perfilRepository.findByNome("Administrador");
        assertNotNull(perfilAdmin, "Perfil administrador não foi criado");
        List<Permissao> permissoes = permissaoRepository.findAll();
        assertNotNull(perfilAdmin.getPermissoes(), "Perfil não tem permissões");
        for (Permissao permissao : permissoes) {
            assertTrue(perfilAdmin.getPermissoes().contains(permissao), "Permissão não está no perfil: " + permissao.getAuthority());
        }

        Usuario usuarioAdmin = usuarioRepository.findByEmail("admin@email.com.br");
        assertNotNull(usuarioAdmin, "Usuário admin não foi criado");
        assertNotNull(usuarioAdmin.getHashSenha(), "Não foi definida a senha padrão para o usuário admin");
        assertEquals(perfilAdmin, usuarioAdmin.getPerfil(), "Não foi definido o perfil administrativo para o usuário");

    }

}
