import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.eclipse.emf.mwe2.launch.runtime.Mwe2Launcher;

public class Main {

    //logger
    final private static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    //line separator
    final private static String LINE_SEPARATOR = System.lineSeparator();


    //CLI params declaration

    //path al al file mwe2
    @Parameter(names = {"--mwe2", "-m"}, required = true, description = "absolute path to .mwe2 file")
    private static String pathToMwe2;

    //path al al file mwe2
    @Parameter(names = {"--project", "-p"}, required = true, description = "absolute path to project root")
    private static String pathToPrj;



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


        if(pathToMwe2.length()<5 || !(pathToMwe2.substring(pathToMwe2.length()-5).equals(".mwe2"))){
            pathToMwe2 = pathToMwe2.concat(".mwe2");
        }

        main.run();

    }


    private void run(){

        separator();
        LOGGER.info("[STARTING PROCESS]{}", LINE_SEPARATOR);
        try{
            LOGGER.info("running {}...", pathToMwe2);

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


    private void separator(){ System.out.println(" "); }
}
