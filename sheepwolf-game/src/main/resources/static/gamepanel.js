class GamePanel {

    #gameboard;
    #stompClient;

    constructor(gameboard, stompClient) {
        this.#gameboard = gameboard;
        this.#stompClient = stompClient;
        this.#gameboard.onGameOver = (won) => this.#showResultModal(won ? 'win' : 'loss');
    }

    resign() {
        this.#stompClient.subscribe('/user/queue/game-cancelled', () => {
            this.#showResultModal('resign');
        });
        this.#gameboard.onGameOver = null; // Unsubscribe from game over to avoid showing two modals
        this.#stompClient.publish({ destination: '/game/cancel' });
    }

    #showResultModal(result) {
        const config = {
            win:    { emoji: '🏆', title: 'Zwycięstwo!',  sub: 'Gratulacje, wygrałeś tę partię!',  redirect: false },
            loss:   { emoji: '💀', title: 'Przegrana',    sub: 'Nie tym razem. Spróbuj jeszcze raz!', redirect: false },
            resign: { emoji: '🏳️', title: 'Zrezygnowano', sub: 'Za chwilę wrócisz do lobby…',       redirect: true  },
        }[result];

        const overlay = document.createElement('div');
        overlay.className = 'game-over-overlay';

        const box = document.createElement('div');
        box.className = 'game-over-box';

        const emoji = document.createElement('div');
        emoji.className = 'game-over-emoji';
        emoji.textContent = config.emoji;

        const title = document.createElement('div');
        title.className = 'game-over-title';
        title.textContent = config.title;

        const sub = document.createElement('div');
        sub.className = 'game-over-sub';
        sub.textContent = config.sub;

        box.appendChild(emoji);
        box.appendChild(title);
        box.appendChild(sub);

        if (config.redirect) {
            const progress = document.createElement('div');
            progress.className = 'resign-progress';
            const bar = document.createElement('div');
            bar.className = 'resign-progress-bar';
            progress.appendChild(bar);
            box.appendChild(progress);
            setTimeout(() => { window.location.href = '/'; }, 3000);
        } else {
            const btn = document.createElement('a');
            btn.className = 'btn-lobby';
            btn.href = '/';
            btn.textContent = '← Wróć do lobby';
            box.appendChild(btn);
        }

        overlay.appendChild(box);
        document.body.appendChild(overlay);
    }
}
