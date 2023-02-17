package SoutenanceBackend.soutenance.Models;

import SoutenanceBackend.soutenance.Models.RendezVous;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fuseauhoraire")

@NoArgsConstructor
@Getter
@Setter
public class FuseauHoraire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalTime timeStart;
    private LocalTime timeEnd;

    public FuseauHoraire(Long id, LocalTime timeStart, LocalTime timeEnd) {
        this.id = id;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;

    }
}


  /*  @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "patient")
  //  @JsonIgnore
    private List<RendezVous> rendezVous = new ArrayList<>();*/