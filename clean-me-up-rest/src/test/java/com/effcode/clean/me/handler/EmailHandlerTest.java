package com.effcode.clean.me.handler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.effcode.clean.me.config.EmailProperties;
import com.effcode.clean.me.mapper.EmailMapper;
import com.effcode.clean.me.rest.dto.SendEmailRequestDto;
import com.effcode.clean.me.support.SmtpEmail;
import com.effcode.clean.me.support.SmtpHandler;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailHandlerTest {

  @Mock private SmtpHandler smtpHandler;
  @Mock private EmailMapper emailMapper;
  @Mock private EmailProperties emailProperties;

  @InjectMocks private EmailHandler emailHandler;

  @Captor private ArgumentCaptor<SmtpEmail> smtpEmailCaptor;

  @Test
  void send_shouldSetCredentials_andPost() {
    // Arrange
    SendEmailRequestDto dto = new SendEmailRequestDto();
    dto.setRecipients(List.of("a@b.com"));
    dto.setSubject("subj");
    dto.setBody("body");

    SmtpEmail mapped = new SmtpEmail();
    mapped.adrs = new String[] {"a@b.com"};

    when(emailMapper.toSmtpEmail(dto)).thenReturn(mapped);
    when(emailProperties.getUsername()).thenReturn("user");
    when(emailProperties.getPassword()).thenReturn("pass");

    // Act
    boolean result = emailHandler.send(dto);

    // Assert
    assertTrue(result);

    verify(emailMapper).toSmtpEmail(dto);
    verify(smtpHandler).post(smtpEmailCaptor.capture());

    SmtpEmail posted = smtpEmailCaptor.getValue();
    assertEquals("user", posted.username);
    assertEquals("pass", posted.password);
    assertArrayEquals(new String[] {"a@b.com"}, posted.adrs);

    verifyNoMoreInteractions(smtpHandler, emailMapper, emailProperties);
  }

  @Test
  void send_whenDuplicates_shouldRemoveDuplicates_beforePost() {
    // Arrange
    SendEmailRequestDto dto = new SendEmailRequestDto();
    dto.setRecipients(List.of("dup@b.com", "x@y.com", "dup@b.com"));
    dto.setSubject("subj");
    dto.setBody("body");

    SmtpEmail mapped = new SmtpEmail();
    // mapper returns duplicates (typical behavior if it maps directly from dto list)
    mapped.adrs = new String[] {"dup@b.com", "x@y.com", "dup@b.com"};

    when(emailMapper.toSmtpEmail(dto)).thenReturn(mapped);
    when(emailProperties.getUsername()).thenReturn("user");
    when(emailProperties.getPassword()).thenReturn("pass");

    // Act
    boolean result = emailHandler.send(dto);

    // Assert
    assertTrue(result);

    verify(smtpHandler).post(smtpEmailCaptor.capture());
    SmtpEmail posted = smtpEmailCaptor.getValue();

    assertEquals("user", posted.username);
    assertEquals("pass", posted.password);

    assertNotNull(posted.adrs);
    assertEquals(2, posted.adrs.length);
    assertTrue(List.of(posted.adrs).containsAll(List.of("dup@b.com", "x@y.com")));
  }

  @Test
  void send_whenNoDuplicates_shouldNotOverrideMappedAddresses() {
    // Arrange
    SendEmailRequestDto dto = new SendEmailRequestDto();
    dto.setRecipients(List.of("a@b.com", "c@d.com"));
    dto.setSubject("subj");
    dto.setBody("body");

    SmtpEmail mapped = new SmtpEmail();
    mapped.adrs = new String[] {"a@b.com", "c@d.com"};

    when(emailMapper.toSmtpEmail(dto)).thenReturn(mapped);
    when(emailProperties.getUsername()).thenReturn("user");
    when(emailProperties.getPassword()).thenReturn("pass");

    // Act
    emailHandler.send(dto);

    // Assert
    verify(smtpHandler).post(smtpEmailCaptor.capture());
    SmtpEmail posted = smtpEmailCaptor.getValue();

    // Since there are no duplicates, handler should not replace smtpEmail.adrs
    assertSame(mapped.adrs, posted.adrs);
    assertArrayEquals(new String[] {"a@b.com", "c@d.com"}, posted.adrs);
  }

  @Test
  void send_shouldCallMapperBeforePosting() {
    // Arrange
    SendEmailRequestDto dto = new SendEmailRequestDto();
    dto.setRecipients(List.of("a@b.com"));
    dto.setSubject("subj");
    dto.setBody("body");

    SmtpEmail mapped = new SmtpEmail();
    when(emailMapper.toSmtpEmail(dto)).thenReturn(mapped);
    when(emailProperties.getUsername()).thenReturn("user");
    when(emailProperties.getPassword()).thenReturn("pass");

    // Act
    emailHandler.send(dto);

    // Assert (interaction order)
    var inOrder = inOrder(emailMapper, emailProperties, smtpHandler);
    inOrder.verify(emailMapper).toSmtpEmail(dto);
    inOrder.verify(emailProperties).getPassword();
    inOrder.verify(emailProperties).getUsername();
    inOrder.verify(smtpHandler).post(any(SmtpEmail.class));
  }
}
