package com.effcode.clean.me.rest;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.effcode.clean.me.expection.GlobalExceptionHandler;
import com.effcode.clean.me.handler.EmailHandler;
import com.effcode.clean.me.rest.dto.SendEmailRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = EmailRestController.class)
@Import(GlobalExceptionHandler.class)
class EmailRestControllerApiTest {

  @Autowired MockMvc mockMvc;

  @MockitoBean EmailHandler emailHandler;

  @Test
  void send_shouldReturn200_andIsSuccessTrue_whenHandlerReturnsTrue() throws Exception {
    // Arrange
    when(emailHandler.send(any(SendEmailRequestDto.class))).thenReturn(true);

    String requestJson =
        """
      {
        "recipients": ["john@doe.com"],
        "subject": "Hello",
        "body": "Test message"
      }
      """;

    // Act + Assert
    mockMvc
        .perform(
            post("/v1/email/send").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.errorMessage").doesNotExist());

    verify(emailHandler, times(1)).send(any(SendEmailRequestDto.class));
    verifyNoMoreInteractions(emailHandler);
  }

  @Test
  void send_shouldReturn400_andIsSuccessFalse_whenHandlerReturnsFalse() throws Exception {
    // Arrange
    when(emailHandler.send(any(SendEmailRequestDto.class))).thenReturn(false);

    String requestJson =
        """
      {
        "recipients": ["john@doe.com"],
        "subject": "Hello",
        "body": "Test message"
      }
      """;

    // Act + Assert
    mockMvc
        .perform(
            post("/v1/email/send").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.errorMessage").doesNotExist());

    verify(emailHandler, times(1)).send(any(SendEmailRequestDto.class));
    verifyNoMoreInteractions(emailHandler);
  }

  @Test
  void send_shouldReturn400_withErrorMessage_whenRecipientsMissing() throws Exception {
    // recipients null -> triggers @NotNull first (your advice returns the first field error)
    String invalidJson =
        """
      {
        "subject": "Hello",
        "body": "Test message"
      }
      """;

    mockMvc
        .perform(
            post("/v1/email/send").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.errorMessage").value(containsString("recipients:")))
        .andExpect(
            jsonPath("$.errorMessage")
                .value(containsString("Please specify at least one recipient")));

    verifyNoInteractions(emailHandler);
  }

  @Test
  void send_shouldReturn400_withErrorMessage_whenSubjectBlank() throws Exception {
    // subject blank -> @NotBlank
    String invalidJson =
        """
      {
        "recipients": ["john@doe.com"],
        "subject": "   ",
        "body": "Test message"
      }
      """;

    mockMvc
        .perform(
            post("/v1/email/send").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.errorMessage").value(containsString("subject:")))
        .andExpect(jsonPath("$.errorMessage").value(containsString("Subject must not be empty")));

    verifyNoInteractions(emailHandler);
  }

  @Test
  void send_shouldReturn400_withErrorMessage_whenRecipientsExceed50() throws Exception {
    // Create 51 recipients in JSON (simple but explicit)
    StringBuilder recipients = new StringBuilder();
    recipients.append("[");
    for (int i = 1; i <= 51; i++) {
      recipients.append("\"user").append(i).append("@example.com\"");
      if (i < 51) recipients.append(",");
    }
    recipients.append("]");

    String invalidJson =
        """
      {
        "recipients": %s,
        "subject": "Hello",
        "body": "Test message"
      }
      """
            .formatted(recipients);

    mockMvc
        .perform(
            post("/v1/email/send").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.errorMessage").value(containsString("recipients:")))
        .andExpect(
            jsonPath("$.errorMessage")
                .value(containsString("The maximum number of recipients is 50")));

    verifyNoInteractions(emailHandler);
  }

  @Test
  void send_shouldReturn500_andGenericMessage_whenUnexpectedExceptionThrown() throws Exception {
    when(emailHandler.send(any(SendEmailRequestDto.class))).thenThrow(new RuntimeException("boom"));

    String requestJson =
        """
      {
        "recipients": ["john@doe.com"],
        "subject": "Hello",
        "body": "Test message"
      }
      """;

    mockMvc
        .perform(
            post("/v1/email/send").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isInternalServerError())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.errorMessage").value("Something Went Wrong!"));

    verify(emailHandler, times(1)).send(any(SendEmailRequestDto.class));
  }
}
