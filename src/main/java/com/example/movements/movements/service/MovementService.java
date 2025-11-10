package com.example.movements.movements.service;
import com.example.movements.movements.dto.MovementDto;
import com.example.movements.movements.dto.MovementListResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovementService {

    public MovementListResponse getMovementsByAccount(String accountId) {

        // MOCK:
        List<MovementDto> movements = List.of(
                new MovementDto("MOV-1", "TRX-0001", -1500.0, "OUT", "amount"),
                new MovementDto("MOV-2", "TRX-0001", -2.0, "OUT", "commission"),
                new MovementDto("MOV-3", "TRX-0001", -15.0, "OUT", "ITF"),
                new MovementDto("MOV-4", null, 500.0, "IN", "abono")
        );
        if ("12345".equals(accountId)) {
            return new MovementListResponse(
                    accountId,
                    650.0,
                    movements
            );
        }
        return null;
    }
}

