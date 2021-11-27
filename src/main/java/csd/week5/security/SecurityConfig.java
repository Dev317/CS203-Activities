package csd.week5.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(
  prePostEnabled = true,
  securedEnabled = true,
  jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    private UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userSvc){
        this.userDetailsService = userSvc;
    }
    
    /** 
     * Attach the user details and password encoder.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
        throws Exception {
        auth
        .userDetailsService(userDetailsService)
        .passwordEncoder(encoder());
    }

    /**
     * TODO: Activity 2a - Authentication
     * Add code to secure requests to Reviews
     * In particular, only authenticated users would be able to create/update/delete Reviews
     * Hint: Add antMatchers rules
     * 
     * 
     * 
     * 
     * TODO: Activity 2b - Authorization
     * Add roles to specify permissions for each enpoint
     * User role: can add review.
     * Admin role: can add/delete/update books/reviews, and add/list users
     *  
     * Note: '*' matches zero or more characters, e.g., /books/* matches /books/20
             '**' matches zero or more 'directories' in a path, e.g., /books/** matches /books/1/reviews 
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .httpBasic()
            .and() //  "and()"" method allows us to continue configuring the parent
        .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/books", "/books/**").permitAll() // Anyone can view books and reviews
            .antMatchers(HttpMethod.POST, "/books").authenticated()
            .antMatchers(HttpMethod.PUT, "/books/*").authenticated()
            .antMatchers(HttpMethod.DELETE, "/books/*").authenticated()
            
            
            // .antMatchers(HttpMethod.POST, "/books").hasRole("ADMIN")
            // .antMatchers(HttpMethod.PUT, "/books/*").hasRole("ADMIN")
            // .antMatchers(HttpMethod.DELETE, "/books/*").hasRole("ADMIN")
            
            // your code here
            .antMatchers(HttpMethod.POST, "/books/**/reviews").authenticated()
            .antMatchers(HttpMethod.PUT, "/books/**/reviews/**").authenticated()
            .antMatchers(HttpMethod.DELETE, "/books/**/reviews/**").authenticated()

            
            // .antMatchers(HttpMethod.POST, "/books/**/reviews").hasAnyRole("ADMIN", "USER")
            // .antMatchers(HttpMethod.PUT, "/books/**/reviews/**").hasRole("ADMIN")
            // .antMatchers(HttpMethod.DELETE, "/books/**/reviews/**").hasRole("ADMIN")
            
            .antMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
            .antMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")

            .and()
        .csrf().disable() // CSRF protection is needed only for browser based attacks
        .formLogin().disable()
        .headers().disable(); // Disable the security headers, as we do not return HTML in our service
    }

    /**
     * @Bean annotation is used to declare a PasswordEncoder bean in the Spring application context. 
     * Any calls to encoder() will then be intercepted to return the bean instance.
     */
    @Bean
    public BCryptPasswordEncoder encoder() {
        // auto-generate a random salt internally
        return new BCryptPasswordEncoder();
    }
}
 