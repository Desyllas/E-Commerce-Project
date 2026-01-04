package dev.desyllas.E_CommerceProject.security;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;


@Service
public class TokenBlacklistService {


    private final ConcurrentHashMap<String, Long> blacklist = new ConcurrentHashMap<>();


    //Blacklist a token until the given expiration time (epoch millis)
    public void blacklistToken(String token, long expirationEpochMillis) {
        if (token == null || token.isBlank()) return;
        if (expirationEpochMillis <= System.currentTimeMillis()) return;
        blacklist.put(token, expirationEpochMillis);
    }

    public boolean isBlacklisted(String token) {
        if (token == null || token.isBlank()) return false;
        Long exp = blacklist.get(token);
        if (exp == null) return false;

        if (exp < System.currentTimeMillis()) {
            blacklist.remove(token);
            return false;
        }
        return true;
    }

    public void remove(String token) {
        if (token == null) return;
        blacklist.remove(token);
    }
}
