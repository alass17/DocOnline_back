package SoutenanceBackend.soutenance.services;

import SoutenanceBackend.soutenance.Models.ObjetRdv;

import java.util.List;

public interface ObjetRdvService {

    Object ajouter (ObjetRdv objetRdv);

    String supprimer (Long idobjet);

    List<ObjetRdv> lister();

    String modifier(Long idobjet , ObjetRdv objetRdv);


}
