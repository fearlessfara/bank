package com.bok.bank.integration.service;

import com.bok.bank.integration.dto.CardDTO;
import com.bok.bank.integration.dto.CardInfoDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/card")
public interface CardController {

    @GetMapping("/allCards")
    public List<CardInfoDTO> allCards(@RequestParam Long accountId);

    @GetMapping("/{cardId}")
    public CardInfoDTO findCard(@RequestParam Long accountId, @PathVariable Long cardId);

    @GetMapping("/getPlainPAN/{cardId}/{PIN}")
    public String getPlainPan(@RequestParam Long accountId, @PathVariable Long cardId, @PathVariable String PIN);

    @GetMapping("/getCardToken/{cardId}")
    public String getCardToken(@RequestParam Long accountId, @PathVariable Long cardId);

    @GetMapping("/getCvv/{cardId}/{PIN}")
    public Integer getCvv(@RequestParam Long accountId, @PathVariable Long cardId, @PathVariable String PIN);

    @GetMapping("/getPIN/{cardId}")
    public String getPIN(@RequestParam Long accountId, @PathVariable Long cardId);

    @GetMapping("/changeCardStatus/{cardId}/{PIN}/{status}")
    public String changeCardStatus(@RequestParam Long accountId, @PathVariable Long cardId, @PathVariable String PIN, @PathVariable String status);

    @PostMapping("/create")
    public String createCard(@RequestParam Long accountId, @RequestBody CardDTO cardDTO);

    @GetMapping("/verify")
    String activation(@RequestParam("accountId") Long accountId, @PathVariable Long cardId, @PathVariable String PIN);
    @GetMapping("/delete")
    String delete(@RequestParam("accountId") Long accountId, @PathVariable Long cardId, @PathVariable String PIN);
}
