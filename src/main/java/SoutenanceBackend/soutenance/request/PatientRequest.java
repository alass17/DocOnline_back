package SoutenanceBackend.soutenance.request;

import SoutenanceBackend.soutenance.Models.Role;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class PatientRequest {


    private String imageprofil;

    @Size(max = 20)
    private String nom;

    private String numero;


    private String email;


    @Size(max = 120)
    private String password;


    @Size(max = 120)
    private String confirmpassword;



    @Size(max = 120)
    private String adresse;

    private Double longitude;
    private Double lagitude;


    private Set<String> role;



}
