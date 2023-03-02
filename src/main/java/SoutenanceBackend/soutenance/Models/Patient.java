package SoutenanceBackend.soutenance.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Table(name = "patient")
@Entity
@Getter
@Setter
@Component
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor


public class Patient extends User {
        private String imageprofil;
        private Double longitude;
        private Double lagitude;
    public Patient(String imageprofil,String nom,  String numero, String email,String password,  String confirmpassword,String adresse,Double longitude,Double lagitude) {
        super(nom, numero, email, password, confirmpassword, adresse);
        this.imageprofil=imageprofil;
        this.longitude=longitude;
        this.lagitude=lagitude;
    }

    public Patient( String nom, String email) {
        super(nom,email );
    }


    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "patient")
    @JsonIgnore
    private List<RendezVous> rendezVous = new ArrayList<>();


    @ManyToMany
    List<Notification> notifications = new ArrayList<>();



}
