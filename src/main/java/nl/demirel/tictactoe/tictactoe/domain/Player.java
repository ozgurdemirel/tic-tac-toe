package nl.demirel.tictactoe.tictactoe.domain;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    private String username;
    private char marker;

}
