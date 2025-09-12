package io.datarecords;

import java.time.Instant;

public record Match(String matchUrl, String result, String mode, Instant endTime, String opponent, boolean isWhite, int playerRating, int opponentRating) {
}
