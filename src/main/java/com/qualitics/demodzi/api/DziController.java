package com.qualitics.demodzi.api;

import com.qualitics.demodzi.services.impl.DziService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DziController {

    private final  DziService dziSerice;

    @GetMapping("/imageUrl")
    public  ResponseEntity<Map<String, String>>  getDziPath(@RequestParam("url") String url) throws IOException {

        Map<String, String>  result = new HashMap<>();
        String dziPath = dziSerice.getFileByUrlAndGeneratePath(url);
        result.put("dzipath",dziPath);
            return new ResponseEntity<>(result, HttpStatus.OK);
 }
}
