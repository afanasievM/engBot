package com.bot.engBot.service;

import com.bot.engBot.bot.Bot;
import com.bot.engBot.repository.entity.Vocabulary;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;


@Service
public class SendTestServiceImpl implements SendTestService{
    final private BotUserService botUserService;
    private final Bot engBot;
    final private VocabularyService vocabularyService;
    final private SendBotMessageService sendBotMessageService;
    final static Logger log = Logger.getLogger(SendBotMessageServiceImpl.class);

    @Autowired
    public SendTestServiceImpl(BotUserService botUserService, SendBotMessageService sendBotMessageService,
                                  VocabularyService vocabularyService, Bot engBot) {
        this.botUserService = botUserService;
        this.vocabularyService = vocabularyService;
        this.sendBotMessageService = sendBotMessageService;
        this.engBot = engBot;
    }

// user id, arraylist vocabulary
    @Override
    public void sendWordsToTest() {
        for (Map.Entry entry:ChooseWordsServiceImpl.listToSend.entrySet()) {
            int buttonsNumber = 0;
            Long userId = (Long) entry.getKey();
            ArrayList<Vocabulary> words = (ArrayList<Vocabulary>) entry.getValue();
            if (words.size() == 0) continue;
            List<Vocabulary> allUserWords = new ArrayList<>();
            allUserWords.addAll(vocabularyService.retrieveAllUserWord(userId));
            if (allUserWords.size() < 4) buttonsNumber = allUserWords.size();
            else buttonsNumber = 4;
            for (Vocabulary word : words) {
                List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                ArrayList<Vocabulary> wordsForButtons = new ArrayList<>();
                wordsForButtons.add(word);
                for (int i = 1; i < buttonsNumber; i++) {
                    Random random = new Random();
                    int id = random.nextInt(words.size());
                    if (!wordsForButtons.contains(allUserWords.get(id))) {
                        wordsForButtons.add(allUserWords.get(id));
                    } else {
                        i--;
                    }
                }
                Collections.shuffle(wordsForButtons);
                for (int i = 0; i < wordsForButtons.size(); i++) {
                    Vocabulary wordButton = wordsForButtons.get(i);
                    List<InlineKeyboardButton> button1 = new ArrayList<>();
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(wordButton.getWordTranslation());
                    button.setCallbackData(wordButton.getWord());
                    button1.add(button);
                    buttons.add(button1);
                }
                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                markup.setKeyboard(buttons);

                SendMessage sendMessage = new SendMessage();
                sendMessage.enableMarkdown(true);
                sendMessage.setChatId(userId.toString());
                sendMessage.setParseMode(ParseMode.MARKDOWN);
                sendMessage.setText("*" + word.getWord() + "*");

                sendMessage.setReplyMarkup(markup);
                log.info(sendMessage);

                try {
                    engBot.execute(sendMessage);
                } catch (TelegramApiException e) {
                    //todo add logging to the project.
                    log.error(e);
                }
            }
        }
    }
}
