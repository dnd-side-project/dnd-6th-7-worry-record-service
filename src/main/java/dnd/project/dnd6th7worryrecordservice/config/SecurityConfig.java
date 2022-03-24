//package dnd.project.dnd6th7worryrecordservice.config;
//
//import com.google.api.client.util.Value;
//import dnd.project.dnd6th7worryrecordservice.jwt.JwtUtil;
//import dnd.project.dnd6th7worryrecordservice.security.filter.JwtAuthenticationFilter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@EnableWebSecurity
//@RequiredArgsConstructor
//@Configuration
//public class SecurityConfig
//        extends WebSecurityConfigurerAdapter
//{
//
//    private final JwtUtil jwtUtil;
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        // swagger-ui.html의 경우 인증된 사용자가 아니어도 접근가능하도록 설정(dev환경에 대해서만 swagger 설정을 하였기 때문에 인증된 사용자가 아니어도 됨)
//        web.ignoring().antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**");
//    }
//
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http
//                .httpBasic().disable()
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/ping").permitAll()
//                .antMatchers("/auth/**").permitAll() // /auth/**에 대한 접근을 인증 절차 없이 허용(로그인 관련 url)
//                .antMatchers("/").permitAll() //메인 화면 접근 가능
//                .antMatchers("/worries/**").hasAnyRole("USER","ADMIN")
//                .anyRequest().authenticated() // 위에서 따로 지정한 접근허용 리소스 설정 후 그 외 나머지 리소스들은 무조건 인증을 완료해야 접근 가능
//                .and()
//                .sessionManagement()    //스프링 시큐리티 session 정책
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 스프링시큐리티가 생성하지도않고 기존것을 사용하지도 않음(JWT 같은토큰방식을 쓸때 사용하는 설정)
//                .and()
//                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
//
//    }
//}