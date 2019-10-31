package com.qualitics.demodzi.services.impl;

import com.qualitics.demodzi.constants.Constant;
import gov.nist.isg.archiver.DirectoryArchiver;
import gov.nist.isg.archiver.FilesArchiver;
import gov.nist.isg.pyramidio.BufferedImageReader;
import gov.nist.isg.pyramidio.PartialImageReader;
import gov.nist.isg.pyramidio.ScalablePyramidBuilder;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Slf4j
public class DziService {

    @Autowired
    Environment env;

    public String generateDziFile(MultipartFile fImage) throws IOException {
        String basePath = new File("").getAbsolutePath();
        System.out.println(fImage.getOriginalFilename());
        String path_dzi = env.getProperty("root.location.store");
        String pathImageToSplit = env.getProperty("root.location.storeImage");
//        String path_dzi = new File("src/main/resources/"+Constant.PATH_FOLDER_DZI)
//                .getAbsolutePath();
        File outputFolder = new File(path_dzi);
        File file = new File(pathImageToSplit, Objects.requireNonNull(fImage.getOriginalFilename()));

        FileUtils.writeByteArrayToFile(file, fImage.getBytes());

        ScalablePyramidBuilder spb = new ScalablePyramidBuilder(254, 1, "jpeg", "dzi");
        FilesArchiver archive = new DirectoryArchiver(outputFolder);
        PartialImageReader pir = new BufferedImageReader(file);
        String ext = getExtensionByStringHandling(fImage.getOriginalFilename()).get();
        log.info("NOM ORIGINAL IMAGE  :"+fImage.getOriginalFilename());
        log.info("EXTENSION IMAGE  :"+ext);
        String fileName = fImage.getOriginalFilename().substring(0,fImage.getOriginalFilename().lastIndexOf("."));
        System.out.println("image name "+fImage.getName());
        System.out.println("Name Fichier : "+fileName);
        log.info("NOM IMAGE  :"+fileName);
        spb.buildPyramid(pir, fileName, archive, 1);
        log.info("DZI IMAGE  :"+Constant.PATH_FOLDER_IMAGES+"/"+fileName+"_files");
        return Constant.PATH_FOLDER_IMAGES+"/"+fileName+"_files";
    }

    public String getFileByUrlAndGeneratePath(String url) throws IOException {
        log.info("URL IMAGE :"+url);
        URL imageUrl = new URL(url);
        URLConnection uc;
        uc = imageUrl.openConnection();
        uc.addRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        String destinationFile = "sample.jpg";
       // Create Buffered Image by Reading from Url using ImageIO library
        BufferedImage image = ImageIO.read(imageUrl);
        // Create ByteArrayOutputStream object to handle Image data
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         // Write as Image with Jpeg extension to byteArrayOutputStream created in previous step
        ImageIO.write(image,"jpg",byteArrayOutputStream);
        // Flush image created to byteArrayOutoputStream
        byteArrayOutputStream.flush();
        //  Create Random file name but unique by adding timestamp with extension
        String fileName = RandomString.make() + new Date().getTime() + ".jpg";
        /********
         * Now Create MultipartFile using MockMultipartFile by providing
         * @param fileName name of the file
         * @param imageType like "image/jpg"
         * @param ByteArray from ByteArrayOutputStream
         ********/
        String imageType ="image/jpg";
        MultipartFile multipartFile = new MockMultipartFile(fileName,fileName,imageType,byteArrayOutputStream.toByteArray());
        byteArrayOutputStream.close() ;// Close once it is done saving
        return generateDziFile(multipartFile) ;
    }
    public Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }


}
