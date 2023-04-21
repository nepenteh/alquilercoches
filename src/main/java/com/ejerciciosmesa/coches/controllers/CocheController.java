package com.ejerciciosmesa.coches.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.ejerciciosmesa.coches.util.paginator.PageRender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.validation.BindingResult;
import javax.validation.Valid;
import org.springframework.web.bind.support.SessionStatus;



import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import com.ejerciciosmesa.coches.models.services.UploadService;





import com.ejerciciosmesa.coches.appdata.AppData;
import com.ejerciciosmesa.coches.models.entities.Coche;
import com.ejerciciosmesa.coches.models.services.CocheService;




@Controller
@SessionAttributes("coche")
@RequestMapping("/coche")
public class CocheController {

	private final AppData appData;
	private final CocheService cocheService;
	
	
	
	
	
	private final UploadService uploadService;

		
	public static final String OPGEN = "COCHE"; 
	
	public CocheController(UploadService uploadService,
										 
										 
									     CocheService cocheService,
									     AppData applicationData
		   
		   		 
			) {
		this.appData = applicationData;
		this.cocheService = cocheService;
		
		
		

		this.uploadService = uploadService;

	}

		
	
	@GetMapping({ "", "/", "/list", "/list/{page}" })
	public String list(@PathVariable(name = "page", required = false) Integer page, Model model) {
	
		if (page == null)
			page = 0;
		
		fillApplicationData(model,"LIST");
		
		Pageable pageRequest = PageRequest.of(page, 10);
		Page<Coche> pageCoche = cocheService.findAll(pageRequest); 
		PageRender<Coche> paginator = new PageRender<>("/coche/list",pageCoche,5);
		

		model.addAttribute("numcoche", cocheService.count());
		model.addAttribute("listcoche", pageCoche);
		model.addAttribute("paginator",paginator);
		
		model.addAttribute("actualpage", page);
		
		return "coche/list";
	}
	
	@GetMapping({ "/formcr", "/formcr/{page}" })
	public String form(@PathVariable(name = "page", required = false) Integer page, Model model) {
		Coche coche = new Coche();		
		model.addAttribute("coche",coche);
		
		if (page == null)
			page = 0;
		model.addAttribute("actualpage", page);
		
		fillApplicationData(model,"CREATE");
		
		return "coche/form";
	}
	
	@GetMapping({ "/formup/{id}", "/formup/{id}/{page}" })
	public String form(@PathVariable(name = "id") Long id, @PathVariable(name = "page", required = false) Integer page, Model model, RedirectAttributes flash) {
		if (page == null)
			page = 0;
		Coche coche = cocheService.findOne(id);
		if(coche==null) {
			flash.addFlashAttribute("error","Data not found");
			return "redirect:/coche/list/" + page;
		}
		
		model.addAttribute("coche", coche);
		
		model.addAttribute("actualpage", page);
		
		fillApplicationData(model,"UPDATE");
		
		return "coche/form";
	}
	
	
	@PostMapping("/form/{page}")
	@Secured("ROLE_ADMIN")
	public String form(@Valid Coche coche,  
			           BindingResult result, 
					   
					   Model model,
					   @RequestAttribute("file") MultipartFile foto_formname,
@RequestParam("fotoImageText") String fotoImageText,
@RequestParam("fotoImageTextOld") String fotoImageTextOld,

					   @PathVariable(name = "page") int page,
					   RedirectAttributes flash,
					   SessionStatus status) {
		
		boolean creating;
		
		if(coche.getId()==null) {
			fillApplicationData(model,"CREATE");
			creating = true;
		} else {
			fillApplicationData(model,"UPDATE");
			creating = false;
		}
		
		String msg = (coche.getId()==null) ? "Creation successful" : "Update successful";
		
		if(result.hasErrors()) {
			model.addAttribute("actualpage", page);
			return "coche/form";
		}
		
		if(!foto_formname.isEmpty())
	AddUpdateImageFoto(foto_formname,coche);
else {
	if(fotoImageText.isEmpty() && !(fotoImageTextOld.isEmpty())) {
		uploadService.delete(fotoImageTextOld);
		coche.setFoto(null);
	}
}



		
		
		
		cocheService.save(coche);
		status.setComplete();
		flash.addFlashAttribute("success",msg);
		
		if (creating)
			page = lastPage();
		
		return "redirect:/coche/list/" + page;
	}
	
	
	private void AddUpdateImageFoto(MultipartFile image, Coche coche) {
					
			if(coche.getId()!=null &&
			   coche.getId()>0 && 
			   coche.getFoto()!=null &&
			   coche.getFoto().length() > 0) {
			
				uploadService.delete(coche.getFoto());
			}
			
			String uniqueName = null;
			try {
				uniqueName = uploadService.copy(image);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			coche.setFoto(uniqueName);
		
	}

	

	@Secured("ROLE_ADMIN")
	@GetMapping({ "/delete/{id}", "/delete/{id}/{page}" })
	public String delete(@PathVariable(name = "id") Long id,
			@PathVariable(name = "page", required = false) Integer page, RedirectAttributes flash) {
		
		if (page == null)
			page = 0;
		
		if(id>0) { 			
			Coche coche = cocheService.findOne(id);
			
			if(coche != null) {
				
		/* Only if there is required relation with this entity */			
				
		
		/* Only if there is no required relation with this entity, or there is a
		 * required relation but no entity related at the moment, (checked before) */
				
		
		/* Relations revised, the entity can be removed */
				cocheService.remove(id);
			} else {
				flash.addFlashAttribute("error","Data not found");
				return "redirect:/coche/list/" + page;
			}
			
			if(coche.getFoto()!=null)
	uploadService.delete(coche.getFoto());

						
			flash.addFlashAttribute("success","Deletion successful");
		}
		
		return "redirect:/coche/list/" + page;
	}
	
	@GetMapping({ "/view/{id}", "/view/{id}/{page}" })
	public String view(@PathVariable(name = "id") Long id,
			@PathVariable(name = "page", required = false) Integer page, Model model, RedirectAttributes flash) {

		if (page == null)
			page = 0;
		
		if (id > 0) {
			Coche coche = cocheService.findOne(id);

			if (coche == null) {
				flash.addFlashAttribute("error", "Data not found");
				return "redirect:/coche/list/" + page;
			}

			model.addAttribute("coche", coche);
			model.addAttribute("actualpage", page);
			fillApplicationData(model, "VIEW");
			return "coche/view";
			
		}

		return "redirect:/coche/list/" + page;
	}
	
	
	@GetMapping("/viewimg/{id}/{imageField}")
	public String viewimg(@PathVariable Long id, @PathVariable String imageField, Model model, RedirectAttributes flash) {

		if (id > 0) {
			Coche coche = cocheService.findOne(id);

			if (coche == null) {
				flash.addFlashAttribute("error", "Data not found");
				return "redirect:/coche/list";
			}

			model.addAttribute("coche", coche);
			fillApplicationData(model, "VIEWIMG");
			model.addAttribute("backOption",true);
			model.addAttribute("imageField",imageField);
			
			return "coche/viewimg";
			
		}

		return "redirect:/coche/list";
	}
	
	
	
	
	private int lastPage() {
		Long nReg = cocheService.count();
		int nPag = (int) (nReg / 10);
		if (nReg % 10 == 0)
			nPag--;
		return nPag;
	}
	
	private void fillApplicationData(Model model, String screen) {
		model.addAttribute("applicationData",appData);
		model.addAttribute("optionCode",OPGEN);
		model.addAttribute("screen",screen);
	}
	
		
}
