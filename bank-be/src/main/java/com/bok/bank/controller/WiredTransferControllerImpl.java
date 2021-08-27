package com.bok.bank.controller;

import com.bok.bank.helper.WiredTransferHelper;
import com.bok.bank.integration.dto.WiredTransferRequestDTO;
import com.bok.bank.integration.dto.WiredTransferResponseDTO;
import com.bok.bank.integration.service.WiredTransferController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WiredTransferControllerImpl implements WiredTransferController {

    @Autowired
    WiredTransferHelper wiredTransferHelper;
    @Override
    public WiredTransferResponseDTO transfer(Long accountId, WiredTransferRequestDTO wiredTransferRequestDTO) {
        return wiredTransferHelper.transfer(accountId, wiredTransferRequestDTO);
    }
}
