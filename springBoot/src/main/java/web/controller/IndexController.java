package web.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web.model.User;



@Controller
@RequestMapping("/")
public class IndexController {

    @GetMapping()
    public String indexView(Model model) {
        User user = new User();
        model.addAttribute("users", user);
        return "index";
    }
}
