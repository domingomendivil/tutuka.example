package com.tutuka.controllers;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tutuka.entities.Result;
import com.tutuka.facade.TrxComparator;

@Controller
/**
 * Upload controller 
 * @author Domingo Mendivil
 *
 */
public class UploadController {

	
	private TrxComparator comparator = new TrxComparator();

    @GetMapping("/")
    public String index() {
        return "upload";
    }
    
    
    
    public File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException 
    {
    	multipart.getBytes();
    	
    	String rootPath = System.getProperty("catalina.home");
		File dir = new File(rootPath + File.separator + "tmpFiles");
		if (!dir.exists())
			dir.mkdirs();

		// Create the file on server
		File convFile = new File(dir.getAbsolutePath()
				+ File.separator + multipart.getOriginalFilename());
		
    	
       // File convFile = new File( "./"+multipart.getOriginalFilename());
        multipart.transferTo(convFile);
        return convFile;
    }
    
   
    
   

    @PostMapping("/upload") // //new annotation since 4.3
    public String singleFileUpload(@RequestParam("file1") MultipartFile multipart,@RequestParam("file2") MultipartFile multipart2,
                                   RedirectAttributes redirectAttributes,HttpSession session) {

        if (multipart.isEmpty()) {
            session.setAttribute("message", "Please select a file to upload");
            return "redirect:error";
        }
        if (multipart2.isEmpty()) {
        	session.setAttribute("message", "Please select a file to upload");
            return "redirect:error";
        }

        try(BufferedReader fileReader1= multipartToFileReader(multipart);
        	BufferedReader fileReader2= multipartToFileReader(multipart2)) {
                	
          //  File file1 = multipartToFileReader(multipart);
           // File file2 = multipartToFileReader(multipart2);

           // Result result = comparator.compareFiles(file1, file2);
        	Result result = comparator.compareFiles(fileReader1, fileReader2,multipart.getOriginalFilename(),multipart2.getOriginalFilename());
            
            addSession(session,result);
            


        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } 

        return "redirect:/result";
    }
    
    private BufferedReader multipartToFileReader(MultipartFile multipart) throws IllegalStateException, IOException {
    	File file1 = multipartToFile(multipart);
		BufferedReader fileReader1 = new BufferedReader(new FileReader(file1));
		return fileReader1;
	}



	private void addSession(HttpSession session,Result result){
    	 session.setAttribute("file1Name", result.getFileResult1().getFileName());
    	 session.setAttribute("file2Name", result.getFileResult2().getFileName());
    	 session.setAttribute("totalFile1Records", result.getFileResult1().getTotalRecords());
         session.setAttribute("totalFile2Records", result.getFileResult2().getTotalRecords());
         session.setAttribute("matchingRecords", result.getMatchingRecords());
         session.setAttribute("file1UnMatchedRecords", result.getFileResult1().getUnMatchedRecords());
         session.setAttribute("file2UnMatchedRecords", result.getFileResult2().getUnMatchedRecords());
         session.setAttribute("list", result.getUnMatchedRecords());
         
    }
    

    @GetMapping("/result")
    public String result(Model model) {
    	model.addAttribute("name","PEPE");
        return "result";
    }

}