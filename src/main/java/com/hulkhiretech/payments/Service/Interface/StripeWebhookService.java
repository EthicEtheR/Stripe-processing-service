package com.hulkhiretech.payments.Service.Interface;

import com.hulkhiretech.payments.dto.stripe.StripeEventDTO;

public interface StripeWebhookService {
     void processEvent(StripeEventDTO eventDTO);
}
