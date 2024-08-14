package Paywise.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "messages")
public class Messages {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    public Messages() {
    }

    public String getEnc_Message() {
        return Enc_Message;
    }

    public void setEnc_Message(String enc_Message) {
        Enc_Message = enc_Message;
    }

    public LocalDateTime getCreated_On() {
        return Created_On;
    }

    public void setCreated_On(LocalDateTime created_On) {
        Created_On = created_On;
    }

    @Column(name = "Encrypted_message",columnDefinition = "varchar(60000)")

    private String Enc_Message;

    @Column(name = "Created_On")
    private LocalDateTime Created_On;


    public Messages(String message, LocalDateTime date) {
        super();
        this.Enc_Message = message;
        this.Created_On = date;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

}
