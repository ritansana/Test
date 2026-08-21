package newConnect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
	@RequestMapping("/orders")
	public class OrderController {

	    @Autowired
	    private SterlingService sterlingService;

	    @PostMapping(value = "/getItem",
	            consumes = "application/xml",
	            produces = "application/xml"
	    )
	    public String getItem(@RequestBody String xml) {

	        return sterlingService.getItem(xml);
	    }
	    
	    @PostMapping("/validateAddress")
	    public String validateAddress(@RequestBody String xml) {
	    	
	        return sterlingService.validateAddress(xml);
	    }
	}

