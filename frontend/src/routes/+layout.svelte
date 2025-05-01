<script>
    import "../style.css";
</script>

<style>
    .layout {
        display: flex;
        flex-direction: column;
        min-height: 100vh;
        background-color: #2F3E46;
        color: white;
        font-family: 'Lato', sans-serif;

    }

    header {
        display: flex;
        justify-content: center;
        align-items: center;
        font-size: 2.4rem;
        font-weight: bold;
        padding: 1rem;
        background-color: #1f2e35;
    }

    main {
        flex: 1;
        align-items: center;
        width: 100%;
        padding: 2rem;
        background-color: #354f52;
    }
    footer {
        text-align: center;
        padding: 1rem;
        background-color: #1f2e35;
        font-size: 0.9rem;
        color: #ccc;
        border-top: 1px solid #445;
    }

    footer a {
        color: #99c;
        text-decoration: none;
        margin: 0 0.5rem;
    }

    footer a:hover {
        text-decoration: underline;
    }
</style>

<div class="layout">
    <header>
        <a href="/" style="text-decoration: none;">
        <div class="primabetcon">
            <div class="primaLogo">CHESS</div><div class="betLogo">TRACKER</div>
        </div>
        </a>
    </header>

    <main>
        <slot />
    </main>
    <footer>
        <span>Follow me:</span>
        <a href="https://instagram.com/danyaminajj" target="_blank">Instagram</a> |
        <a href="https://github.com/whileyoungg" target="_blank">GitHub</a>
    </footer>
</div>
