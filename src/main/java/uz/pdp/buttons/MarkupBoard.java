package uz.pdp.buttons;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;


public class MarkupBoard {
    private static final MarkupBoard instance = new MarkupBoard();

    public ReplyKeyboardMarkup mainMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(baseMenu());
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup contentManagerMainMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        KeyboardButton button1 = new KeyboardButton("Yangi kitoblar");
        List<KeyboardRow> keyboardRows = baseMenu();
        keyboardRows.get(0).add(button1);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup settings() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        KeyboardButton button1 = new KeyboardButton("Tilni o'zgartish");
        KeyboardButton button2 = new KeyboardButton("Orqaga");
        replyKeyboardMarkup.setKeyboard(List.of(getRow(button1, button2)));
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup adminMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        KeyboardButton button1 = new KeyboardButton("Reklama Jo'natish");
        KeyboardButton button2 = new KeyboardButton("Yangi kitoblar");
        List<KeyboardRow> lists = baseMenu();
        lists.get(0).add(button2);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(getRow(button1));
        keyboardRows.addAll(lists);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public List<KeyboardRow> baseMenu(KeyboardButton... buttons) {
        List<KeyboardRow> lists = new ArrayList<>();

        KeyboardButton button1 = new KeyboardButton("Izlash");
        KeyboardButton button2 = new KeyboardButton("Kitoblarim");
        KeyboardButton button3 = new KeyboardButton("Kitob qo'shish");
        KeyboardButton button4 = new KeyboardButton("Sozlamalar");
        KeyboardButton button5 = new KeyboardButton("Admin haqida");

        lists = List.of(getRow(button1), getRow(button2, button3), getRow(button4, button5));
        return lists;
    }

    private KeyboardRow getRow(KeyboardButton... keyboardButtons) {
        ArrayList<KeyboardButton> buttons = new ArrayList(List.of(keyboardButtons));
        return new KeyboardRow(buttons);
    }

    public static MarkupBoard getInstance() {
        return instance;
    }
}
