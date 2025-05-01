<script>
    export const prerender = true;
    export const ssr = true;
    export const csr = true;
    export const trailingSlash = 'ignore';

    export const load = async ({ params, fetch }) => {
        const res = await fetch(`/api/players/${params.username}`);
        const player = await res.json();
        return { player };
    };
    import { page } from '$app/stores';
    import { onMount } from 'svelte';
    import { get } from 'svelte/store';

    let player = null;
    let username = get(page).params.username;

    onMount(async () => {
        const res = await fetch(`http://localhost:8080/api/players/${username}`);
        if (res.ok) {
            player = await res.json();
        } else {
            console.error('Failed to fetch player');
        }
    });

    function resultColor(result) {
        if (result === 'W') return 'green';
        if (result === 'D') return 'orange';
        if (result === 'L') return 'red';
        return 'gray';
    }
</script>
<style>
    /* Minimal OBS styling */
    body {
        margin: 0;
        background: transparent;
        font-family: 'Lato', sans-serif;
        color: white;
    }
</style>
{#if player}
    <!-- Same compact UI layout as before -->
    <div style="text-align: center;">
        <h1>{player.title} {player.username}
            <img src={`https://flagcdn.com/w40/${player.country.toLowerCase()}.png`} width="30" />
        </h1>

        <div style="display: flex; justify-content: center; gap: 2rem;">
            {#if player.blitzRating !== -1}<div>Blitz<br>{player.blitzRating}</div>{/if}
            {#if player.rapidRating !== -1}<div>Rapid<br>{player.rapidRating}</div>{/if}
            {#if player.bulletRating !== -1}<div>Bullet<br>{player.bulletRating}</div>{/if}
        </div>

        <div style="margin-top: 1rem;">Recent Results:
            {#each player.recentGames.slice(0, 5) as game}
                <span style="color: {resultColor(game.result)};">{game.result}</span>
            {/each}
        </div>
    </div>
{/if}