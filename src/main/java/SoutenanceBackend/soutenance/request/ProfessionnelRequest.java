package SoutenanceBackend.soutenance.request;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

import javax.validation.constraints.Size;

import java.util.Set;

@Getter
@Setter


public class ProfessionnelRequest {

    @Size(max = 20)
    private String nom;

    private  String imageprofil;
    @Size(max = 20)
    private String numero;


    @Size(max = 50)
    @Email
    private String email;


    @Size(max = 120)
    private String password;


    @Size(max = 120)
    private String confirmpassword;



    @Size(max = 120)
    private String adresse;

    private String document;

    private Double longitude;
    private Double lagitude;

    private Set<String> role;




}
