package org.example.swtest_day3_practice.user;

import org.springframework.stereotype.Component;

@Component
public class EmailClient {
    public void sendWelcomeEmail(String email) {
        System.out.println("이메일 발송 : " + email);
    }
}
