package uz.pdp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.pdp.enums.auth.Status;
import uz.pdp.enums.book.BookStatus;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private Long id;
    private String fileID;
    private String name;
    private String sentAt;
    private String sentBy;
    private String status = Status.NON_ACTIVE.toString();
    private int like;
    private int dislike;
    private long download = 100;

    public Book(String fileID, String name, String sentAt, String sentBy) {
        this.fileID = fileID;
        this.name = name;
        this.sentAt = sentAt;
        this.sentBy = sentBy;
        this.status = BookStatus.IN_PROCESS.toString();
    }
}
