package br.com.ldr.devshe.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.ldr.devshe.domain.AuthLog;
import br.com.ldr.devshe.domain.Perfil;
import br.com.ldr.devshe.domain.Permissao;
import br.com.ldr.devshe.domain.Usuario;
import br.com.ldr.devshe.dto.Autorizacao;
import br.com.ldr.devshe.dto.Credenciais;
import br.com.ldr.devshe.exceptions.ServiceError;
import br.com.ldr.devshe.repositories.AuthLogRepository;
import br.com.ldr.devshe.repositories.PerfilRepository;
import br.com.ldr.devshe.repositories.PermissaoRepository;
import br.com.ldr.devshe.repositories.UsuarioRepository;
import br.com.ldr.devshe.security.JWTAuthentication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;

@Service
public class SecurityService {
	
	@Autowired
	private PermissaoRepository permissaoRepository;
	
	@Autowired
	private PerfilRepository perfilRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private ConfigService configService;
	
	@Autowired
	private AuthLogRepository logRepository;
	
	
	private static final Logger logger = LoggerFactory.getLogger("SecurityService");

	private SecretKey key;
	
	
	public JWTAuthentication parseToken(String token) {

		try {
			Claims body = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
			String email = body.getSubject();
			Usuario usuario = usuarioRepository.findByEmail(email);
			if (usuario != null && usuario.isAtivo()) {
				return new JWTAuthentication(usuario);
			}
		} catch (Throwable t) {
			// erro parseando token
			logger.error("Erro validando token. Token inválido.", t);
		}
		
		return null;
	}
	
	
	public Autorizacao autenticar(Credenciais credenciais) {
		if (credenciais != null && credenciais.getUsername() != null && credenciais.getPassword() != null) {
			Usuario usuario = usuarioRepository.findByEmail(credenciais.getUsername());
			if (usuario != null && usuario.isAtivo() &&
				this.checkSenha(credenciais.getPassword(), usuario.getHashSenha())) {
				Date agora = new Date();
				Autorizacao resultado = new Autorizacao();
				resultado.setDateCreated(agora.getTime());
				resultado.setName(usuario.getNome());
				resultado.setToken(this.criarToken(usuario, agora.getTime()));
				resultado.setTimeToLive(Long.parseLong(this.configService.info("jwt.ttl")));
				resultado.setUsername(usuario.getEmail());
				resultado.setRoles(new ArrayList<String>());
				for (Permissao perm : usuario.getPerfil().getPermissoes()) {
					resultado.getRoles().add(perm.getAuthority());
				}
				
				AuthLog log = new AuthLog();
				log.setUsuario(usuario);
				log.setMomento(agora);
				this.logRepository.save(log);
				
				return resultado;
			}
		}
		return null;
	}
	
	private String criarToken(Usuario usuario, long agora) {
		Claims claims = Jwts.claims().setSubject(usuario.getEmail());
		long timeToLive = Long.parseLong(configService.info("jwt.ttl"));
		return Jwts.builder()
				  .setClaims(claims)
				  .setExpiration(new Date(agora + timeToLive))
				  .signWith(key, SignatureAlgorithm.HS512)
				  .compact();
	}
	
	private boolean checkSenha(String raw, String hash) {
		return encoder.matches(raw, hash);
	}
	
	private String hashSenha(String raw) {
		return this.encoder.encode(raw);
	}
	
	@PostConstruct
	public void init() {
		key = Keys.hmacShaKeyFor(configService.info("jwt.secret").getBytes(StandardCharsets.UTF_8));

		try (InputStream input = getClass().getClassLoader().getResourceAsStream("roles.properties")) {
			Properties props = new Properties();
			props.load(input);
			for (Object key : props.keySet()) {
				if (permissaoRepository.findByAuthority(key.toString()) == null) {
					Permissao permissao = new Permissao();
					permissao.setAuthority(key.toString());
					permissao.setNome(props.getProperty(key.toString()));
					permissaoRepository.save(permissao);
				}
			}
		} catch (IOException ex) {
			throw new ServiceError("Erro carregando permissões padrão", ex);
		}
		
		Perfil perfil = perfilRepository.findByNome("Administrador");
		if (perfil == null) {
			perfil = new Perfil();
			perfil.setNome("Administrador");
			perfil.setPermissoes(new HashSet<Permissao>());
			perfil.setAtivo(true);
			List<Permissao> permissoes = permissaoRepository.findAll();
			perfil.getPermissoes().addAll(permissoes);
			perfilRepository.save(perfil);
		}
		
		Usuario admin = usuarioRepository.findByEmail("admin@email.com.br");
		if (admin == null) {
			admin = new Usuario();
			admin.setNome("Administrador");
			admin.setEmail("admin@email.com.br");
			admin.setAtivo(true);
			admin.setPerfil(perfil);
			admin.setHashSenha(this.hashSenha("admin1234"));
			usuarioRepository.save(admin);
		}
		
	}

}
