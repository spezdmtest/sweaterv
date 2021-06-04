package com.example.sweaterv.controller;

import com.example.sweaterv.domain.Message;
import com.example.sweaterv.domain.User;
import com.example.sweaterv.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    @Autowired
    private MessageRepo messageRepo;

    @GetMapping("/")
    public String greeting(Model model) {
        return "greeting";
    }
    @GetMapping("/main")
    public String main(@RequestParam(required = false,defaultValue = "") String filter, Model model){
        Iterable<Message> messages = messageRepo.findAll();
        if (filter != null && !filter.isEmpty()) {
            messages = messageRepo.findByTag(filter);
        } else {
            messages = messageRepo.findAll();
        }
        model.addAttribute( "messages",messages);
        model.addAttribute("filter",filter);
        return "main";
    }

    @PostMapping("/main")
    public String add(
                      @AuthenticationPrincipal User user,
                      @RequestParam String text,
                      @RequestParam String tag, Model model) {
        Message message = new Message(text, tag, user);
        messageRepo.save(message);

        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages",messages);
        return "main";
    }
}