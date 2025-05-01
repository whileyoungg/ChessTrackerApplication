package io.businessLogic;

import io.datarecords.Match;
import io.datarecords.Player;
import io.dataretreive.Dataretreiver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

public class PlayerManagerTest {
    @Mock
    Dataretreiver dataretreiver;

    Player player;
    Match match;
    @BeforeEach
    void setUp() {
        match = Mockito.mock(Match.class);
        dataretreiver = Mockito.mock(Dataretreiver.class);
        ArrayList<Match> matchList = new ArrayList<>();
        matchList.add(match);
        player = new Player("","ocevn","UA", 999,999,999, 15,matchList);
    }
    @Test
    public void getPlayerTest(){
        when(dataretreiver.getPlayer("ocevn")).thenReturn(player);
        dataretreiver.getPlayer("ocevn");
        verify(dataretreiver, times(1)).getPlayer("ocevn");
        assertThat(dataretreiver.getPlayer("ocevn").rapidRating()).isEqualTo(999);

    }
}
