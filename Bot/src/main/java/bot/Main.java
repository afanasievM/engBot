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

//        ApiContextInitializer.init();
        Bot bot = new Bot("2017292911:AAHDCCSInfwRSwFuA48-Bsu8JWoXy1kkEmg","engBot");
        bot.botConnect();
        log.info("final");

    }
}
