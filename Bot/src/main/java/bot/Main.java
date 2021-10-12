package bot;


import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Bot bot = new Bot(args[0],args[1]);
        System.out.println(bot.getBotToken());
        System.out.println(bot.getBotUsername());
        System.out.println("test");

    }
}
