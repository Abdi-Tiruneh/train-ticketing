package TrainTicketingSystem.security.filter;

import TrainTicketingSystem.security.service.SecurityUtil;
import TrainTicketingSystem.userManager.user.Users;
import TrainTicketingSystem.userManager.user.dto.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            var parser = JsonParserFactory.getJsonParser();
            var loginInfo = parser.parseMap(body);
            String username = (String) loginInfo.get("username");
            String password = (String) loginInfo.get("password");
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, password);
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new AuthenticationServiceException("Failed to parse username or password from request body", e);
        }
    }

//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
//                                            FilterChain chain, Authentication authResult) throws IOException {
//        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
//        Location user = customUserDetailsService.getByUsername(userDetails.getUsername());
//
//        Map<String, String> tokens = new HashMap<>();
//        tokens.put("access_token", SecurityUtil.generateAccessToken(userDetails));
//        tokens.put("refresh_token", SecurityUtil.generateRefreshToken(userDetails, request));
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
//    }
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
//                                            FilterChain chain, Authentication authResult) throws IOException {
//        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
//        UserResponse user = SecurityUtil.getUser(userDetails.getUsername());
//
//        // Generate tokens
//        String accessToken = SecurityUtil.generateAccessToken(userDetails);
//        String refreshToken = SecurityUtil.generateRefreshToken(userDetails, request);
//
//        // Set tokens in response header
//        response.addHeader("access_token", accessToken);
//        response.addHeader("refresh_token", refreshToken);
//
//        // Set user details in response body
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        new ObjectMapper().writeValue(response.getOutputStream(), user);
//    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        UserResponse user = SecurityUtil.getUser(userDetails.getUsername());

        // Generate tokens
        String accessToken = SecurityUtil.generateAccessToken(userDetails);
        String refreshToken = SecurityUtil.generateRefreshToken(userDetails, request);

        response.addHeader("access_token", accessToken);
        response.addHeader("refresh_token", refreshToken);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        try (PrintWriter writer = response.getWriter()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(writer, user);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
