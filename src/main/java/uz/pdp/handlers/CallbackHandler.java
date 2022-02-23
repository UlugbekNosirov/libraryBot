package uz.pdp.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import uz.pdp.MyBot;
import uz.pdp.buttons.InlineBoard;
import uz.pdp.enums.auth.State;
import uz.pdp.models.SessionUser;
import uz.pdp.repository.AuthUserRepository;
import uz.pdp.repository.BookRepository;
import uz.pdp.services.AuthUserService;
import uz.pdp.services.BookService;


public class CallbackHandler {
    private static final SessionUser sessionUser = AuthUserService.getSessionUser();
    private static final MyBot bot = MyBot.getInstance();

    public static void handler(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();
        String chatID = callbackQuery.getMessage().getChatId().toString();
        Integer messageID = callbackQuery.getMessage().getMessageId();
        if (in(callbackData, "uz", "en", "ru")) {
            DeleteMessage deleteMessage = new DeleteMessage(chatID, callbackQuery.getMessage().getMessageId());
            bot.send(deleteMessage);
            AuthUserRepository.getInstance().update(callbackQuery.getFrom().getId().toString(), callbackData);
            SendMessage sendMessage = new SendMessage(chatID, "Til o'zgartirildi");
            bot.send(sendMessage);
        } else if (in(callbackData, "next", "previous")) {
            EditMessageText editMessageText;
            SendMessage sendMessage;
            if (callbackData.equals("next")) {
                if (isNextAble()) {
                    sessionUser.setPage(sessionUser.getPage() + 1);
                    editMessageText = new EditMessageText();
                    editMessageText.setChatId(chatID);
                    editMessageText.setMessageId(messageID);
                    editMessageText.setText(BookService.getText());
                    editMessageText.setReplyMarkup(BookService.getInlineKeyboard());
                    bot.send(editMessageText);
                } else {
                    sendMessage = new SendMessage(chatID, "Siz allaqachon oxirgi sahifadasiz");
                    bot.send(sendMessage);
                }
            } else if (callbackData.equals("previous")) {
                if (sessionUser.getPage() != 1) {
                    sessionUser.setPage(sessionUser.getPage() - 1);
                    editMessageText = new EditMessageText();
                    editMessageText.setChatId(chatID);
                    editMessageText.setMessageId(messageID);
                    editMessageText.setText(BookService.getText());
                    editMessageText.setReplyMarkup(BookService.getInlineKeyboard());
                    bot.send(editMessageText);
                } else {
                    sendMessage = new SendMessage(chatID, "Siz allaqachon birinchi sahifadasiz");
                    bot.send(sendMessage);
                }
            }
        } else {
            if (sessionUser.getState().equals(State.SEARCH_BOOK)) {
                sessionUser.setState(State.LIKE);
                String fileID = BookRepository.getInstance().getFileIDAccepted(Long.valueOf(callbackData));
                SendDocument sendDocument = new SendDocument(chatID, new InputFile(fileID));
                sendDocument.setReplyMarkup(InlineBoard.getInstance().like(callbackData));
                bot.send(sendDocument);
                BookRepository.getInstance().download(Long.valueOf(callbackData));
            } else if (sessionUser.getState().equals(State.SEE_BOOKS_IN_PROCESS)) {
                sessionUser.setState(State.CHECK_BOOK);
                String fileID = BookRepository.getInstance().getFileIDInProcess(Long.valueOf(callbackData));
                SendDocument sendDocument = new SendDocument(chatID, new InputFile(fileID));
                sendDocument.setReplyMarkup(InlineBoard.getInstance().check(callbackData));
                bot.send(sendDocument);
            } else if (sessionUser.getState().equals(State.CHECK_BOOK)) {
                DeleteMessage deleteMessage = new DeleteMessage(chatID, messageID);
                bot.send(deleteMessage);
                Long id = Long.valueOf(callbackData.substring(1));
                if (callbackData.startsWith("a")) {
                    BookRepository.getInstance().accept(id);
                } else if (callbackData.startsWith("r")) {
                    BookRepository.getInstance().reject(id);
                }
                sessionUser.setState(State.SEE_BOOKS_IN_PROCESS);
            } else if (sessionUser.getState().equals(State.LIKE)) {
                DeleteMessage deleteMessage = new DeleteMessage(chatID, messageID);
                bot.send(deleteMessage);
                Long id = Long.valueOf(callbackData.substring(1));
                if (callbackData.startsWith("l")) {
                    BookRepository.getInstance().like(id);
                } else if (callbackData.startsWith("d")) {
                    BookRepository.getInstance().dislike(id);
                }
                sessionUser.setState(State.SEARCH_BOOK);
            }
        }
    }

    private static boolean isNextAble() {
        return sessionUser.getPage() * 10 < BookService.books.size();
    }

    private static boolean in(String callbackData, String... strs) {
        for (String str : strs) {
            if (str.equals(callbackData)) {
                return true;
            }
        }
        return false;
    }
}