package io.dataretreive;

import io.datarecords.Match;
import io.datarecords.Player;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

public class Dataretreivertest {

    @Test
    public void dataretreivertest() {
        Dataretreiver dataretreiver = new Dataretreiver(); // Pass username to constructor

        Player retreivedPlayer = dataretreiver.getPlayer("ocevn");       // Call no-args getPlayer()
        ArrayList<Match> retreivedMatches = dataretreiver.getMatches("ocevn"); // Call no-args getMatches()

        // Proper assertions
        assertThat(retreivedPlayer).isNotNull();
        assertThat(retreivedPlayer.username()).isEqualTo("ocevn");
        assertThat(retreivedMatches).isNotNull();
        assertThat(retreivedMatches).hasSizeLessThanOrEqualTo(40); // assert on collection size
        assertThat(retreivedPlayer.rapidRating()).isEqualTo(1704);
    }
}
