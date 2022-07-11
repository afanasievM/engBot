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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;


@Service
public class SendTestServiceImpl implements SendTestService {
    final private BotUserService botUserService;
    final private Bot engBot;
    final private VocabularyService vocabularyService;
    final private SendBotMessageService sendBotMessageService;
    final private Logger log = Logger.getLogger(SendTestServiceImpl.class);

    @Autowired
    public SendTestServiceImpl(BotUserService botUserService, SendBotMessageService sendBotMessageService,
                               VocabularyService vocabularyService, Bot engBot) {
        this.botUserService = botUserService;
        this.vocabularyService = vocabularyService;
        this.sendBotMessageService = sendBotMessageService;
        this.engBot = engBot;
    }

    @Override
    public void sendWordsToTest() {
        for (Map.Entry entry : ChooseWordsServiceImpl.listToSend.entrySet()) {
            Long userId = (Long) entry.getKey();
            List<Vocabulary> words = (List<Vocabulary>) entry.getValue();
            if (words.isEmpty()) {
                continue;
            }
            List<Vocabulary> allUserWords = vocabularyService.retrieveAllUserWord(userId);
            int buttonsNumber = getButtonsNumber(allUserWords.size());
            for (Vocabulary word : words) {
                List<Vocabulary> wordsForButtons = chooseWordsForButtons(buttonsNumber, word, allUserWords);
                List<List<InlineKeyboardButton>> buttons = prepareButtons(wordsForButtons);
                sendMessage(word, buttons, userId);
            }
        }
    }

    private List<List<InlineKeyboardButton>> prepareButtons(List<Vocabulary> wordsForButtons) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        log.info("shuffle");
        Collections.shuffle(wordsForButtons);
        for (int i = 0; i < wordsForButtons.size(); i++) {
            Vocabulary wordButton = wordsForButtons.get(i);
            List<InlineKeyboardButton> buttonsLine = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(wordButton.getWordTranslation());
            button.setCallbackData(wordButton.getWord());
            buttonsLine.add(button);
            buttons.add(buttonsLine);
        }
        return buttons;
    }

    private List<Vocabulary> chooseWordsForButtons(int buttonsNumber, Vocabulary word, List<Vocabulary> allUserWords) {
        List<Vocabulary> wordsForButtons = new ArrayList<>();
        wordsForButtons.add(word);
        for (int i = 1; i < buttonsNumber; i++) {
            Random random = new Random();
            int id = random.nextInt(allUserWords.size());
            if (!wordsForButtons.contains(allUserWords.get(id))) {
                wordsForButtons.add(allUserWords.get(id));
            } else {
                i--;
            }
        }
        return wordsForButtons;
    }

    private void sendMessage(Vocabulary word, List<List<InlineKeyboardButton>> buttons, Long userId) {
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
            log.error(e);
        }
    }

    private int getButtonsNumber(int userWordsCount) {
        int buttonsNumber;
        if (userWordsCount < 4) {
            buttonsNumber = userWordsCount;
        } else {
            buttonsNumber = 4;
        }
        return buttonsNumber;
    }
}
