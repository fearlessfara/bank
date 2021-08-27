package com.bok.bank.integration.service;

import com.bok.bank.integration.dto.WiredTransferRequestDTO;
import com.bok.bank.integration.dto.WiredTransferResponseDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wiredTransfer")
public interface WiredTransferController {

    @PostMapping("/transfer")
    WiredTransferResponseDTO transfer(@RequestParam("accountId") Long accountId, @RequestBody WiredTransferRequestDTO wiredTransferRequestDTO);
}
