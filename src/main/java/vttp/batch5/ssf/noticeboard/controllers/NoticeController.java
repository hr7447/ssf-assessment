package vttp.batch5.ssf.noticeboard.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.json.JsonObject;
import jakarta.validation.Valid;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.services.HealthCheckService;
import vttp.batch5.ssf.noticeboard.services.NoticeService;

@Controller
public class NoticeController {

    @Autowired
    private NoticeService noticeSvc;

    @Autowired
    private HealthCheckService healthSvc;

    @GetMapping("/")
    public String showNoticeForm(Model model) {
        model.addAttribute("notice", new Notice());
        return "notice";
    }

    @PostMapping("/notice")
    public String postNotice(@Valid Notice notice, BindingResult binding, Model model) {
        if (binding.hasErrors()) {
            return "notice";
        }

        Optional<JsonObject> opt = noticeSvc.postToNoticeServer(notice);
        
        if (opt.isEmpty()) {
            model.addAttribute("error", "I/O error on POST request for \"http://localhost:3000/notice\": Connection refused");
            return "error";
        }

        JsonObject response = opt.get();
        model.addAttribute("noticeId", response.getString("id"));
        return "success";
    }

    @GetMapping("/status")
    public ResponseEntity<String> healthCheck() {
        boolean isHealthy = healthSvc.isHealthy();
        
        if (isHealthy) {
            return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body("{}");
        }
        
        return ResponseEntity
            .status(503)
            .contentType(MediaType.APPLICATION_JSON)
            .body("{}");
    }
}
