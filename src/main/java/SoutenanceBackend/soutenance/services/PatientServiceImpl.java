package SoutenanceBackend.soutenance.services;

import SoutenanceBackend.soutenance.Models.Patient;
import SoutenanceBackend.soutenance.Models.Professionnel;
import SoutenanceBackend.soutenance.Repository.PatientRepo;
import SoutenanceBackend.soutenance.Repository.ProfessionnelRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService{

    @Autowired
    ProfessionnelRepo professionnelRepo;
    @Autowired
    PatientRepo patientRepo;
    @Override
    public List<Professionnel> trouverProfessionnelParAdressse(String adresse) {
      //  List<Patient> patient = (List<Patient>) patientRepo.findByAdresse(adresse);

        List<Professionnel> professionnel =professionnelRepo.findAll();
        List<Professionnel> profession = new ArrayList<>();
        for(Professionnel p:professionnel){
            if(p.getAdresse().equals(adresse)){
                profession.add(p);
            }
        }

        return  profession;
    }

    @Override
    public List<Professionnel> TrouverTousLesProfessionnels() {
        return professionnelRepo.findAll();
    }



    public Optional<Professionnel> TrouverProfessionnelParId(Long id) {
        return professionnelRepo.findById(id);



    }

    @Override
    public Patient trouverParId(Long idpatient) {
        return patientRepo.findById(idpatient).get();
    }





}
