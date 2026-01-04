package dev.desyllas.E_CommerceProject.controller;

import dev.desyllas.E_CommerceProject.dto.AuthRequest;
import dev.desyllas.E_CommerceProject.dto.AuthResponse;
import dev.desyllas.E_CommerceProject.dto.RegisterRequest;
import dev.desyllas.E_CommerceProject.security.AuthenticationService;
import dev.desyllas.E_CommerceProject.security.JwtService;
import dev.desyllas.E_CommerceProject.security.TokenBlacklistService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService service;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthController(AuthenticationService service,
                          JwtService jwtService,
                          TokenBlacklistService tokenBlacklistService) {
        this.service = service;
        this.jwtService = jwtService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = service.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = service.authenticate(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Logout by blacklisting the presented JWT until its natural expiration.
     * Client must send Authorization: Bearer <token>
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }

        String token = authHeader.substring(7);
        try {
            // extract expiration from token claims
            java.util.Date exp = jwtService.extractClaim(token, Claims::getExpiration);
            if (exp != null) {
                tokenBlacklistService.blacklistToken(token, exp.getTime());
            } else {
                tokenBlacklistService.blacklistToken(token, System.currentTimeMillis() + 60_000);
            }
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
