package com.jobhunt.finder.telegramBot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramAPI extends TelegramLongPollingBot {

    private final String botToken = "6708726690:AAETLihsAiqCCIJfuMdTf93Z6piJJExHVtU";
    String chatId = null;


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
           chatId = update.getMessage().getChatId().toString(); // Get the chat ID from the update

            String message = update.getMessage().getText();
            if (message.startsWith("/jobpostings")) { // Check if the message is a job postings command
                CompletableFuture.supplyAsync(() -> sendResponseToBot(chatId, message))
                        .thenAccept(this::sendTelegramMessage);
            }
        }
    }

    private String sendResponseToBot(String message, String s) {
        String jobApiUrl = "http://localhost:8080/api/jobPostings";

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(jobApiUrl))
                .GET() // Use GET request
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                List<Map<String, String>> jobPostings = objectMapper.readValue(response.body(), new TypeReference<>() {});
                List<Map<String, String>> firstFiveJobPostings = jobPostings.stream().limit(5).collect(Collectors.toList());
                StringBuilder responseMessage = new StringBuilder("Latest job postings:\n");
                for (Map<String, String> job : firstFiveJobPostings) {
                    responseMessage.append(String.format("%s is hiring for %s\n", job.get("Company Name:"), job.get("Job Title:")));
                    responseMessage.append(String.format("Location: %s\n", job.get("Job Location:")));
                    responseMessage.append(String.format("Posting Date: %s\n", job.get("Posting DateTime:")));
                    responseMessage.append(String.format("Apply Now: %s\n", job.get("jobUrl")));
                   }

                return responseMessage.toString();
            } else {
                log.error("Failed to fetch job postings. Status code: {}", response.statusCode());
                return "Failed to fetch job postings.";
            }
        } catch (IOException | InterruptedException e) {
            log.error("Error sending GET request to job API", e);
            return "Error fetching job postings.";
        }
    }

    private void sendTelegramMessage(String responseMessage) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(responseMessage);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error sending message to Telegram", e);
        }
    }

    @Override
    public String getBotUsername() {
        return "Job_Hub_FinderBot";
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
