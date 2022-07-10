package com.bot.engBot.service;

import com.bot.engBot.bot.Bot;
import com.bot.engBot.repository.entity.Vocabulary;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import java.io.Serializable;

@Service
public class CallBackServiceImpl implements CallBackService {
    private final VocabularyService vocabularyService;
    final private BotUserService botUserService;
    final private Bot engBot;
    final private Logger log = Logger.getLogger(CallBackServiceImpl.class);

    public CallBackServiceImpl(VocabularyService vocabularyService, BotUserService botUserService, Bot engBot) {
        this.vocabularyService = vocabularyService;
        this.botUserService = botUserService;
        this.engBot = engBot;
    }


    @Override
    public void callBackProcess(CallbackQuery callBack) {
        log.info(callBack.getMessage());
        Long chatId = callBack.getMessage().getChat().getId();
        removeButton(chatId, callBack);
        String message = checkAnswer(chatId, callBack);
        showAlertAnswer(message, callBack);
    }

    private void showAlertAnswer(String messageToSend, CallbackQuery callBack) {
        AnswerCallbackQuery answerCallBack = new AnswerCallbackQuery();
        answerCallBack.setCallbackQueryId(callBack.getId());
        answerCallBack.setShowAlert(true);
        answerCallBack.setText(messageToSend);
        execute(answerCallBack);
    }

    private String checkAnswer(Long chatId, CallbackQuery callBack) {
        String messageToSend;
        String wordFromMessage = callBack.getMessage().getText();
        String wordFromMessageData = callBack.getData();
        log.info(wordFromMessage);
        Vocabulary testWord = vocabularyService.findByWordAndOwnerId(wordFromMessage, chatId).get();
        if (wordFromMessage.equals(wordFromMessageData)) {
            messageToSend = String.format("%s -> %s.\nTRUE", testWord.getWord(), testWord.getWordTranslation());
            if (testWord.getRepeats() == 1) {
                sendThatLearntWord(testWord);
            }
            vocabularyService.decreaseWordRepeats(testWord);
        } else {
            messageToSend = String.format("FALSE\n %s -> %s\n repeats reset to %d",
                    testWord.getWord(),
                    testWord.getWordTranslation(),
                    VocabularyServiceImpl.REPEATS);
            vocabularyService.resetWord(testWord);
        }
        return messageToSend;
    }

    void sendThatLearntWord(Vocabulary testWord) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(testWord.getOwnerId().toString());
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        String text = String.format("You learned *%s* -> *%s*!!!!",
                testWord.getWord(),
                testWord.getWordTranslation());
        sendMessage.setText(text);
        execute(sendMessage);
    }

    void removeButton(Long chatId, CallbackQuery callBack) {
        EditMessageReplyMarkup edit = new EditMessageReplyMarkup(
                chatId.toString(),
                callBack.getMessage().getMessageId(),
                callBack.getInlineMessageId(),
                null);
        execute(edit);
    }

    private <T extends Serializable, Method extends BotApiMethod<T>> void execute(Method method) {
        try {
            engBot.execute(method);
        } catch (Exception e) {
            log.info(e);
        }
    }
}