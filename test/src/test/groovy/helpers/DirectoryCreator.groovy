package helpers

import pages.Config

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

/**
 * Created by Kateryna on 24.12.2017.
 */
class DirectoryCreator {

     private static void createPlanDirectory(def subdirectory = "") throws IOException {

         def path = Paths.get(Config.bambooHome, "xml-data", "build-dir").toString()
         final FileTreeBuilder treeBuilder = new FileTreeBuilder(new File(path))
         treeBuilder.dir("${Config.projKey}-${Config.planKey}-JOB1"){dir(subdirectory)}
     }


     private static void copyFile(String fileName, String subDirectory = "") {

         Files.copy(
             new File(Paths.get(Config.testFiles).toString(), fileName).toPath(),
             new File(Paths.get(Config.bambooHome, "xml-data", "build-dir", "${Config.projKey}-${Config.planKey}-JOB1", subDirectory, fileName).toString()).toPath(),
             StandardCopyOption.REPLACE_EXISTING,
             StandardCopyOption.COPY_ATTRIBUTES
         )
     }
}