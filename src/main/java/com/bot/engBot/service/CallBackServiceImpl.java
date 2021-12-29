package com.bot.engBot.service;

import com.bot.engBot.bot.Bot;
import com.bot.engBot.repository.entity.Vocabulary;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
        String wordFromMessage = callBack.getMessage().getText();
        String wordFromMessageData = callBack.getData();
        log.info(wordFromMessage);
        log.info(wordFromMessage);

        EditMessageReplyMarkup edit = new EditMessageReplyMarkup(chatId.toString(), callBack.getMessage().getMessageId(), callBack.getInlineMessageId(), null);

        try {
            engBot.execute(edit);
        } catch (TelegramApiException e) {
            log.info(e);
        }
        AnswerCallbackQuery answerCallBack = new AnswerCallbackQuery();
        answerCallBack.setCallbackQueryId(callBack.getId());
        answerCallBack.setShowAlert(true);
        String messageToSend;
        Vocabulary testWord = vocabularyService.findByWordAndOwnerId(wordFromMessage, chatId).get();
        if (wordFromMessage.equals(wordFromMessageData)) {

            messageToSend = testWord.getWord() + " -> " + testWord.getWordTranslation() + ".\n<b>TRUE</b>";
            answerCallBack.setText(messageToSend);
            try {
                engBot.execute(answerCallBack);
            } catch (Exception e) {
                log.info(e);
            }

            if (testWord.getRepeats() == 1) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.enableMarkdown(true);
                sendMessage.setChatId(testWord.getOwnerId().toString());
                sendMessage.setParseMode(ParseMode.MARKDOWN);
                sendMessage.setText("You learned *" + testWord.getWord() + "* -> *" + testWord.getWordTranslation() + "*!!!!");

                try {
                    engBot.execute(sendMessage);
                } catch (TelegramApiException e) {
                    //todo add logging to the project.
                    log.error(e);
                }
            }
                vocabularyService.decreaseWordRepeats(testWord);

        }else {
                messageToSend = "<b>FALSE</b>\n" + wordFromMessage + " repeats reset to " + VocabularyServiceImpl.REPEATS;

                answerCallBack.setText(messageToSend);

                try {
                    engBot.execute(answerCallBack);
                } catch (Exception e) {
                    log.info(e);
                }
                vocabularyService.resetWord(testWord);



        }
    }
}