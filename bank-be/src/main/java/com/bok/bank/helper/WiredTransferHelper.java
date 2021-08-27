package com.bok.bank.helper;

import com.bok.bank.exception.BankAccountException;
import com.bok.bank.integration.dto.WiredTransferRequestDTO;
import com.bok.bank.integration.dto.WiredTransferResponseDTO;
import com.bok.bank.model.BankAccount;
import com.bok.bank.model.WiredTransfer;
import com.bok.bank.repository.BankAccountRepository;
import com.bok.bank.repository.WiredTransferRepository;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class WiredTransferHelper {

    @Autowired
    WiredTransferRepository wiredTransferRepository;

    @Autowired
    BankAccountRepository bankAccountRepository;

    //TODO complete this
    public WiredTransferResponseDTO transfer(Long accountId, WiredTransferRequestDTO wiredTransferRequestDTO) {
        Preconditions.checkNotNull(accountId, "accountId is null");
        Preconditions.checkNotNull(wiredTransferRequestDTO.destinationIBAN, "destinationIBAN is null");
        Preconditions.checkNotNull(wiredTransferRequestDTO.amount, "amount is null");
        Preconditions.checkNotNull(wiredTransferRequestDTO.executionDate, "executionDate is null");

        WiredTransferResponseDTO response = new WiredTransferResponseDTO();
        //check IBAN validity
        BankAccount sender = bankAccountRepository.findByAccountId(accountId).orElseThrow(BankAccountException::new);

        WiredTransfer wt = new WiredTransfer();
        wt.setAmount(wiredTransferRequestDTO.amount);
        wt.setSenderAccount(sender);
        wt.setBeneficiaryIBAN(wiredTransferRequestDTO.destinationIBAN);
        Optional<BankAccount> beneficiaryAccount = bankAccountRepository.findBankAccountByIBAN(wiredTransferRequestDTO.destinationIBAN);
        //if the IBAN is in our database then set also the account
        beneficiaryAccount.ifPresent(wt::setBeneficiaryAccount);

        if (wiredTransferRequestDTO.instantTransfer) {
            //do the transfer synchronously
            //the instant transfers have a fee (set it to 1%)
            wt.setExecutionDate(LocalDate.now());
            wt.setInstantTransfer(true);

            response.accepted = process(wt);

        } else {
            wt.setExecutionDate(wiredTransferRequestDTO.executionDate);
            wt.setInstantTransfer(false);
        }

        //do the transfer in async (using messages)
        return null;
    }


    //every day at 8 a.m. process the transfers scheduled for today
    @Scheduled(cron = "0 0 8 * * *")
    public void processScheduledTransfers() {
        List<WiredTransfer> transfers = wiredTransferRepository.findByInstantTransferIsFalseAndExecutionDateIsAndProcessedIsFalse(LocalDate.now());
        process(transfers);
    }

    public void process(List<WiredTransfer> transfers) {
        transfers.forEach(this::process);
    }

    public boolean process(WiredTransfer wiredTransfer) {
        //move the money and the set processed to true
        return true;
    }

}
