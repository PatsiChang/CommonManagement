package com.common.email.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Email {
    private String emailReceiver;
    private String receiverName;
    private String token;
    private String subject;
    private String message;
    private boolean isHTML;

}
