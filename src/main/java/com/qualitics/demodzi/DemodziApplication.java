
package com.qualitics.demodzi;

import com.qualitics.demodzi.constants.Constant;
import com.qualitics.demodzi.services.impl.DziService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class DemodziApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(DemodziApplication.class, args);

		String basePath = new File("").getAbsolutePath();
		System.out.println(basePath);

//		String path_dzi = new File("src/main/resources/"+Constant.PATH_FOLDER_DZI)
//				.getAbsolutePath();
		/*String path_image = new File("src/main/resources/"+Constant.PATH_FOLDER_IMAGES)
				.getAbsolutePath();


		File f4 = new File(path_image+ "DJI_0744.JPG");
		File f5 = new File(path_image + "DJI_0746.JPG");
		DziService dzi = new DziService();
		dzi.generateDziFile(f4);
		dzi.generateDziFile(f5);*/


	}

}