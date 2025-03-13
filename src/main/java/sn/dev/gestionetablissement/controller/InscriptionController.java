package sn.dev.gestionetablissement.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sn.dev.gestionetablissement.Service.InscriptionService;
import sn.dev.gestionetablissement.model.Inscrire;

import java.util.List;

@Controller
public class InscriptionController {
    private final InscriptionService inscriptionService;

    @Autowired
    public InscriptionController(InscriptionService inscriptionService) {
        this.inscriptionService = inscriptionService;
    }

    @GetMapping("/inscrire")
    public String inscrire(Model model, @RequestParam(name = "query", defaultValue = "", required = false) String query) {
        List<Inscrire> inscrires;
        if (query.isEmpty()) {
            inscrires = inscriptionService.findAll();
        } else {
//            inscrires = inscriptionService.findAllByAnnAccContaining(query);
        }

//        model.addAttribute("listeInscrpt", inscrires);
        model.addAttribute("inscrire", new Inscrire());
        model.addAttribute("query", query);
        return "insrire";
    }


    @PostMapping("/save")
    public String save(@Valid Inscrire inscrire, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/insrire";
        }
        inscriptionService.create(inscrire);
        return "redirect:/insrire";
    }
}
