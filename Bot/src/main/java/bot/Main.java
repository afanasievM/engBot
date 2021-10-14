package bot;
//
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.config.*;




public class Main {

    final static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        PropertyConfigurator.configure(System.getProperty("user.dir") +"/Bot/src/main/resources/log4j.propeties");

        logger.error("ERROR");
        logger.warn("WARNING");
        logger.debug("DEBUG");
        logger.info("INFO");
        System.out.println("Final Output");



        Bot bot = new Bot(args[0],args[1]);
        System.out.println(bot.getBotToken());
        System.out.println(bot.getBotUsername());
        System.out.println("test");

    }
}
