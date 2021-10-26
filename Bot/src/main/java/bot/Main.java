package bot;
//
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.config.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.time.LocalTime;
import java.util.HashMap;


public class Main {

    final static Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        System.out.println(System.getProperty("user.dir"));
        PropertyConfigurator.configure(System.getProperty("user.dir") +"/Bot/src/main/resources/log4j.propeties");

//        ApiContextInitializer.init();
        Bot bot = new Bot("2017292911:AAHDCCSInfwRSwFuA48-Bsu8JWoXy1kkEmg","engBot");
        bot.botConnect();
        log.info("final");
        while (true){
            HashMap<Long,Integer> choosenWords = bot.chooseWord();
            log.info(choosenWords);
            Thread.sleep(1000*60);
            LocalTime timeStart = LocalTime.of(7,59);
            LocalTime timeFinish = LocalTime.of(22,1);
            LocalTime time = LocalTime.now();
            log.info(time);
//            bot.sendWords(choosenWords);
            if (time.getMinute() == 0){
                log.info(time);
                if (time.isAfter(timeStart) && time.isBefore(timeFinish)){
                    log.info("Time to test");
                    bot.sendWords(choosenWords);
                }
            }
//            break;
        }
    }
}
