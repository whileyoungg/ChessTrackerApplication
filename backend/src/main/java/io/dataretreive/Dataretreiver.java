package io.dataretreive;

import io.datarecords.Match;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.datarecords.Player;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
@Component
public class Dataretreiver {
private String correctNickname;
    public ArrayList<Match> getMatches(String username) {
        ArrayList<Match> matches = new ArrayList<>();

        ArrayList<String> endpoints = getLatestArchiveUrl(username);

        try {
            for(String endpoint : endpoints) {


                URL url = new URL(endpoint);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                int status = conn.getResponseCode();
                if (status != 200) {
                    throw new NoUserFoundException("User not found or API error: HTTP status " + status);
                }
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                );
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                conn.disconnect();

                // Parse JSON
                JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
                JsonArray games = jsonObject.getAsJsonArray("games");

                for (JsonElement gameElement : games) {
                    JsonObject game = gameElement.getAsJsonObject();

                    String urlString = game.get("url").getAsString();
                    String mode = game.get("time_class").getAsString();
                    String winner = game.has("winner") ? game.get("winner").getAsString() : "";
                    String white = game.getAsJsonObject("white").get("username").getAsString();
                    String black = game.getAsJsonObject("black").get("username").getAsString();
                    int whiteRating = game.getAsJsonObject("white").get("rating").getAsInt();
                    int blackRating = game.getAsJsonObject("black").get("rating").getAsInt();
                    String whiteResult = game.getAsJsonObject("white").get("result").getAsString();
                    String blackResult = game.getAsJsonObject("black").get("result").getAsString();

                    long endTime = game.get("end_time").getAsLong();
                    int playerRating = 0;
                    int opponentRating = 0;
                    String result = "?";
                    String opponentNickname = "";
                    boolean isWhite = false;
                    if (white.equalsIgnoreCase(username)) {
                        correctNickname = white;
                        opponentNickname = black;
                        playerRating = whiteRating;
                        opponentRating = blackRating;
                        isWhite = true;
                        if (whiteResult.equals("win")) {
                            result = "W";
                        } else if (whiteResult.equals(blackResult)) {
                            result = "D";
                        } else {
                            result = "L";
                        }
                    } else if (black.equalsIgnoreCase(username)) {
                        correctNickname = black;
                        opponentNickname = white;
                        isWhite = false;
                        playerRating = blackRating;
                        opponentRating = whiteRating;

                        if (blackResult.equals("win")) {
                            result = "W";
                        } else if (blackResult.equals(whiteResult)) {
                            result = "D";
                        } else {
                            result = "L";
                        }
                    }

                    matches.add(new Match(urlString, result, mode.toUpperCase(), endTime, opponentNickname, isWhite, playerRating, opponentRating));
                }
                matches.sort((m1, m2) -> Long.compare(m2.endTime(), m1.endTime()));

                matches = new ArrayList<>(matches.stream().limit(60).toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return matches;
    }
    public Player getPlayer(String username) {
        ArrayList<Match> matches = getMatches(username); // your last 5 matches
        String endpoint = "https://api.chess.com/pub/player/" + username;

        try {
            // Fetch basic player data
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int status = conn.getResponseCode();
            if (status != 200) {
                throw new NoUserFoundException("User not found or API error: HTTP status " + status);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            conn.disconnect();

            JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();

            // Extract fields
            String title = jsonObject.has("title") ? jsonObject.get("title").getAsString() : "";
            String countryUrl = jsonObject.get("country").getAsString();
            String country = countryUrl.substring(countryUrl.lastIndexOf("/") + 1); // extract country code
            int playerId = jsonObject.get("player_id").getAsInt();


            // Fetch ratings separately
            int blitzRating = -1;
            int rapidRating = -1;
            int bulletRating = -1;

            try {
                URL statsUrl = new URL("https://api.chess.com/pub/player/" + username + "/stats");
                HttpURLConnection statsConn = (HttpURLConnection) statsUrl.openConnection();
                statsConn.setRequestMethod("GET");

                BufferedReader statsReader = new BufferedReader(new InputStreamReader(statsConn.getInputStream()));
                StringBuilder statsResponse = new StringBuilder();
                String statsLine;
                while ((statsLine = statsReader.readLine()) != null) {
                    statsResponse.append(statsLine);
                }
                statsReader.close();
                statsConn.disconnect();

                JsonObject statsJson = JsonParser.parseString(statsResponse.toString()).getAsJsonObject();

                // Extract ratings for Blitz, Rapid, and Bullet from the response
                JsonObject blitzStats = statsJson.getAsJsonObject("chess_blitz");
                JsonObject rapidStats = statsJson.getAsJsonObject("chess_rapid");
                JsonObject bulletStats = statsJson.getAsJsonObject("chess_bullet");

                // Get the 'last' rating for each type of chess (e.g., blitz, rapid, bullet)
                if (blitzStats != null && blitzStats.has("last")) {
                    blitzRating = blitzStats.getAsJsonObject("last").get("rating").getAsInt();
                }
                if (rapidStats != null && rapidStats.has("last")) {
                    rapidRating = rapidStats.getAsJsonObject("last").get("rating").getAsInt();
                }
                if (bulletStats != null && bulletStats.has("last")) {
                    bulletRating = bulletStats.getAsJsonObject("last").get("rating").getAsInt();
                }

            } catch (Exception e) {
                System.out.println("Failed to fetch ratings for user: " + username);
            }

            // Now create and return Player object
            return new Player(title,correctNickname, country, playerId, blitzRating, rapidRating, bulletRating, matches);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public ArrayList<String> getLatestArchiveUrl(String username) {
        String endpoint = "https://api.chess.com/pub/player/" + username + "/games/archives";
        ArrayList<String> urls = new ArrayList<>();
        try {
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int status = conn.getResponseCode();
            if (status != 200) {
                throw new NoUserFoundException("User not found or API error: HTTP status " + status);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            conn.disconnect();

            JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
            JsonArray archives = jsonObject.getAsJsonArray("archives");

            if (archives.size() == 0) {
                return null;
            }
            if(archives.size()>=3){
                urls.add(archives.get(archives.size() - 1).getAsString());
                urls.add(archives.get(archives.size() - 2).getAsString());
                urls.add(archives.get(archives.size() - 3).getAsString());
                return urls;
            } else if(archives.size()==1){
                urls.add(archives.get(archives.size() - 1).getAsString());
                return urls;
            } else if(archives.size()==2){
                urls.add(archives.get(archives.size() - 1).getAsString());
                urls.add(archives.get(archives.size() - 2).getAsString());
                return urls;
            }



        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return urls;
    }

}
