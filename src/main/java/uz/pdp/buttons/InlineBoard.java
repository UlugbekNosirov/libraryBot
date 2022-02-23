package uz.pdp.buttons;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.pdp.emojis.Emoji;
import uz.pdp.models.Book;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InlineBoard {
    private static final InlineBoard instance = new InlineBoard();
    public InlineKeyboardMarkup languages() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton button1 = new InlineKeyboardButton(Emoji.uz + "UZ");
        button1.setCallbackData("uz");
        InlineKeyboardButton button2 = new InlineKeyboardButton(Emoji.ru + "RU");
        button2.setCallbackData("ru");
        InlineKeyboardButton button3 = new InlineKeyboardButton(Emoji.en + "EN");
        button3.setCallbackData("en");
        inlineKeyboardMarkup.setKeyboard(List.of(getRow(button1), getRow(button2), getRow(button3)));
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup makeInlineKeyboard(ArrayList<Book> books) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineKeyboard = new ArrayList();

        InlineKeyboardButton button1;
        for(int i = 0; i < books.size(); i += 2) {
            button1 = new InlineKeyboardButton(String.valueOf(i + 1));
            button1.setCallbackData((books.get(i)).getId().toString());
            if (i + 1 < books.size()) {
                InlineKeyboardButton button2 = new InlineKeyboardButton(String.valueOf(i + 2));
                button2.setCallbackData((books.get(i + 1)).getId().toString());
                inlineKeyboard.add(getRow(button1, button2));
            } else {
                inlineKeyboard.add(getRow(button1));
            }
        }

        InlineKeyboardButton next = new InlineKeyboardButton("↪ Next");
        next.setCallbackData("next");
        button1 = new InlineKeyboardButton("↩ Previous");
        button1.setCallbackData("previous");
        inlineKeyboard.add(getRow(button1, next));
        inlineKeyboardMarkup.setKeyboard(inlineKeyboard);
        return inlineKeyboardMarkup;
    }
    public InlineKeyboardMarkup check(String id) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton button1 = new InlineKeyboardButton("\uD83D\uDC4D");
        button1.setCallbackData("a" + id);

        InlineKeyboardButton button2 = new InlineKeyboardButton("\uD83D\uDE45\u200D♂️");
        button2.setCallbackData("r" + id);

        inlineKeyboardMarkup.setKeyboard(List.of(getRow(button1, button2)));
        return inlineKeyboardMarkup;
    }
    public InlineKeyboardMarkup like(String id) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton button1 = new InlineKeyboardButton("\uD83D\uDC4D");
        button1.setCallbackData("l" + id);

        InlineKeyboardButton button2 = new InlineKeyboardButton("\uD83D\uDC4E");
        button2.setCallbackData("d" + id);

        inlineKeyboardMarkup.setKeyboard(List.of(getRow(button1, button2)));
        return inlineKeyboardMarkup;
    }

    private List<InlineKeyboardButton> getRow(InlineKeyboardButton... keyboardButtons) {
        return List.of(keyboardButtons);
    }

    public static InlineBoard getInstance() {
        return instance;
    }
}
