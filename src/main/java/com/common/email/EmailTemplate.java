package com.common.email;

import com.common.bean.Email;
import com.common.utils.DateHelper;
import lombok.AllArgsConstructor;

import java.util.function.Function;

//@AllArgsConstructor
//public enum EmailTemplate {
//    SIGN_UP((email) -> "Welcome to Smart Home!",
//        (email) -> {
//            SignUpEmail signUpEmail = (SignUpEmail) email;
//            return String.join(" ",
//                "You have signed up on",
//                DateHelper.formatLocalDateTime(signUpEmail.getSignUpTime()), ".",
//                getSystemNewChar(),
//                "You can sign to your account with following URL:",
//                signUpEmail.getAccountUrl());
//        }, defaultEmailFooter()),
//    UNSUBSCRIBED((email) -> {
//        return "";
//    }, (email) -> {
//        ((UnsubscribeEmail) email).getBeenUnsubsribedCount();
//        return "this person has been unsub for {} times. Why you are so mean";
//    }, defaultEmailFooter());

//    private Function<Email, String> buildSubject;
//    private Function<Email, String> buildEmailBody;
//    private Function<Email, String> buildFooter;
//
//    private static Function<Email, String> defaultEmailSubject() {
//        return (email) -> "Notification from smart home";
//    }
//
//    private static Function<Email, String> defaultEmailFooter() {
//        return (email) -> "Best Regards, \n Smart Home Teams";
//    }
//
//    private static String getSystemNewChar() {
//        return "\n";
//    }
//}
