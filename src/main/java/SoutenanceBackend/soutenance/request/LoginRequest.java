package SoutenanceBackend.soutenance.request;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank
    private String numeroOrEmail;

    @NotBlank
    private String password;


    public String getNumeroOrEmail() {
        return numeroOrEmail;
    }

    public void setNumeroOrEmail(String numeroOrEmail) {
        this.numeroOrEmail = numeroOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
