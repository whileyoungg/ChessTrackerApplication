<script>
    import { page } from '$app/stores';
    import { onMount } from 'svelte';
    import { get } from 'svelte/store';
    import { Chart, registerables } from 'chart.js';
    Chart.register(...registerables);

    let games = [];
    let username = get(page).params.username;
    let chartCanvas;
    let chartInstance;
    Chart.register(...registerables);
    onMount(async () => {
        const res = await fetch(`http://localhost:8080/api/players/${username}/blitz`);
        if (res.ok) {
            games = await res.json();
            console.log("Fetched games:", games);
        } else {
            console.error('Failed to fetch rapid games');
        }
    });
    function formatDate(timestamp) {
        // If it's in seconds, convert to milliseconds
        if (timestamp < 1000000000000) {
            timestamp *= 1000;
        }
        const date = new Date(timestamp);
        return date.toLocaleString(); // You can adjust toLocaleString options for formatting
    }

    function resultColor(result) {
        if (result === 'W') return 'green';
        if (result === 'D') return 'orange';
        if (result === 'L') return 'red';
        return 'gray';
    }
    function drawChart() {
        if (chartInstance) {
            chartInstance.destroy();
        }

        const reversedGames = [...games].reverse(); // clone & reverse safely

        const data = {
            labels: reversedGames.map((_, i) => `Game ${i + 1}`),
            datasets: [
                {
                    label: 'Rating Progression',
                    data: reversedGames.map(g => g.playerRating),
                    borderColor: '#4CAF50',
                    backgroundColor: '#81c784',
                    fill: false,
                    tension: 0.3
                }
            ]
        };


        const config = {
            type: 'line',
            data,
            options: {
                responsive: true,
                plugins: {
                    legend: { labels: { color: 'white' } }
                },
                scales: {
                    x: { ticks: { color: 'white' } },
                    y: { ticks: { color: 'white' } }
                }
            }
        };

        chartInstance = new Chart(chartCanvas, config);
    }

    $: if (games.length && chartCanvas) {
        drawChart();
    }
</script>


<style>
    .page-wrapper {
        color: white;
        font-family: 'Lato', sans-serif;
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
    canvas {
        margin-bottom: 2rem;
        background-color: #2f3e46;
        padding: 1rem;
        border-radius: 8px;
    }

    .match-url-btn:hover {
        background-color: #45a049;
    }
</style>

<div class="page-wrapper">
    <h2>Recent Rapid Games for <a href={`/players/${username}`} class="game-opponent">{username}</a></h2>
    {#if games.length}
        <canvas bind:this={chartCanvas}></canvas>
    {/if}
    {#if games.length > 0}
        {#each games as game}
            <div class="game-card">
                <div class="game-header">
                    <span class="game-mode">{game.mode}</span>
                    <span class="game-result" style="color: {resultColor(game.result)}">{game.result}</span>
                </div>
                <p style="color: #ccc; font-size: 0.9rem;">Ended on: {formatDate(game.endTime)}</p>
                {#if game.isWhite}
                    <strong>{username} ({game.playerRating})</strong> vs
                    <a href={`/players/${game.opponent}`} class="game-opponent">
                        {game.opponent} ({game.opponentRating})
                    </a>
                {:else}
                    <a href={`/players/${game.opponent}`} class="game-opponent">
                        {game.opponent} ({game.opponentRating})
                    </a> vs
                    <strong>{username} ({game.playerRating})</strong>
                {/if}

                {#if game.matchUrl}
                    <button on:click={() => window.open(game.matchUrl, '_blank')} class="match-url-btn">
                        View Match
                    </button>
                {/if}
            </div>
        {/each}
    {:else}
        <p>No games available.</p>
    {/if}
</div>
