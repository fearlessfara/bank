package com.bok.bank.service;

import com.bok.bank.dto.CardInfoDTO;
import com.bok.bank.dto.CardDTO;
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

    @GetMapping("/getPlainPAN/{cardId}")
    public String getPlainPan(@RequestParam Long accountId, @PathVariable Long cardId);

    @GetMapping("/getCvv/{cardId}")
    public Integer getCvv(@RequestParam Long accountId, @PathVariable Long cardId);

    @PostMapping("/")
    public CardInfoDTO newCard(@RequestParam Long accountId, @RequestBody CardDTO cardDTO);
}
