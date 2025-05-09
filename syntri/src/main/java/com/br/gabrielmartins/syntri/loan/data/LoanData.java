package com.br.gabrielmartins.syntri.loan.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanData {
    private UUID playerId;
    private double amount;
    private double totalWithInterest;
    private LocalDateTime dueDate;
    private boolean paid;
}
