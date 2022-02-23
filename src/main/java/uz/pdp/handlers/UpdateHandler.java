package uz.pdp.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;

public class UpdateHandler {
    public UpdateHandler() {
    }

    public static void handler(Update update) {
        if (update.hasMessage()) {
            MessageHandler.handler(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            CallbackHandler.handler(update.getCallbackQuery());
        } else {
            System.out.println("Update empty");
        }

    }
}
