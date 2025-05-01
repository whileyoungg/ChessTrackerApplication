package io.datarecords;

import java.util.ArrayList;

public record Player(String title,String username, String country, int player_id, int blitzRating, int rapidRating, int bulletRating, ArrayList<Match> recentGames) {
}
