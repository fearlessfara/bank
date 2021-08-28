package com.bok.bank.integration.service;

import com.bok.bank.integration.dto.AuthorizationRequestDTO;
import com.bok.bank.integration.dto.AuthorizationResponseDTO;
import com.bok.bank.integration.dto.TransactionResponseDTO;
import com.bok.bank.integration.dto.WireTransferRequestDTO;
import com.bok.bank.integration.dto.WireTransferResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public interface TransactionController {

    @GetMapping("/allTransaction")
    List<TransactionResponseDTO> allTransaction(@RequestParam("accountId") Long accountId);

    @PostMapping("/authorize")
    AuthorizationResponseDTO authorize(@RequestParam("accountId") Long accountId, @RequestBody AuthorizationRequestDTO authorizationRequestDTO);

    @PostMapping("/wireTransfer")
    WireTransferResponseDTO wireTransfer(@RequestParam("accountId") Long accountId, @RequestBody WireTransferRequestDTO wiredTransferRequestDTO);

    @PostMapping("/card/{token}")
    List<TransactionResponseDTO> getCardTransaction(@RequestParam("accountId") Long accountId, @RequestParam("token") String token);

}
