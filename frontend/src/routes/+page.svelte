<script>
    import '../style.css';
    import { goto } from '$app/navigation';
    let player = '';

    const submitSearch = (e) => {
        e.preventDefault();
        if (player.trim()) {
            goto(`/players/${player}`);
        }
    };
</script>

<style>
    .search-container {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 1.5rem;
        padding: 3rem 1rem;
    }

    form {
        display: flex;
        width: 100%;
        max-width: 500px;
        background: white;
        border-radius: 8px;
        overflow: hidden;
        box-shadow: 0 4px 12px rgba(0,0,0,0.2);
    }

    input[type="search"] {
        flex: 1;
        padding: 1rem;
        font-size: 1.6rem;
        border: none;
        outline: none;
    }

    button {
        background-color: #57bd84;
        color: white;
        font-weight: bold;
        padding: 0 1.5rem;
        border: none;
        font-size: 1.6rem;
        cursor: pointer;
        transition: background 0.2s ease;
    }

    button:hover {
        background-color: #48a470;
    }
</style>

<div class="search-container">
    <form on:submit={submitSearch}>
        <input
                id="search"
                type="search"
                placeholder="Search a player"
                bind:value={player}
                required
        />
        <button type="submit">Go</button>
    </form>
</div>

