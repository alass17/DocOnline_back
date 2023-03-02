package SoutenanceBackend.soutenance.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Table(name = "rdv")
@NoArgsConstructor

@Entity
@Getter
@Setter
public class RendezVous {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idrdv;
    private LocalDate date;
    //private LocalTime heure;

    private boolean isActive;


   /* @ManyToMany
    @JoinTable(name = "objet_rdv",
            joinColumns = {
                    @JoinColumn(name = "idrdv")},
            inverseJoinColumns = {@JoinColumn(name = "idobjet")})
    List<ObjetRdv> objetRdvs=new ArrayList<>();*/


    @ManyToOne
    //@JoinColumn(name = "objetRdv_id")
    private ObjetRdv objetRdv;


    @ManyToOne
    //@JoinColumn(name = "professionnel_id")
    private Professionnel professionnel;

    @ManyToOne
   // @JoinColumn(name = "patient_id")
    private Patient patient;



    @ManyToOne
   // @JoinColumn(name = "fuseauHoraire_id", nullable = false)
    private FuseauHoraire fuseauHoraire;

    public RendezVous( LocalDate date, boolean isActive, Professionnel professionnel, Patient patient,ObjetRdv objetRdv, FuseauHoraire fuseauHoraire) {

        this.date = date;
        this.isActive = isActive;
        this.professionnel = professionnel;
        this.patient = patient;
        this.objetRdv=objetRdv;
        this.fuseauHoraire = fuseauHoraire;
    }
}
