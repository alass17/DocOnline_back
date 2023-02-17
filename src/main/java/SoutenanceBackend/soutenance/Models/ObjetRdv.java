package SoutenanceBackend.soutenance.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "objetrdv")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ObjetRdv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idobjet;
    private String libelle;


   /* @JsonIgnore
    @ManyToMany(mappedBy = "objetRdvs")
    List<RendezVous> rendezVous =new ArrayList<>();*/

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "objetRdv")
    @JsonIgnore
    private List<RendezVous> rendezVous = new ArrayList<>();
}
