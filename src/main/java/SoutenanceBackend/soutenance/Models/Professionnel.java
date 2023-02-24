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
import java.util.Collection;
import java.util.List;


@Table(name = "professionnel")
@Entity
@Getter
@Setter
@Component
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Professionnel extends User{


    public Professionnel(String nom, String imageprofil , String numero, @Email String email, String password, String confirmpassword, String adresse, String document,Double latitude,Double longitude) {
        super(nom, numero, email, password, confirmpassword, adresse);
        this.document = document;
        this.imageprofil=imageprofil;
        this.latitude=latitude;
        this.longitude=longitude;
    }



    private String imageprofil;
    private String document;

    private Double longitude;
    private Double latitude;


    @ManyToMany
    @JoinTable(name = "specialite_prof",
            joinColumns = {
                    @JoinColumn(name = "idspec")},
                    inverseJoinColumns = {@JoinColumn(name = "id")})
    List<Specialite> specialites=new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "professionnel")
    @JsonIgnore
    private List<RendezVous> rendezVous = new ArrayList<>();

    @ManyToMany
    List<Notification> notifications = new ArrayList<>();
}
