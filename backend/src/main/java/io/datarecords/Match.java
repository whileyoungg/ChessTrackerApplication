package io.datarecords;

public record Match(String matchUrl, String result, String mode,long endTime, String opponent, boolean isWhite,int playerRating, int opponentRating) {
}
