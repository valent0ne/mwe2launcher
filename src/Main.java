import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.eclipse.emf.mwe2.launch.runtime.Mwe2Launcher;

import java.io.File;
import java.io.FileNotFoundException;



public class Main {

    //logger
    final private static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    //line separator
    final private static String LINE_SEPARATOR = System.lineSeparator();


    //CLI params declaration


    //path alla root del progetto
    @Parameter(names = {"--project", "-p"}, required = true, description = "absolute path to project root")
    private static String pathToPrj;

    //nome del file .mwe2
    @Parameter(names = {"--mwe2", "-m"}, required = true, description = "name of .mwe2 file")
    private static String mwe2FileName;

    public static void main(String[] args) {
        Main main = new Main();
        JCommander jc = JCommander.newBuilder()
                .addObject(main)
                .build();

        jc.setProgramName("java -jar mwe2launcher.jar");

        try{
            jc.parse(args);

        }catch (Exception e){

            jc.usage();
            System.exit(1);
        }

        if(mwe2FileName.length()<5 || !(mwe2FileName.substring(mwe2FileName.length()-5).equals(".mwe2"))){
            mwe2FileName = mwe2FileName.concat(".mwe2");
        }


        main.run();

    }


    private void run(){

        separator();
        LOGGER.info("[STARTING PROCESS]{}", LINE_SEPARATOR);
        try{
            LOGGER.info("searching for {}...", mwe2FileName);

            String pathToMwe2 = searchFile(new File(pathToPrj), mwe2FileName);
            if(pathToMwe2 == null){
                throw new FileNotFoundException(mwe2FileName+" not found");
            }

            LOGGER.info("file found, running workflow...");

            // lancio workflow mwe2
            Mwe2Launcher.main(new String[]{pathToMwe2, "-p", "rootPath="+pathToPrj});

        }catch(Throwable e){
            LOGGER.error(e.getMessage());
            separator();
            LOGGER.error("[PROCESS ABORTED]{}", LINE_SEPARATOR);
            System.exit(1);
        }
        separator();
        LOGGER.info("[PROCESS COMPLETED]{}", LINE_SEPARATOR);
    }

    static String searchFile(File file, String search) {
        if (file.isDirectory()) {
            File[] arr = file.listFiles();
            assert arr != null;
            for (File f : arr) {
                String found = searchFile(f, search);
                if (found != null)
                    return found;
            }
        } else {
            if (file.getName().equals(search)) {
                return file.getAbsolutePath();
            }
        }
        return null;
    }
    

    private void separator(){ System.out.println(" "); }
}
