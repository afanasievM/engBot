package com.bot.engBot.commands;

import com.bot.engBot.repository.entity.BotUser;
import com.bot.engBot.repository.entity.Vocabulary;
import com.bot.engBot.service.BotUserService;
import com.bot.engBot.service.SendBotMessageService;
import com.bot.engBot.service.VocabularyService;
import com.bot.engBot.service.VocabularyServiceImpl;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

public class AddCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    private final VocabularyService vocabularyService;
    final private Logger log = Logger.getLogger(AddCommand.class);


    public AddCommand(SendBotMessageService sendBotMessageService, VocabularyService vocabularyService) {
        this.sendBotMessageService = sendBotMessageService;
        this.vocabularyService = vocabularyService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        String cmd = update.getMessage().getText().replace("/add","");
        String wordToLearn = null;
        String translate = null;
        try {
            String[] cmdStructure = cmd.split(";");
            log.info(Arrays.stream(cmdStructure).toArray().toString());
            wordToLearn = cmdStructure[0].replace("@vocabengbot ", "").trim();
            translate = cmdStructure[1];
        } catch (Exception e){
            log.info(e);
            sendBotMessageService.sendMessage(chatId, "Please user correct form: \n/add word:translate");
            return;
        }
        String finalWordToLearn = wordToLearn;
        String finalTranslate = translate;
        vocabularyService.findByWordAndOwnerId(wordToLearn,chatId).ifPresentOrElse(
                word ->{
                    log.info("OLD WORD");
                    sendBotMessageService.sendMessage(chatId, "You have this word\nIf you want you can correct translation by command\n/correct word;translation");
                },
                ()->{
                    log.info("NEW WORD");
                    Vocabulary vocabulary = new Vocabulary();
                    vocabulary.setOwnerId(chatId);
                    vocabulary.setWord(finalWordToLearn);
                    vocabulary.setWordTranslation(finalTranslate);
                    vocabulary.setRepeats(VocabularyServiceImpl.REPEATS);
                    vocabulary.setActive(true);
                    sendBotMessageService.sendMessage(chatId, "You add new word:" +
                                                        "\nword -> " + finalWordToLearn +
                                                        "\ntranslation -> " + finalTranslate +
                                                        "\nrepeats -> " + VocabularyServiceImpl.REPEATS);
                    vocabularyService.addWord(vocabulary);
                }
        );

//        botUserService.findByChatId(chatId).ifPresentOrElse(
//                user -> {
//                    user.setActive(true);
//                    user.setFirst_name(update.getMessage().getChat().getFirstName());
//                    user.setFirst_name(update.getMessage().getChat().getUserName());
//                    botUserService.save(user);
//                    log.info("OLD USER");
//                },
//                () -> {
//                    log.info("NEW USER");
//                    BotUser botUser = new BotUser();
//                    botUser.setActive(true);
//                    botUser.setId(chatId);
//                    botUser.setFirst_name(update.getMessage().getChat().getUserName());
//                    botUser.setFirst_name(update.getMessage().getChat().getFirstName());
//                    botUser.setUser_language("ua");
//                    botUser.setRole("user");
//                    botUser.setJoin_at(new Timestamp(new Date().getTime()));
//                    log.info(new Date().getTime());
//                    botUserService.save(botUser);
//                });
//
//        sendBotMessageService.sendMessage(chatId, START_MESSAGE);

//        int activeUserCount = botUserService.retrieveAllActiveUsers().size();
//        if (!users.containsKey(chatId)) {
//            log.info("NEW USER");
//            User user = new User(update.getMessage().getChat());
//            users.put(chatId,user);
//            jsonDump(USERS_PATH, users);
//            // Java object to JSON file
//        } else log.info("OLD USER");
//        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), START_MESSAGE);
    }
}
//case "/add":
//        boolean wordContains = false;
//        String wordToLearn = null;
//        String translate = null;
//        try {
//        wordToLearn = cmd[2].split(";")[0].replace("@vocabengbot ", "").trim();
//        translate = cmd[2].split(";")[1];
//        } catch (Exception e){
//        log.info(e);
//        sendMsg(chatId.toString(), "Please user correct form: \n/add word:translate");
//        break;
//        }
//        Word word = new Word(wordToLearn,translate);
//        for (Word w:words) {
////                        log.info(w.getWord() + " comapare " + wordToLearn);
//        if (w.getWord().equals(wordToLearn)){
//        wordContains = true;
//        break;
//        }
//        }
//        log.info("contain word " + wordContains);
//        if (!wordContains) {
//        this.words.add(word);
//        log.info("NEW WORD");
//        jsonDump(WORDS_PATH, words);
//        } else log.info("OLD WORD");
//        if (!vocabulary.isEmpty()) {
//        vocabulary.forEach((k, v) -> {
//        if (!v.containsKey(words.indexOf(word))) {
//        log.info("NEW USER WORD");
//        v.put(words.indexOf(word), this.repeats);
//        jsonDump(VOCABULARY_PATH, vocabulary);
//        } else {
//        log.info("OLD USER WORD");
//
//        }
//        });
//        } else {
//        log.info("Vocabulary is empty");
//
//        for (Map.Entry<Long,User> entry:users.entrySet()) {
//        Long userID = entry.getKey();
//        HashMap<Integer,Integer> userVocabulary = new HashMap<>();
//        userVocabulary.put(words.indexOf(word),this.repeats);
//        vocabulary.put(userID,userVocabulary);
//        jsonDump(VOCABULARY_PATH, vocabulary);
//        }
//
//
//
//        }
//        log.info(vocabulary);
//        sendMsg(chatId.toString(), "word -> " + wordToLearn + " translate -> " + translate + "\nrepeats to learn -> " + this.repeats);