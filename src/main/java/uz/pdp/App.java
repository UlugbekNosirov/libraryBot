package uz.pdp;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import uz.pdp.configs.PConfig;

public class App {
    public App() {
    }

    public static void main(String[] args) {
        PConfig.getInstance().load();

        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(new MyBot());
        } catch (TelegramApiException var2) {
            var2.printStackTrace();
        }

    }
}
