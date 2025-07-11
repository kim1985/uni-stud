// 3. Utility JWT molto semplice
// JwtUtil.java
package com.uni.unistud.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Utility per la gestione dei token JWT
 *
 * Responsabilità:
 * - Generazione token JWT con email come subject
 * - Validazione token (firma e scadenza)
 * - Estrazione dati dal token (email, scadenza)
 * - Gestione sicura delle chiavi di firma
 *
 * Utilizza la libreria JJWT v0.12+ con API moderna (zero deprecation warnings)
 */
@Component
public class JwtUtil {

    /**
     * Chiave segreta per firmare i token, codificata in Base64
     * Caricata da application.properties con valore di default
     * IMPORTANTE: Cambiare in produzione con una chiave più sicura!
     */
    @Value("${app.jwt.secret:bXlTZWNyZXRLZXlGb3JKV1RUb2tlbkdlbmVyYXRpb25BbmRWYWxpZGF0aW9uUHVycG9zZXM=}")
    private String jwtSecret;

    /**
     * Durata del token in millisecondi
     * Default: 86400000ms = 24 ore
     * Configurabile tramite application.properties
     */
    @Value("${app.jwt.expiration:86400000}")
    private long jwtExpiration;

    /**
     * Genera un nuovo token JWT per l'utente specificato
     *
     * Il token contiene:
     * - Subject: email dell'utente
     * - IssuedAt: data/ora di creazione
     * - Expiration: data/ora di scadenza (now + jwtExpiration)
     * - Signature: firma HMAC-SHA256 con chiave segreta
     *
     * @param email email dell'utente da includere nel token
     * @return token JWT come stringa (formato: header.payload.signature)
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)                    // Imposta l'email come subject del token
                .issuedAt(new Date())              // Data di creazione = adesso
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))  // Data scadenza
                .signWith(getSigningKey())         // Firma con chiave HMAC-SHA256 (algoritmo automatico)
                .compact();                        // Converte in stringa Base64
    }

    /**
     * Estrae l'email dell'utente dal token JWT
     *
     * @param token token JWT da cui estrarre l'email
     * @return email dell'utente (subject del token)
     * @throws Exception se il token non è valido o è malformato
     */
    public String getEmailFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.getSubject();  // Subject contiene l'email
    }

    /**
     * Verifica se il token è scaduto confrontando la data di expiration con l'ora attuale
     *
     * @param token token JWT da verificare
     * @return true se il token è scaduto, false se è ancora valido
     */
    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());  // Scaduto se expiration < now
    }

    /**
     * Estrae la data di scadenza dal token JWT
     *
     * @param token token JWT da cui estrarre la scadenza
     * @return data di scadenza del token
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.getExpiration();  // Campo 'exp' del payload JWT
    }

    /**
     * Valida completamente un token JWT
     *
     * Controlli effettuati:
     * 1. Formato token corretto (3 parti separate da punti)
     * 2. Firma valida (verificata con chiave segreta)
     * 3. Token non scaduto (expiration > now)
     *
     * @param token token JWT da validare
     * @return true se il token è valido e non scaduto, false altrimenti
     */
    public boolean validateToken(String token) {
        try {
            System.out.println("=== VALIDATING TOKEN ===");
            System.out.println("Token length: " + token.length());
            // Se riesco a estrarre i claims senza eccezioni, il token è ben formato e firmato correttamente
            Claims claims = getAllClaimsFromToken(token);
            System.out.println("Claims estratti con successo");
            System.out.println("Subject: " + claims.getSubject());
            System.out.println("Expiration: " + claims.getExpiration());

            // Controllo aggiuntivo: verifica che non sia scaduto
            boolean expired = isTokenExpired(token);
            System.out.println("Is expired: " + expired);

            return !expired;

        } catch (Exception e) {
            // Qualsiasi eccezione significa token non valido
            // (firma errata, formato malformato, algoritmo non supportato, etc.)
            System.out.println("ERRORE validazione token: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return false;
        }
    }

    /**
     * Metodo helper per estrarre tutti i claims dal token JWT
     *
     * Utilizza l'API moderna di JJWT v0.12+:
     * - verifyWith() invece di setSigningKey() (deprecato)
     * - parseSignedClaims() invece di parseClaimsJws() (deprecato)
     * - getPayload() invece di getBody() (deprecato)
     *
     * @param token token JWT da parsare
     * @return oggetto Claims contenente tutti i dati del payload
     * @throws Exception se il token è malformato o la firma non è valida
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()                      // Crea un parser JWT
                .verifyWith(getSigningKey())      // Imposta la chiave per verificare la firma
                .build()                          // Costruisce il parser configurato
                .parseSignedClaims(token)         // Parsa e verifica il token
                .getPayload();                    // Estrae il payload (claims)
    }

    /**
     * Crea la chiave di firma sicura a partire dal secret configurato
     *
     * Processo:
     * 1. Decodifica il secret da Base64 a byte array
     * 2. Genera una SecretKey HMAC-SHA256 dai byte
     *
     * La chiave risultante è usata per:
     * - Firmare i token durante la generazione
     * - Verificare la firma durante la validazione
     *
     * @return chiave segreta per HMAC-SHA256
     */
    private SecretKey getSigningKey() {
        // Decodifica il secret da Base64 (configurato in application.properties)
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);

        // Crea una SecretKey per HMAC-SHA256 dai byte decodificati
        return Keys.hmacShaKeyFor(keyBytes);
    }
}