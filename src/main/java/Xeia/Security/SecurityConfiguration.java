package Xeia.Security;

import Xeia.Customer.Customer;
import Xeia.Data.CustomerRepository;
import org.conscrypt.OpenSSLMessageDigestJDK;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    //TODO: configure web security
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MD5Encoder();
    }

    @Bean
    public UserDetailsService userDetailsService(CustomerRepository customerRepository) {

        return username -> {
            System.out.println("func called");
            Customer cust = customerRepository.findCustomer(username);
            return cust;
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        return security.authorizeRequests()
                .antMatchers("/admin").access("hasRole('ADMIN')")
                .antMatchers("/", "/**", "/h2-console/**").access("permitAll()")
                .and()
                .formLogin().loginPage("/login").defaultSuccessUrl("/store", true).failureForwardUrl("/login")
                .and()
                .csrf().disable().headers().frameOptions().disable().and()
                .build();
    }
}
