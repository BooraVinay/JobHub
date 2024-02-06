package com.jobhunt.finder.controller;

import com.jobhunt.finder.telegramBot.TelegramAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequiredArgsConstructor
public class BotController {

    private final TelegramAPI telegramAPI;

    @PostMapping("/")
    public ResponseEntity<?> handleUpdate(@RequestBody Update update) {
        if (update == null || !update.hasMessage() || !update.getMessage().hasText()) {
            // Return a bad request response if the update is null or does not contain a message with text
            return ResponseEntity.badRequest().build();
        }

        try {
            // Handle the update using the Telegram API
            telegramAPI.onUpdateReceived(update);
            // Return an OK response if the update is successfully processed
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Log any exceptions that occur during update processing
            e.printStackTrace();
            // Return an internal server error response if an exception occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
