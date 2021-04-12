 package ch.hearc.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


//////////////
//  UPDATE  //
//  CHAP_06 //
//////////////
@Configuration
@EnableWebSecurity
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {
	
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private final String USERS_QUERY = "select username, password, true as enabled from user where username=?";
	private final String ROLES_QUERY = "select u.username, r.role from user u inner join user_role ur on (u.id = ur.user_id) inner join role r on (ur.role_id=r.id) where u.username=?";


	/**
	 * Configuration de l'authentification par JDBC
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//JDBC Authentication
		auth.jdbcAuthentication()
		.usersByUsernameQuery(USERS_QUERY)
		.authoritiesByUsernameQuery(ROLES_QUERY)
		.dataSource(dataSource)
		.passwordEncoder(bCryptPasswordEncoder);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http.authorizeRequests()
		.antMatchers("/", "/home").permitAll()
		.and()
		.formLogin().loginPage("/login").permitAll()
		.and()
		.formLogin().loginPage("/login").failureUrl("/login?error=true").defaultSuccessUrl("/").usernameParameter("username").passwordParameter("password")
		.and()
		.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/")
		.and()
		.rememberMe();
	}
	
}
