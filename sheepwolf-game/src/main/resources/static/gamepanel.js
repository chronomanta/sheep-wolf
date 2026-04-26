class GamePanel {

    #gameboard;

    constructor(gameboard) {
        this.#gameboard = gameboard;
        this.#gameboard.onGameOver = (won) => this.#showGameOverModal(won);
    }

    #showGameOverModal(won) {
        const overlay = document.createElement('div');
        overlay.className = 'game-over-overlay';

        const box = document.createElement('div');
        box.className = 'game-over-box';

        const emoji = document.createElement('div');
        emoji.className = 'game-over-emoji';
        emoji.textContent = won ? '🏆' : '💀';

        const title = document.createElement('div');
        title.className = 'game-over-title';
        title.textContent = won ? 'Zwycięstwo!' : 'Przegrana';

        const sub = document.createElement('div');
        sub.className = 'game-over-sub';
        sub.textContent = won ? 'Gratulacje, wygrałeś tę partię!' : 'Nie tym razem. Spróbuj jeszcze raz!';

        const btn = document.createElement('a');
        btn.className = 'btn-lobby';
        btn.href = '/';
        btn.textContent = '← Wróć do lobby';

        box.appendChild(emoji);
        box.appendChild(title);
        box.appendChild(sub);
        box.appendChild(btn);
        overlay.appendChild(box);
        document.body.appendChild(overlay);
    }
}

