package io.pivotal.mday.weekly.report.sentiment.gui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GuiController {
	@RequestMapping("/")
	public String index(Model model) {
		return "index";
	}

}
