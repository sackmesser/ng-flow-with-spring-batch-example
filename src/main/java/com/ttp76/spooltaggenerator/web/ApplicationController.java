package com.ttp76.spooltaggenerator.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;

@Controller
public class ApplicationController {

//    @Autowired
//    private HotelRepository hotelRepository;

    @RequestMapping(value = "/upload", headers = "'Content-Type': 'multipart/form-data'", method = RequestMethod.POST)
    public void UploadFile(MultipartHttpServletRequest request, HttpServletResponse response) {

        Iterator<String> itr = request.getFileNames();

        MultipartFile file = request.getFile(itr.next());

        String fileName = file.getOriginalFilename();
        System.out.println(fileName);
    }
}