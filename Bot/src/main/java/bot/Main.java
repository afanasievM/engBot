package bot;
//
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.config.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;


public class Main {

    final static Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        PropertyConfigurator.configure(System.getProperty("user.dir") +"/Bot/src/main/resources/log4j.propeties");

        log.error("ERROR");
        log.warn("WARNING");
        log.debug("DEBUG");
        log.info("INFO");
        System.out.println("Final Output");


//        ApiContextInitializer.init();
        Bot bot = new Bot(args[0],args[1]);


    }
}
