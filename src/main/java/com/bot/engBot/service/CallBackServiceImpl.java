package com.bot.engBot.service;

import com.bot.engBot.bot.Bot;
import com.bot.engBot.repository.entity.Vocabulary;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class CallBackServiceImpl implements CallBackService{
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

        EditMessageReplyMarkup edit = new EditMessageReplyMarkup(chatId.toString(), callBack.getMessage().getMessageId(),callBack.getInlineMessageId(),null);

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
        if (wordFromMessage.equals(wordFromMessageData)){

            messageToSend = testWord.getWord() + " -> " + testWord.getWordTranslation() + ".\nTRUE";
            answerCallBack.setText(messageToSend);
            try {
                engBot.execute(answerCallBack);
            } catch (Exception e){
                log.info(e);
            }
            vocabularyService.wordsRepeatsDecrease(testWord);
        } else {
            messageToSend = "FALSE\n" + wordFromMessage + " repeats reset to " + VocabularyServiceImpl.REPEATS;

            answerCallBack.setText(messageToSend);

            try {
                engBot.execute(answerCallBack);
            } catch (Exception e){
                log.info(e);
            }
            vocabularyService.wordsReset(testWord);
        }



    }
}