package uz.pdp.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import uz.pdp.MyBot;
import uz.pdp.buttons.InlineBoard;
import uz.pdp.buttons.MarkupBoard;
import uz.pdp.configs.PConfig;
import uz.pdp.enums.auth.Role;
import uz.pdp.enums.auth.State;
import uz.pdp.models.SessionUser;
import uz.pdp.repository.AuthUserRepository;
import uz.pdp.services.AuthUserService;
import uz.pdp.services.BookService;

import java.io.File;


public class MessageHandler {
    private static final PConfig pConfig = PConfig.getInstance();
    private static final SessionUser sessionUser = AuthUserService.getSessionUser();
    private static final MyBot bot = MyBot.getInstance();
    private static final AuthUserRepository authUserRepository = AuthUserRepository.getInstance();

    public static void handler(Message message) {
        String chatID = message.getChatId().toString();
        String userID = message.getFrom().getId().toString();

        if (message.hasText()) {
            String text = message.getText();
            if (text.equals("/start")) {
                AuthUserService.register(message.getFrom(), chatID);
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(chatID);
                sendPhoto.setCaption("Kutubxonamizga xush kelibsiz!");
                if (authUserRepository.getRole(userID).equals(Role.CONTENT_MANAGER.toString())) {
                    sendPhoto.setReplyMarkup(MarkupBoard.getInstance().contentManagerMainMenu());
                    sendPhoto.setPhoto(new InputFile(new File("src/main/resources/statics/contentManager.jpg")));
                } else if (authUserRepository.getRole(userID).equals(Role.ADMIN.toString())) {
                    sendPhoto.setReplyMarkup(MarkupBoard.getInstance().adminMenu());
                    sendPhoto.setPhoto(new InputFile(new File("src/main/resources/statics/admin.jpg")));
                } else {
                    sendPhoto.setReplyMarkup(MarkupBoard.getInstance().mainMenu());
                    sendPhoto.setPhoto(new InputFile(new File("src/main/resources/statics/telegram_bot.jpg")));
                }
                bot.send(sendPhoto);
            } else if (text.equals(pConfig.getFromAppProperties("add.book"))) {
                SendMessage sendMessage = new SendMessage(chatID, "Kitob jo'natishingiz mumkin");
                bot.send(sendMessage);
                sessionUser.setState(State.ADD_BOOK);
            } else if (text.equals(pConfig.getFromAppProperties("search.book"))) {
                sessionUser.setState(State.SEARCH_BOOK);
                SendMessage sendMessage = new SendMessage(chatID, "Kitob nomini kiriting:");
                bot.send(sendMessage);
            } else if (text.equals(PConfig.getInstance().getFromAppProperties("about.admin"))) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatID);
                sendMessage.setText("Ism - familiyasi: Nosirov Ulugbek  \nYoshi: 18\nMutaxasisligi: Java developer\nO'qish joyi: PDP IT ACADEMY in Tashkent\nIsh joyi: Ishsiz\nLink: @ulugbek_nosirov\nBog'lanish: 1) +998903083823 ");
                bot.send(sendMessage);
            } else if (text.equals(PConfig.getInstance().getFromAppProperties("settings"))) {
                SendMessage sendMessage = new SendMessage(chatID, "Sozlamalar");
                sendMessage.setReplyMarkup(MarkupBoard.getInstance().settings());
                bot.send(sendMessage);
                sessionUser.setState(State.SETTINGS);
            } else if (sessionUser.getState().equals(State.SETTINGS) && text.equals(PConfig.getInstance().getFromAppProperties("change.language"))) {
                SendMessage sendMessage = new SendMessage(chatID, "Tilni tanlang:");
                sendMessage.setReplyMarkup(InlineBoard.getInstance().languages());
                bot.send(sendMessage);
            } else if (text.equals(PConfig.getInstance().getFromAppProperties("settings.back"))) {
                SendMessage sendMessage = new SendMessage(chatID, "Asosiy menu");
                if (authUserRepository.getRole(userID).equals(Role.USER.toString())) {
                    sendMessage.setReplyMarkup(MarkupBoard.getInstance().mainMenu());
                } else if (authUserRepository.getRole(userID).equals(Role.CONTENT_MANAGER.toString())) {
                    sendMessage.setReplyMarkup(MarkupBoard.getInstance().contentManagerMainMenu());
                } else {
                    sendMessage.setReplyMarkup(MarkupBoard.getInstance().adminMenu());
                }
                bot.send(sendMessage);
                sessionUser.setState(State.OTHER_PROCESS);
            } else if (text.equals(pConfig.getFromAppProperties("myBooks"))) {
                SendMessage sendMessage = new SendMessage(chatID, BookService.getMyBooksText(userID));
                bot.send(sendMessage);
            } else if (text.equals(pConfig.getFromAppProperties("newBooks")) && !authUserRepository.getRole(userID).equals(Role.USER.toString())) {
                sessionUser.setState(State.SEE_BOOKS_IN_PROCESS);
                sessionUser.setPage(1);
                BookService.searchInProcess();
                String results = BookService.getText();
                SendMessage sendMessage = new SendMessage();
                if (!results.equals("")) {
                    InlineKeyboardMarkup inlineKeyboard = BookService.getInlineKeyboard();
                    sendMessage = new SendMessage(chatID, results);
                    sendMessage.setReplyMarkup(inlineKeyboard);
                    bot.send(sendMessage);
                } else {
                    SendMessage sendMessage1 = new SendMessage(chatID, "Yangi kitoblar mavjud emas.");
                    bot.send(sendMessage1);
                }
            } else if (text.equals(pConfig.getFromAppProperties("send.advertisement")) && authUserRepository.getRole(userID).equals(Role.ADMIN.toString())) {
                SendMessage sendMessage = new SendMessage(chatID, "Bu bo'lim ustida ish olib borilyapti \uD83D\uDC68\u200D\uD83D\uDCBB");
                bot.send(sendMessage);
                sessionUser.setState(State.SEND_ADVERTISEMENT);
            } else if (sessionUser.getState().equals(State.SEARCH_BOOK)) {
                sessionUser.setPage(1);
                BookService.search(text);
                String results = BookService.getText();
                SendMessage sendMessage = new SendMessage();
                if (!results.equals("")) {
                    InlineKeyboardMarkup inlineKeyboard = BookService.getInlineKeyboard();
                    sendMessage = new SendMessage(chatID, results);
                    sendMessage.setReplyMarkup(inlineKeyboard);
                } else {
                    sendMessage = new SendMessage(chatID, "Bu nomdagi kitob mavjud emas😥");
                }
                bot.send(sendMessage);
            }
        } else if (message.hasDocument() && sessionUser.getState().equals(State.ADD_BOOK)) {
            Document document = message.getDocument();
            BookService.save(document, message.getFrom().getId().toString());
            SendMessage sendMessage = new SendMessage(chatID, "Kutubxonamizni boyitganingiz uchun rahmat 😐!");
            bot.send(sendMessage);
            SendMessage warning = new SendMessage(chatID, "⚠️Foydalanuvchilar yuborgan kitoblar bizning Content Manager tomonidan ko'rib chiqilgandan so'ng kutubxona bazasiga qo'shiladi.");
            bot.send(warning);
        }
        // TODO: 1/3/2022 Send advertisiment
        /*else if (authUserRepository.getRole(userID).equals(Role.ADMIN.toString()) && sessionUser.getState().equals(State.SEND_ADVERTISEMENT)) {
            ArrayList<AuthUser> users = AuthUserRepository.getInstance().getAll();
            if (authUserRepository.getRole(userID).equals(Role.ADMIN.toString()) && sessionUser.getState().equals(State.SEND_ADVERTISEMENT.toString())) {
                if (message.hasText()) {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setText(message.getText());
                    for (AuthUser user : users) {
                        sendMessage.setChatId(user.getChatID());
                        bot.send(sendMessage);
                    }
                }
            } else if (message.hasVideo()) {
                SendVideo sendVideo = new SendVideo();
                sendVideo.setVideo(new InputFile(message.getVideo().getFileId()));
                for (AuthUser user : users) {
                    sendVideo.setChatId(user.getChatID());
                    bot.send(sendVideo);
                }
            } else if (message.hasPhoto()) {
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setPhoto(new InputFile(message.getPhoto().get(1).getFileId()));
                for (AuthUser user : users) {
                    sendPhoto.setChatId(user.getChatID());
                    bot.send(sendPhoto);
                }
            }
            SendMessage sendMessage = new SendMessage(chatID, "Reklama jo'natildi");
            bot.send(sendMessage);
        }*/
    }
}