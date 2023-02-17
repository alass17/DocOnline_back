package SoutenanceBackend.soutenance.response;

import java.util.List;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String nom;
    private String numero;
    private String email;
    private String adresse;
    private List<String> roles;

    public JwtResponse(String accessToken, Long id,String nom, String numero, String email, String adresse,List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.nom = nom;
        this.numero = numero;
        this.email = email;
        this.adresse=adresse;
        this.roles = roles;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public List<String> getRoles() {
        return roles;
    }
}
