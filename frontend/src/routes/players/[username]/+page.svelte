<script>
    import { page } from '$app/stores';
    import { onMount } from 'svelte';
    import { get } from 'svelte/store';
    import { goto } from '$app/navigation';


    let player = null;
    let lastgames = [];
    let username = get(page).params.username;
    function countryCodeToEmoji(countryCode) {
        return countryCode
            .toUpperCase()
            .split('')
            .map(c => String.fromCodePoint(127397 + c.charCodeAt()))
            .join('');
    }
    onMount(async () => {
        const res = await fetch(`http://localhost:8080/api/players/${username}`);
        if (res.ok) {
            player = await res.json();
            lastgames = player.recentGames || [];
        } else {
            console.error('Failed to fetch player');
        }
    });

</script>


<style>
    .player-header {
        display: flex;
        align-items: center;
        font-family: 'Arial', sans-serif;
        font-weight: bold;
        font-size: 2.5rem;
        color: #fff;
        margin-bottom: 1rem;
    }
    .flag {
        margin-left: 10px;
        width: 40px;
        height: 25px;
    }
    .ratings {
        display: flex;
        gap: 1.5rem;
        margin-bottom: 2rem;
    }
    .rating-card {
        background-color: #354f52;
        padding: 1rem;
        border-radius: 8px;
        color: #fff;
        font-size: 1.1rem;
        width: 150px;
        text-align: center;
    }
    .recent-games {
        background-color: #2f3e46;
        padding: 2rem;
        border-radius: 8px;
    }
    .game-card {
        background-color: #3b4d56;
        color: #fff;
        padding: 1.5rem;
        margin-bottom: 1rem;
        border-radius: 8px;
    }
    .game-header {
        display: flex;
        justify-content: space-between;
        margin-bottom: 0.8rem;
    }
    .game-mode {
        font-style: italic;
        color: #a8d0e6;
    }
    .game-result {
        font-weight: bold;
        color: #ffdd57;
    }

    .game-opponent {
        text-decoration: none; /* No underline on regular state */
        color: #fff; /* You can customize the color */
    }

    .game-opponent:hover {
        text-decoration: underline; /* Adds underline on hover */
        color: #ffdd57; /* Optionally change the color when hovered */
    }
    .match-url-btn {
        background-color: #4CAF50; /* Green */
        color: white;
        border: none;
        padding: 10px 20px;
        text-align: center;
        text-decoration: none;
        display: inline-block;
        align-items: flex-start;
        font-size: 10px;
        margin: 4px 2px;
        cursor: pointer;
        border-radius: 5px;
        transition: background-color 0.3s;
        width: 2cm;
        height: 1cm;
    }

    .match-url-btn:hover {
        background-color: #45a049;
    }
</style>

{#if player}
    <div class="player-header">
        {#if player.title !== ""}
            <h1>{player.title} {player.username}</h1>
        {:else}
            <h1>{player.username}</h1>
        {/if}
        <img src={`https://flagcdn.com/w40/${player.country.toLowerCase()}.png`} alt={player.country} class="flag" />
    </div>

    <div class="ratings">
        {#if player.blitzRating !== -1}
            <div class="rating-card">
                <h4>Blitz Rating</h4>
                <p>{player.blitzRating}</p>
            </div>
        {/if}

        {#if player.rapidRating !== -1}
            <div class="rating-card">
                <h4>Rapid Rating</h4>
                <p>{player.rapidRating}</p>
            </div>
        {/if}

        {#if player.bulletRating !== -1}
            <div class="rating-card">
                <h4>Bullet Rating</h4>
                <p>{player.bulletRating}</p>
            </div>
        {/if}
    </div>
    <button on:click={() => goto(`/players/${player.username}/playerCard`)} class="match-url-btn">
        Player Card
    </button>
    <div class="recent-games">
        <h3>Recent Games:</h3>
        {#if lastgames.length > 0}
            {#each lastgames as game}
                <div class="game-card">
                    <div class="game-header">
                        <span class="game-mode">{game.mode}</span>
                        <span
                                class="game-result"
                                style="color: {game.result === 'W' ? 'green' : game.result === 'D' ? 'yellow' : game.result === 'L' ? 'red' : 'black'};">
                        {game.result}
                    </span>
                    </div>
                    {#if game.isWhite}
                        <strong>{player.username} ({game.playerRating})</strong> vs
                        <a href={`/players/${game.opponent}`} class="game-opponent">{game.opponent} ({game.opponentRating})</a>
                    {:else}
                        <a href={`/players/${game.opponent}`} class="game-opponent">{game.opponent} ({game.opponentRating})</a> vs
                        <strong>{player.username} ({game.playerRating})</strong>
                    {/if}
                    {#if game.matchUrl}
                        <button
                                on:click={() => window.open(game.matchUrl, '_blank')}
                                class="match-url-btn">
                            View Match
                        </button>
                    {/if}
                </div>
            {/each}
        {:else}
            <p>No recent games available.</p>
        {/if}
    </div>
{:else}
    <p>Loading or player not found...</p>
{/if}
