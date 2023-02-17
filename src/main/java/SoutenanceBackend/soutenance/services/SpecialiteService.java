package SoutenanceBackend.soutenance.services;

import SoutenanceBackend.soutenance.Models.Specialite;

import java.util.List;
import java.util.Optional;

public interface SpecialiteService {
    Object ajouter (Specialite specialite);
    List<Specialite>afficher();
    String modifier(Long idspec,Specialite specialite);
    String supprimer (Long idspec);



}
