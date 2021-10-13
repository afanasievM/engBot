package bot;

import org.apache.log4j.Logger;


public class Main {

    private static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {

        logger.error("ERROR");
        logger.warn("WARNING");
        logger.fatal("FATAL");
        logger.debug("DEBUG");
        logger.info("INFO");
        System.out.println("Final Output");



        Bot bot = new Bot(args[0],args[1]);
        System.out.println(bot.getBotToken());
        System.out.println(bot.getBotUsername());
        System.out.println("test");

    }
}
