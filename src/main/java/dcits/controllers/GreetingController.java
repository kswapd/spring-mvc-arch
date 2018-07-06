package dcits.controllers;

/**
 * Created by kongxiangwen on 5/21/18 w:21.
 */
import dcits.models.Animal;
import dcits.models.People;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GreetingController {

	@Autowired
	People p;
	@Autowired
	Animal animal;

	@GetMapping("/greeting")
	@ResponseBody
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		//People p = (People) ctx.getBean("cutesource");
		System.out.println(p.getName());
		return "greeting " + name;
	}


	@GetMapping("/greeting2")
	public String greeting2(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		System.out.println(p.getName());
		return "greeting2";
	}

	@RequestMapping("/sb")
	@ResponseBody
	String home() {
		return "Hello Spring boot!" + "people:"+p.getName()+"animal:"+animal.getAnimalName();
	}

}
