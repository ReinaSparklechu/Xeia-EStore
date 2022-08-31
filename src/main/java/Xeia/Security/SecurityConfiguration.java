package Xeia.Security;

import Xeia.Customer.Customer;
import Xeia.Data.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
    //TODO: configure web security

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MD5Encoder();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            System.out.println("User Details service called");
            Customer cust = customerRepository.findCustomer(username);
            return cust;
        };
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**","/admin").hasAuthority("ADMIN")
                .antMatchers("/login/**", "/**", "/h2-console/**","/store/**").permitAll()
                .and()
                .formLogin().loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/store").permitAll().successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

                        System.out.println(authentication.getDetails());
                        System.out.println(authentication.getCredentials());
                        System.out.println(authentication.getName());
                        System.out.println(authentication.getPrincipal());
                        request.getSession().setAttribute("custName", authentication.getName());
                        response.sendRedirect("/login/process");
                    }
                }).failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                        response.sendRedirect("login/failure");
                    }
                })
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/").invalidateHttpSession(true).deleteCookies("JSESSIONID")
                .and()
                .csrf().disable().headers().frameOptions().disable()
                ;
                //.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username,passwordHash,enabled from customer where username = ?")
                .authoritiesByUsernameQuery("select c.username, r.role_name from Role r , Role_Customer rc, Customer c where c.username = ? and c.userId = rc.CustomerId and r.Role_id = rc.Role_id");

    }

}
