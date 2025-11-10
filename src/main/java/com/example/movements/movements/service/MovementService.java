package com.example.movements.movements.service;
import com.example.movements.movements.dto.MovementDto;
import com.example.movements.movements.dto.MovementListResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovementService {

    public MovementListResponse getMovementsByAccount(String accountId) {

        // Cuenta 1: ID = 12345
        List<MovementDto> movementsAccount1  = List.of(
                new MovementDto("MOV-1", "TRX-0001", -1500.0, "OUT", "amount"),
                new MovementDto("MOV-2", "TRX-0001", -2.0, "OUT", "commission"),
                new MovementDto("MOV-3", "TRX-0001", -15.0, "OUT", "ITF"),
                new MovementDto("MOV-4", null, 500.0, "IN", "abono")
        );

        // Cuenta 2: ID = 67890
        List<MovementDto> movementsAccount2 = List.of(
                new MovementDto("MOV-4", null, -250.0, "OUT", "Pago de servicios"),
                new MovementDto("MOV-5", null, -120.0, "OUT", "Cine"),
                new MovementDto("MOV-6", null, 1500.0, "IN", "Transferencia recibida")
        );

        // Cuenta 3: ID = 98765
        List<MovementDto> movementsAccount3 = List.of(
                new MovementDto("MOV-7", "TRX-002", -800.0, "OUT", "amount"),
                new MovementDto("MOV-8",  "TRX-002", -1.0, "OUT", "commission"),
                new MovementDto("MOV-9", "TRX-002", -10.0, "OUT", "ITF")
        );

        if ("12345".equals(accountId)) {
            return new MovementListResponse(accountId, 650.0, movementsAccount1);
        } else if ("67890".equals(accountId)) {
            return new MovementListResponse(accountId, 3130.0, movementsAccount2);
        } else if ("98765".equals(accountId)) {
            return new MovementListResponse(accountId, 4200.0, movementsAccount3);
        }
        return null;
    }
}

