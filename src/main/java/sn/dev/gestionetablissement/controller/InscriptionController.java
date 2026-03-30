package sn.dev.gestionetablissement.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import sn.dev.gestionetablissement.Service.InscriptionService;
import sn.dev.gestionetablissement.model.Inscrire;
import sn.dev.gestionetablissement.model.Etudiant; // Import ajouté

import java.util.List;

@Controller
public class InscriptionController {

    private final InscriptionService inscriptionService;

    @Autowired
    public InscriptionController(InscriptionService inscriptionService) {
        this.inscriptionService = inscriptionService;
    }

    @GetMapping("/inscrire")
    public String inscrire(Model model) {
        // Récupérer toutes les inscriptions
        List<Inscrire> inscrires = inscriptionService.findAll();
        model.addAttribute("listeInscrpt", inscrires);

        // Ajouter un objet Inscrire avec un Etudiant initialisé
        Inscrire inscrire = new Inscrire();
        inscrire.setEtudiant(new Etudiant()); // ← important !
        model.addAttribute("inscrire", inscrire);

        return "inscrire";
    }

    @PostMapping("/save")
    public String save(@Valid Inscrire inscrire, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // S'il y a des erreurs, renvoyer sur le formulaire avec les données existantes
            model.addAttribute("listeInscrpt", inscriptionService.findAll());
            return "inscrire";
        }
        inscriptionService.create(inscrire);
        return "redirect:/inscrire";
    }
}