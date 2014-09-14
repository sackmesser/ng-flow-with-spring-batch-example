package com.ttp76.spooltaggenerator.web;

import com.ttp76.spooltaggenerator.service.BFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {

    @Autowired
    BFileService bFileService;

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public
    @ResponseBody
    String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public
    @ResponseBody
    FileSystemResource handleFileUpload(
            @RequestParam("flowChunkNumber") Integer flowChunkNumber,
            @RequestParam("flowChunkSize") Long flowChunkSize,
            @RequestParam("flowCurrentChunkSize") String flowCurrentChunkSize,
            @RequestParam("flowTotalSize") Long flowTotalSize,
            @RequestParam("flowIdentifier") String flowIdentifier,
            @RequestParam("flowFilename") String flowFilename,
            @RequestParam("flowRelativePath") String flowRelativePath,
            @RequestParam("flowTotalChunks") Integer flowTotalChunks,
            @RequestParam("file") MultipartFile file) throws Exception {

        if (!file.isEmpty()) {
            return bFileService.process(file.getInputStream());
        }

        throw new Exception("No file to be processed!");
    }

}