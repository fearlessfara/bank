package com.bok.bank.integration.service;

import com.bok.bank.integration.dto.CardDTO;
import com.bok.bank.integration.dto.CardInfoDTO;
import com.bok.bank.integration.dto.PinDTO;
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

    @PostMapping("/getPlainPAN/{cardId}")
    public String getPlainPan(@RequestParam Long accountId, @PathVariable Long cardId, @RequestBody PinDTO pinDTO);

    @GetMapping("/getCardToken/{cardId}")
    public String getCardToken(@RequestParam Long accountId, @PathVariable Long cardId);

    @PostMapping("/getCvv/{cardId}")
    public Integer getCvv(@RequestParam Long accountId, @PathVariable Long cardId, @RequestBody PinDTO pinDTO);

    @GetMapping("/getPIN/{cardId}")
    public String getPIN(@RequestParam Long accountId, @PathVariable Long cardId);

    @PostMapping("/changeCardStatus/{cardId}/{status}")
    public String changeCardStatus(@RequestParam Long accountId, @PathVariable Long cardId, @PathVariable String status, @RequestBody PinDTO pinDTO);

    @PostMapping("/create")
    public String createCard(@RequestParam Long accountId, @RequestBody CardDTO cardDTO);

    @PostMapping("/activation/{cardId}")
    String activation(@RequestParam("accountId") Long accountId, @PathVariable Long cardId, @RequestBody PinDTO pinDTO);

    @PostMapping("/delete/{cardId}")
    String delete(@RequestParam("accountId") Long accountId, @PathVariable Long cardId, @RequestBody PinDTO pinDTO);
}
