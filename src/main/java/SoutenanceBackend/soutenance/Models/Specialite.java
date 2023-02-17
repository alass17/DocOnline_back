package SoutenanceBackend.soutenance.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Table(name = "specialite")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Specialite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idspec;
    private String libelle;


    @JsonIgnore
    @ManyToMany(mappedBy = "specialites")
    List<Professionnel>professionnels=new ArrayList<>();
}
