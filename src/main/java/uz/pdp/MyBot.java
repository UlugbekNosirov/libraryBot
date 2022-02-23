package uz.pdp;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.pdp.configs.PConfig;
import uz.pdp.handlers.UpdateHandler;


public class MyBot extends TelegramLongPollingBot {
    private static final MyBot instance = new MyBot();

    public MyBot() {
    }

    public void onUpdateReceived(Update update) {
        UpdateHandler.handler(update);
    }

    public void send(BotApiMethod message) {
        try {
            this.execute(message);
        } catch (TelegramApiException var3) {
            var3.printStackTrace();
        }

    }

    public void send(SendPhoto sendPhoto) {
        try {
            this.execute(sendPhoto);
        } catch (TelegramApiException var3) {
            var3.printStackTrace();
        }

    }

    public void send(SendDocument sendDocument) {
        try {
            this.execute(sendDocument);
        } catch (TelegramApiException var3) {
            var3.printStackTrace();
        }
    }
    public void send(SendVideo sendVideo) {
        try {
            this.execute(sendVideo);
        } catch (TelegramApiException var3) {
            var3.printStackTrace();
        }
    }

    public String getBotUsername() {
        return PConfig.getInstance().getFromAppProperties("bot.username");
    }

    public String getBotToken() {
        return PConfig.getInstance().getFromAppProperties("bot.token");
    }

    public static MyBot getInstance() {
        return instance;
    }
}
