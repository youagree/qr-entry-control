package ru.unit_techno.qr_entry_control_impl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.unit_techno.qr_entry_control_impl.service.CardService;

@RestController
@RequestMapping("/api/card")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping("/return/{cardId}")
    public void returnCard(@PathVariable String cardId, @RequestParam Long deviceId) {
        cardService.returnCard(cardId, deviceId);
    }
}
