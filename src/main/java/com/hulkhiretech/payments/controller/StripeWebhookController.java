package com.hulkhiretech.payments.controller;

import com.google.gson.Gson;
import com.hulkhiretech.payments.Service.Impl.StripeWebhookServiceImpl;
import com.hulkhiretech.payments.Service.Interface.StripeWebhookService;
import com.hulkhiretech.payments.dto.stripe.StripeEventDTO;
import com.hulkhiretech.payments.pojo.stripe.StripeEvent;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stripe/webhook")
@Slf4j
@RequiredArgsConstructor
public class StripeWebhookController {
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final StripeWebhookService stripeWebhookService;

   // @Value("${stripe.endPointSecret}")
    private  String  endPointSecret="whsec_29a3624461b211b3368989d72eab97f9b8052da2d6787c9253964efb336ec3d2";

    @PostMapping
    public ResponseEntity<String> addPost(@RequestBody String eventBody,
                                  @RequestHeader("Stripe-Signature") String sigHeader){
        log.info("Received event body :{} and Received signature :{}",eventBody,sigHeader);

        try {
            Webhook.constructEvent(eventBody,sigHeader,endPointSecret);
            log.info("Successfully Verify webhook ");
        } catch (Exception e) {
            log.error("Invalid Notification ");
            return ResponseEntity.badRequest().build();
        }

        //process the event
        //Check event type and according process it
        StripeEvent event=gson.fromJson(eventBody, StripeEvent.class);
        log.info("Received eventType :{}",event.getType());

        StripeEventDTO eventDTO=modelMapper.map(event, StripeEventDTO.class);
        log.info("Event DTO is :{}",eventDTO);

        stripeWebhookService.processEvent(eventDTO);

        return ResponseEntity.ok("DONE");
    }
}
